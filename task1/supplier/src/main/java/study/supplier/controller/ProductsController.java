package study.supplier.controller;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import study.supplier.dto.Paged;
import study.supplier.entity.Product;
import study.supplier.entity.QProduct;
import study.supplier.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductRepository productRepository;
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping
    public Paged<Iterable<Product>> getProducts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                @RequestParam(defaultValue = "0") Double minPrice,
                                                @RequestParam(defaultValue = "99999999") Double maxPrice,
                                                @RequestParam(required = false) String category,
                                                @RequestParam(defaultValue = "") String nameContains,
                                                @RequestParam(defaultValue = "", name = "descContains") String descriptionContains) {
        final QProduct qProduct = QProduct.product;
        final BooleanExpression predicate = qProduct
            .price.between(minPrice, maxPrice)
            .and(qProduct.name.containsIgnoreCase(nameContains))
            .and(qProduct.description.containsIgnoreCase(descriptionContains))
            .and(category == null ? Expressions.TRUE : qProduct.category.name.equalsIgnoreCase(category));

        final Page<Product> products = productRepository.findAll(predicate, PageRequest.of(page, size));
        final int totalPages = products.getTotalPages();
        final List<Product> content = products.getContent();

        return new Paged<>(content, totalPages);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

}
