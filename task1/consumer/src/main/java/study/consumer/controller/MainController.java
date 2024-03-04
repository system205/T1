package study.consumer.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import study.consumer.dto.Category;
import study.consumer.dto.Product;
import study.consumer.dto.ProductsCategories;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class MainController {
    private final RestTemplate restTemplate;

    @Value("${supplier.url:http://localhost:8080/api/v1}")
    private String url;

    @PostConstruct
    private void info() {
        log.info("Supplier URL: {}", url);
    }

    @GetMapping
    public ProductsCategories getProductsCategories() {
        final Product[] products = Objects.requireNonNull(
            restTemplate.getForObject(url + "/products", Product[].class));
        final Category[] categories = Objects.requireNonNull(
            restTemplate.getForObject(url + "/categories", Category[].class));

        log.info("Number of products: {}", products.length);
        log.info("Number of categories: {}", categories.length);

        return new ProductsCategories(List.of(products), List.of(categories));
    }

    private boolean filterByPrice(Product product, Double minPrice, Double maxPrice) {
        if (product.getPrice() == null) return false;
        if (minPrice == null && maxPrice == null) return true;
        if (minPrice == null) return product.getPrice() <= maxPrice;
        if (maxPrice == null) return product.getPrice() >= minPrice;
        return product.getPrice() >= minPrice && product.getPrice() <= maxPrice;
    }

    @GetMapping("/products")
    public ResponseEntity<Iterable<Product>> getProducts(
        @RequestParam(defaultValue = "0") Double minPrice,
        @RequestParam(defaultValue = "99999999") Double maxPrice,
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "") String nameContains,
        @RequestParam(defaultValue = "") String descriptionContains,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request: minPrice={}, maxPrice={}, category={}, nameContains={}, descriptionContains={}, page={}, size={}",
            minPrice, maxPrice, category, nameContains, descriptionContains, page, size);

        final Product[] products = Objects.requireNonNull(
            restTemplate.getForObject(url + "/products", Product[].class)
        );

        log.info("Number of products: {}", products.length);

        List<Product> filteredProducts = Arrays.stream(products)
            .filter(product -> filterByPrice(product, minPrice, maxPrice))
            .filter(product -> category == null || product.getCategory() != null && product.getCategory().getName().equals(category))
            .filter(product -> product.getName() != null && product.getName().toLowerCase().contains(nameContains.toLowerCase()))
            .filter(product -> product.getDescription() != null && product.getDescription().toLowerCase().contains(descriptionContains.toLowerCase()))
            .toList();

        log.info("Number of filtered products: {}", filteredProducts.size());


        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(page));
        headers.add("X-Page-Size", String.valueOf(size));
        headers.add("X-Total-Elements", String.valueOf(filteredProducts.size()));
        headers.add("X-Total-Pages", String.valueOf(Math.ceil(filteredProducts.size() * 1f / size)));

        List<Product> pagedProducts = filteredProducts.stream()
            .skip((long) page * size)
            .limit(size)
            .toList();

        return ResponseEntity.ok().headers(headers).body(pagedProducts);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody @Valid Product product) {
        if (product.getCategory() != null) // Make sure to have the category
            product.setCategory(restTemplate.postForObject(url + "/categories", product.getCategory(), Category.class));
        return restTemplate.postForObject(url + "/products", product, Product.class);
    }

    @PutMapping("/products/{id}")
    public void updateProduct(@PathVariable Long id,
                              @RequestBody @Valid Product product) {
        restTemplate.put(url + "/products/" + id, product);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        restTemplate.delete(url + "/products/" + id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
            error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getName(), "Check that you specified this parameter correctly");
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleNotReadableMessage(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Check that you specified the parameters in body correctly");
        return errors;
    }

}
