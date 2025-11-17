package org.example.teamshop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.teamshop.model.Category;
import org.example.teamshop.model.Product;
import org.example.teamshop.repository.CategoryRepository;
import org.example.teamshop.repository.ProductRepository;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class ProductControllerIntegrationTest {

    @Value("${api.path}")
    private String apiBasePath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Category testCategory;
    private String productApiPath;

    @BeforeEach
    void setUp() {
        testCategory = categoryRepository.save(new Category(null, "Electronics"));
        productApiPath = apiBasePath + "/product";
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("1. POST " + "${api.path}" + "/product: Successfully create product (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_Admin_Success() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Laptop", new BigDecimal("1200.50"), testCategory);
        String requestJson = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post(productApiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(1200.50)))
                .andExpect(jsonPath("$.category.id", is(testCategory.getId())))
                .andExpect(jsonPath("$.category.name", is(testCategory.getName())))
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        Long newId = jsonNode.get("id").asLong();

        assertTrue(productRepository.findById(newId).isPresent(), "Product should exist in db");
        Product productFromDb = productRepository.findById(newId).get();
        assertEquals("Laptop", productFromDb.getName());
        assertEquals(0, new BigDecimal("1200.50").compareTo(productFromDb.getPrice()));
        assertNotNull(productFromDb.getCategory());
        assertEquals(testCategory.getId(), productFromDb.getCategory().getId());
    }

    @Test
    @DisplayName("2. POST " + "${api.path}" + "/product: Create product - forbidden (не ADMIN)")
    @WithMockUser(roles = "CLIENT")
    public void testCreateProduct_Client_Forbidden() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Forbidden Laptop", new BigDecimal("1200.50"), testCategory);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(productApiPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());

        assertEquals(0, productRepository.count(), "Product should NOT exist in db");
    }

    @Test
    @DisplayName("3. GET " + "${api.path}" + "/product/{id}: Successfully get existed product")
    public void testGetProductById_Exists() throws Exception {
        Product product = productRepository.save(new Product(null, "Test Phone", new BigDecimal("500"), testCategory, null));

        mockMvc.perform(get(productApiPath + "/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Test Phone")))
                .andExpect(jsonPath("$.price", is(500)))
                .andExpect(jsonPath("$.category.id", is(testCategory.getId())));

        assertEquals(1, productRepository.count());
    }

    @Test
    @DisplayName("4. GET "+ "${api.path}" +"/product/{id}: Error 404 when receiving a non-existent product")
    void testGetProductById_NotFound() throws Exception {
        mockMvc.perform(get(productApiPath + "/{id}", 999L)) // Неіснуючий ID
                .andExpect(status().isNotFound());

        assertEquals(0, productRepository.count());
    }

    @Test
    @DisplayName("5. GET "+ "${api.path}" +"/product: Successfully retrieving product list")
    void testGetAllProducts() throws Exception {
        // Arrange
        productRepository.save(new Product(null, "Keyboard", new BigDecimal("75"), testCategory, null));
        productRepository.save(new Product(null, "Mouse", new BigDecimal("25"), testCategory, null));

        mockMvc.perform(get(productApiPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Очікуємо масив з 2 елементів
                .andExpect(jsonPath("$[0].name", is("Keyboard")))
                .andExpect(jsonPath("$[1].name", is("Mouse")));

        assertEquals(2, productRepository.count());
    }

    @Test
    @DisplayName("6. PUT "+ "${api.path}" +"/product/{id}: Successful product update (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void testUpdateProduct_Admin_Success() throws Exception {
        Product originalProduct = productRepository.save(new Product(null, "Old Name", new BigDecimal("10"), testCategory, null));
        UpdateProductRequest updateRequest = new UpdateProductRequest("New Name", new BigDecimal("20"), testCategory);
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put(productApiPath + "/{id}", originalProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(originalProduct.getId().intValue())))
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.price", is(20)));

        Product updatedProduct = productRepository.findById(originalProduct.getId()).orElseThrow();
        assertEquals("New Name", updatedProduct.getName());
        assertEquals(0, new BigDecimal("20.00").compareTo(updatedProduct.getPrice()));
    }

    @Test
    @DisplayName("7. PUT "+ "${api.path}" +"/product/{id}: Product update is prohibited (NOT ADMIN)")
    @WithMockUser(roles = "CLIENT")
    void testUpdateProduct_Client_Forbidden() throws Exception {
        Product originalProduct = productRepository.save(new Product(null, "Old Name", new BigDecimal("10"), testCategory, null));
        UpdateProductRequest updateRequest = new UpdateProductRequest("New Name", new BigDecimal("20"), testCategory);
        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put(productApiPath + "/{id}", originalProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());

        Product productFromDb = productRepository.findById(originalProduct.getId()).orElseThrow();
        assertEquals("Old Name", productFromDb.getName());
        assertEquals(0, new BigDecimal("10.00").compareTo(productFromDb.getPrice()));
    }

    @Test
    @DisplayName("8. DELETE "+ "${api.path}" +"/product/{id}: Product removal successful (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_Admin_Success() throws Exception {
        Product productToDelete = productRepository.save(new Product(null, "ToDelete", new BigDecimal("1"), testCategory, null));
        long initialCount = productRepository.count();
        assertEquals(1, initialCount);

        mockMvc.perform(delete(productApiPath + "/{id}", productToDelete.getId()))
                .andExpect(status().isOk());

        assertFalse(productRepository.findById(productToDelete.getId()).isPresent(), "Продукт не має існувати в БД");
        assertEquals(0, productRepository.count());
    }

    @Test
    @DisplayName("9. DELETE "+ "${api.path}" +"/product/{id}: Product deletion is prohibited (NOT ADMIN)")
    @WithMockUser(roles = "CLIENT")
    void testDeleteProduct_Client_Forbidden() throws Exception {
        Product productToDelete = productRepository.save(new Product(null, "ToDelete", new BigDecimal("1"), testCategory, null));
        long initialCount = productRepository.count();
        assertEquals(1, initialCount);

        mockMvc.perform(delete(productApiPath + "/{id}", productToDelete.getId()))
                .andExpect(status().is4xxClientError());

        assertTrue(productRepository.findById(productToDelete.getId()).isPresent(), "Продукт все ще має існувати в БД");
        assertEquals(1, productRepository.count());
    }

    @Test
    @DisplayName("10. DELETE "+ "${api.path}" +"/product/{id}: Error 404 when deleting a non-existent product (ADMIN)")
    @WithMockUser(roles = "ADMIN")
    void testDeleteProduct_Admin_NotFound() throws Exception {
        mockMvc.perform(delete(productApiPath + "/{id}", 999L)) // Неіснуючий ID
                .andExpect(status().isNotFound());

        assertEquals(0, productRepository.count());
    }
}
