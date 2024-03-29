package study.supplier.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.supplier.SupplierApplicationTests;
import study.supplier.entity.Category;
import study.supplier.repository.CategoryRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CategoriesControllerTest extends SupplierApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private ObjectWriter writer;

    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        writer = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @Transactional
    void createCategory_shouldReturnCreatedCategoryAndCreatedStatus() throws Exception {
        Category Category = new Category("Test Category");
        String requestJson = writer.writeValueAsString(Category);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(Category.getName()));
    }

    @Test
    void createDuplicatedCategory_shouldReturnOkStatus() throws Exception {
        Category Category = new Category("Food");
        String requestJson = writer.writeValueAsString(Category);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(Category.getName()));
    }

    @Test
    void getCategories_shouldReturnListOf3InitialCategories() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Transactional
    void getCategoryById_shouldReturnCategoryWithMatchingId() throws Exception {
        Category category = new Category("Test2 Category");
        category = categoryRepository.save(category);

        mockMvc.perform(get("/api/v1/categories/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void getCategoryById_shouldReturnNullIfNotFound() throws Exception {
        // Perform GET request with a non-existent ID
        mockMvc.perform(get("/api/v1/categories/{id}", 9999))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @Transactional
    void updateCategory_shouldUpdateExistingCategory() throws Exception {
        Category category = new Category("ToUpdate");
        category = categoryRepository.save(category);

        category.setName("Updated Category");

        mockMvc.perform(put("/api/v1/categories/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(writer.writeValueAsString(category)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void deleteCategory_shouldDeleteCategoryWithMatchingId() throws Exception {
        Category category = new Category("ToDelete");
        category = categoryRepository.save(category);

        mockMvc.perform(delete("/api/v1/categories/{id}", category.getId()))
            .andExpect(status().isOk());

        assertFalse(categoryRepository.findById(category.getId()).isPresent());
    }
}
