package study.consumer.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import study.consumer.dto.Category;
import study.consumer.dto.Paged;
import study.consumer.dto.Product;
import study.consumer.dto.ProductsCategories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
    public ResponseEntity<ProductsCategories> getProductsCategories() {
        final Product[] products = Objects.requireNonNull(
            restTemplate.getForObject(url + "/products", Product[].class));
        final Category[] categories = Objects.requireNonNull(
            restTemplate.getForObject(url + "/categories", Category[].class));

        log.info("Number of products: {}", products.length);
        log.info("Number of categories: {}", categories.length);

        return ResponseEntity.ok(new ProductsCategories(List.of(products), List.of(categories)));
    }

    @GetMapping("/products")
    public ResponseEntity<Iterable<Product>> getProducts(
        @RequestParam(defaultValue = "0") Double minPrice,
        @RequestParam(defaultValue = "99999999") Double maxPrice,
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "") String nameContains,
        @RequestParam(defaultValue = "", name = "descContains") String descriptionContains,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Request: minPrice={}, maxPrice={}, category={}, nameContains={}, descriptionContains={}, page={}, size={}",
            minPrice, maxPrice, category, nameContains, descriptionContains, page, size);

        final Paged<List<Product>> pagedFilteredProducts =
            restTemplate.exchange(
                """
                    %s/products?page=%d\
                    &size=%d\
                    &minPrice=%f\
                    &maxPrice=%f\
                    &category=%s\
                    &nameContains=%s\
                    &descContains=%s""".formatted(url, page, size,
                    minPrice, maxPrice,
                    category, nameContains,
                    descriptionContains), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Paged<List<Product>>>() {
                }).getBody();
        assert pagedFilteredProducts != null;

        final List<Product> products = pagedFilteredProducts.content();
        log.info("Number of products: {}", products.size());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(page));
        headers.add("X-Page-Size", String.valueOf(size));
        headers.add("X-Total-Elements", String.valueOf(products.size()));
        headers.add("X-Total-Pages", String.valueOf(pagedFilteredProducts.totalPages()));

        return ResponseEntity.ok().headers(headers).body(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
        if (product.getCategory() != null) // Make sure to have the category
            product.setCategory(restTemplate.postForObject(url + "/categories", product.getCategory(), Category.class));
        final Product postedProduct = restTemplate.postForObject(url + "/products", product, Product.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id,
                                              @RequestBody @Valid Product product) {
        restTemplate.put(url + "/products/" + id, product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        restTemplate.delete(url + "/products/" + id);
        return ResponseEntity.noContent().build();
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

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ResourceAccessException.class)
    public Map<String, String> handleResourceAccessException(ResourceAccessException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Supplier service is not available. Try later.");
        return errors;
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(RestClientException.class)
    public Map<String, String> handleRestClientException(RestClientException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Supplier service is not working properly. Try later.");
        return errors;
    }
}
