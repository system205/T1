package study.consumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import study.consumer.dto.Category;
import study.consumer.dto.Product;
import study.consumer.dto.ProductsCategories;

import java.util.List;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class MainController {
    private final RestTemplate restTemplate;

    @GetMapping("/all")
    public ProductsCategories getProductsCategories() {
        final Product[] products = Objects.requireNonNull(restTemplate.getForObject("http://localhost:8080/api/v1/products", Product[].class));
        final Category[] categories = Objects.requireNonNull(restTemplate.getForObject("http://localhost:8080/api/v1/categories", Category[].class));
        log.info("Number of products: {}", products.length);
        log.info("Number of categories: {}", categories.length);
        return new ProductsCategories(List.of(products), List.of(categories));
    }
}
