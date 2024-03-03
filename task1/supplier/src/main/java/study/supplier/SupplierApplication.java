package study.supplier;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import study.supplier.entity.Category;
import study.supplier.entity.Product;
import study.supplier.repository.CategoryRepository;
import study.supplier.repository.ProductRepository;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SupplierApplication {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(SupplierApplication.class, args);
    }


    @PostConstruct
    private void addTestData() {
        // Add categories
        Category food = new Category("Food");
        Category electronics = new Category("Electronics");
        Category clothes = new Category("Clothes");
        categoryRepository.saveAll(List.of(food, electronics, clothes));


        // Add products
        productRepository.saveAll(List.of(
                new Product("Bread", "Tasty", 1.0, food),
                new Product("Milk", "Fresh", 1.5, food),
                new Product("TV", "Smart", 500.0, electronics),
                new Product("T-shirt", "Cotton", 10.0, clothes)
        ));

        log.info("Test data added");
        log.info("Categories in the database: " + categoryRepository.findAll().size());
        log.info("Products in the database: " + productRepository.findAll().size());
    }


}
