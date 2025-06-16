package org.example.teamshop.controller;

import org.example.teamshop.model.Category;
import org.example.teamshop.model.Image;
import org.example.teamshop.model.Product;
import org.example.teamshop.repository.CategoryRepository;
import org.example.teamshop.repository.ProductRepository;
import org.example.teamshop.service.ProductService.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Value("${api.path}")
    private String apiPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final List<Product> products = new ArrayList<>();

    private Product phoneProduct;

    @BeforeEach
    void setUp() {
        Category electronicsCategory = new Category();
        electronicsCategory.setName("Electronics");
        categoryRepository.save(electronicsCategory);

        electronicsCategory = categoryRepository.findByName("Electronics").get();
        phoneProduct =
                new Product(null, "Phone", new BigDecimal(1000), electronicsCategory, Set.of());

        phoneProduct = productRepository.save(phoneProduct);
        products.add(phoneProduct);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testGetProducts_ProductsExist() throws Exception {
        mockMvc.perform(get(apiPath + "/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(phoneProduct.getId()))
                .andExpect(jsonPath("$[0].name").value("Phone"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[0].category.id").value(phoneProduct.getCategory().getId()));
    }

    @Test
    public void testGetProducts_ProductsDoNotExist() throws Exception {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        mockMvc.perform(get(apiPath + "/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetProduct_ProductExist() throws Exception {
        mockMvc.perform(get(apiPath + "/product/{id}", phoneProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$.id").value(phoneProduct.getId()))
                .andExpect(jsonPath("$.category.id").value(phoneProduct.getCategory().getId()))
                .andExpect(jsonPath("$.category.name").value(phoneProduct.getCategory().getName()))
                .andExpect(jsonPath("$.price").value(phoneProduct.getPrice().intValue()));
    }

    @Test
    public void testGetProduct_ProductDoesNotExist() throws Exception {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        mockMvc.perform(get(apiPath + "/product/{id}", phoneProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProduct_IllegalArgument_ShouldThrowException() throws Exception {
        mockMvc.perform(get(apiPath + "/product/{id}", ""))
                .andExpect(status().isBadRequest());
    }
}
