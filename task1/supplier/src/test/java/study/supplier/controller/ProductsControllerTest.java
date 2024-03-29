package study.supplier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.supplier.SupplierApplicationTests;
import study.supplier.entity.Product;
import study.supplier.repository.ProductRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
class ProductsControllerTest extends SupplierApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private ObjectWriter writer;

    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @BeforeEach
    void setUpUrl(){
        url = "http://localhost:"+port;
    }

    @Test
    @Transactional
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        Product product = new Product("Test Product", "Test Description", 10.0, null);


        String requestJson = writer.writeValueAsString(product);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(product.getName()))
            .andExpect(jsonPath("$.description").value(product.getDescription()))
            .andExpect(jsonPath("$.price").value(product.getPrice()))
            .andExpect(jsonPath("$.category").value(product.getCategory()));
    }

    @Test
    void getProducts_shouldReturnListOf4InitialProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @Test
    void getPagedProducts_shouldReturnListOfProductsWithPage() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .param("page", "3")
                .param("size", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(jsonPath("$.content[0].id", is(4)));
    }

    @Test
    @Transactional
    void getProductById_shouldReturnProductWithMatchingId() throws Exception {
        Product product = new Product("Test Product", "Test Description", 10.0, null);
        product = productRepository.save(product);

        mockMvc.perform(get("/api/v1/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(product.getName()))
            .andExpect(jsonPath("$.description").value(product.getDescription()))
            .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void getProductById_shouldReturnNullIfNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/{id}", 9999))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @Transactional
    void updateProduct_shouldUpdateExistingProduct() throws Exception {
        Product product = new Product("Test", "ToUpdate", 1E4, null);
        product = productRepository.save(product);

        product.setName("Updated Product");
        product.setPrice(15.0);

        mockMvc.perform(put("/api/v1/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(product.getName()))
            .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void deleteProduct_shouldDeleteProductWithMatchingId() throws Exception {
        Product product = new Product("ToDelete", "ToDelete", 123.0, null);
        product = productRepository.save(product);

        mockMvc.perform(delete("/api/v1/products/{id}", product.getId()))
            .andExpect(status().isOk());

        assertFalse(productRepository.findById(product.getId()).isPresent());
    }

}
