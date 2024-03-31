package study.supplier;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import study.supplier.controller.CategoriesController;
import study.supplier.controller.ProductsController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class SupplierApplicationTests {
    @BeforeEach
    void setUpUrl(@LocalServerPort Integer port){
        RestAssured.baseURI = "http://localhost:"+port;
    }

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres");
    @BeforeAll
    static void beforeAll(){
        postgreSQLContainer.start();
        log.info("The postgres test container is started");
    }

    @AfterAll
    static void afterAll(){
        log.info("The postgres test container is stopping");
        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    protected ProductsController productsController;

    @Autowired
    protected CategoriesController categoriesController;

    @Test
    void contextLoads() {
        assertThat(productsController).isNotNull();
        assertThat(categoriesController).isNotNull();
        log.info("The context loaded successfully");
    }

}
