package study.supplier.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.server.LocalServerPort;
import study.supplier.SupplierApplicationTests;
import study.supplier.entity.Product;
import study.supplier.repository.ProductRepository;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@AutoConfigureMockMvc
class ProductsControllerTest extends SupplierApplicationTests {
    @Autowired
    private ProductRepository productRepository;
    @BeforeEach
    void setUpUrl(@LocalServerPort Integer port) {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        Product product = new Product("Test POST Product", "Test Description", 10.0, null);

        Product created = given()
            .contentType(ContentType.JSON)
            .body(product)
            .when()
            .post("/api/v1/products")
            .then()
            .statusCode(200)
            .body("name", equalTo(product.getName()))
            .body("description", equalTo(product.getDescription()))
            .body("category", nullValue())
            .body("id", notNullValue())
            .extract().as(Product.class);

        assertEquals(created.getPrice(), product.getPrice());
    }

    @Test
    void getProducts_shouldReturnListOf4InitialProducts() throws Exception {
        productRepository.saveAll(List.of(
            new Product("P1", "P1D", 1D, null),
            new Product("P2", "P1D", 2D, null),
            new Product("P3", "P1D", 3D, null),
            new Product("P4", "P1D", 4D, null)
        ));

        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/v1/products")
            .then()
            .statusCode(200)
            .body("content", hasSize(4));
    }

    @Test
    void getPagedProducts_shouldReturnListOfProductsWithPage() throws Exception {
        productRepository.saveAll(List.of(
            new Product("P1", "P1D", 1D, null),
            new Product("P2", "P1D", 2D, null),
            new Product("P3", "P1D", 3D, null),
            new Product("P4", "P1D", 4D, null)
        ));

        given()
            .when()
            .param("page", "3")
            .param("size", "1")
            .get("/api/v1/products")
            .then()
            .statusCode(200)
            .body("content", hasSize(1))
            .body("content[0].name", equalTo("P4"));
    }

    @Test
    void getProductById_shouldReturnProductWithMatchingId() throws Exception {
        Product product = new Product("Test Product", "Test Description", 10.0, null);
        product = productRepository.save(product);

        final Product searchedProduct = given()
            .when()
            .get("/api/v1/products/{id}", product.getId())
            .then()
            .statusCode(200)
            .body("name", equalTo(product.getName()))
            .body("description", equalTo(product.getDescription()))
            .extract().as(Product.class);

        Assertions.assertEquals(product.getPrice(), searchedProduct.getPrice());
        Assertions.assertEquals(product.getId(), searchedProduct.getId());
    }

    @Test
    void getProductById_shouldReturnNullIfNotFound() throws Exception {
        given()
            .when()
            .get("/api/v1/products/{id}", 9999)
            .then()
            .statusCode(200)
            .body(emptyOrNullString());
    }

    @Test
    void updateProduct_shouldUpdateExistingProduct() throws Exception {
        Product product = new Product("Test", "ToUpdate", 1E4, null);
        product = productRepository.save(product);

        product.setName("Updated Product");
        product.setPrice(15.0);

        final Product retrievedProduct = given()
            .contentType(ContentType.JSON)
            .body(product)
            .when()
            .put("/api/v1/products/{id}", product.getId())
            .then()
            .statusCode(200)
            .body("name", equalTo(product.getName()))
            .extract().as(Product.class);

        assertEquals(product.getPrice(), retrievedProduct.getPrice());
    }

    @Test
    void deleteProduct_shouldDeleteProductWithMatchingId() throws Exception {
        Product product = new Product("ToDelete", "ToDelete", 123.0, null);
        product = productRepository.save(product);

        given()
            .when()
            .delete("/api/v1/products/{id}", product.getId())
            .then()
            .statusCode(200);

        assertFalse(productRepository.findById(product.getId()).isPresent());
    }

}
