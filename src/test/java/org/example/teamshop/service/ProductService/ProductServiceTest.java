package org.example.teamshop.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import org.example.teamshop.dto.ProductDto;
import org.example.teamshop.mapper.ProductMapper;
import org.example.teamshop.model.Category;
import org.example.teamshop.model.Product;
import org.example.teamshop.repository.ProductRepository;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;
import org.example.teamshop.service.Category.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @Test
    public void testGetProductById_Successfully_AndReturnProduct() {
        Long productId = 1L;
        Category electronicsCategory = new Category(1, "Electronics");
        Product smartphone = new Product(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        when(productRepository.findById(productId)).thenReturn(Optional.of(smartphone));

        Product product = productService.getProductById(productId);
        assertNotNull(product);
        assertEquals(product.getId(), smartphone.getId());
        assertEquals(product.getName(), smartphone.getName());
        assertEquals(product.getPrice(), smartphone.getPrice());
        assertEquals(product.getCategory(), smartphone.getCategory());
        assertEquals(product.getImages(), smartphone.getImages());

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetProductById_ProductIdIsNull_ShouldThrowException() {
        Long productId = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(productId);
        });

        assertEquals("Product Id cannot be null", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    public void testGetProductById_ItDoesntExist_ShouldThrowException() {
        Long productId = 2L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.getProductById(productId);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testGetProductDtoById_Successfully_ShouldReturnProductDto() {
        Long productId = 1L;
        Category electronicsCategory = new Category(1, "Electronics");
        Product smartphone = new Product(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());
        ProductDto smartphoneDto = new ProductDto(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        when(productRepository.findById(productId)).thenReturn(Optional.of(smartphone));
        when(productMapper.toProductDto(smartphone)).thenReturn(smartphoneDto);

        ProductDto productDto = productService.getProductDtoById(productId);

        assertNotNull(productDto);
        assertEquals(1L, productDto.getId());
        assertEquals("Smartphone", productDto.getName());
        assertEquals(new BigDecimal(1000), productDto.getPrice());
        assertEquals(electronicsCategory.getId(), productDto.getCategory().getId());
        assertEquals(electronicsCategory.getName(), productDto.getCategory().getName());
        assertEquals(Set.of(), productDto.getImages());
        verify(productRepository, times(1)).findById(anyLong());
        verify(productMapper, times(1)).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductDtoById_ProductIdIsNull_ShouldThrowException() {
        Long productId = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductDtoById(productId);
        });

        assertEquals("Product Id cannot be null", exception.getMessage());
        verify(productRepository, never()).findById(anyLong());
        verify(productMapper, never()).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductDtoById_ItDoesntExist_ShouldThrowException() {
        Long productId = 1L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.getProductDtoById(productId);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(anyLong());
        verify(productMapper, never()).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductDtoList_Successfully_ShouldReturnListOfProductDto() {
        Category electronicsCategory = new Category(1, "Electronics");

        Product smartphone = new Product(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());
        ProductDto smartphoneDto = new ProductDto(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        Product laptop = new Product(2L, "Laptop", new BigDecimal(2000), electronicsCategory, Set.of());
        ProductDto laptopDto = new ProductDto(2L, "Laptop", new BigDecimal(2000), electronicsCategory, Set.of());


        List<Product> listOfProduct = new ArrayList<>();
        listOfProduct.add(smartphone);
        listOfProduct.add(laptop);

        List<ProductDto> listOfProductDto = new ArrayList<>();
        listOfProductDto.add(smartphoneDto);
        listOfProductDto.add(laptopDto);

        when(productRepository.findAll()).thenReturn(listOfProduct);
        when(productMapper.toProductDto(smartphone)).thenReturn(smartphoneDto);
        when(productMapper.toProductDto(laptop)).thenReturn(laptopDto);

        List<ProductDto> products = productService.getProductDtoList();


        assertNotNull(products);

        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i), listOfProductDto.get(i));
        }

        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(listOfProductDto.size())).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductDtoList_ListIsEmpty_ShouldReturnListOfProductDto() {
        List<Product> productList = new ArrayList<>();

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDto> products = productService.getProductDtoList();

        assertNotNull(products);
        assertEquals(0, products.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, never()).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductsByName_Successfully_ShouldReturnListOfProductDto() {
        String name = "smartphone";
        Category electronicsCategory = new Category(1, "Electronics");

        Product smartphone = new Product(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());
        ProductDto smartphoneDto = new ProductDto(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        Product otherSmartphone = new Product(2L, "smartphone", new BigDecimal(2000), electronicsCategory, Set.of());
        ProductDto otherSmartphoneDto = new ProductDto(2L, "smartphone", new BigDecimal(2000), electronicsCategory, Set.of());


        List<Product> listOfProduct = new ArrayList<>();
        listOfProduct.add(smartphone);
        listOfProduct.add(otherSmartphone);

        List<ProductDto> listOfProductDto = new ArrayList<>();
        listOfProductDto.add(smartphoneDto);
        listOfProductDto.add(otherSmartphoneDto);

        when(productRepository.findAllByNameIgnoreCase(name)).thenReturn(listOfProduct);
        when(productMapper.toProductDto(smartphone)).thenReturn(smartphoneDto);
        when(productMapper.toProductDto(otherSmartphone)).thenReturn(otherSmartphoneDto);

        List<ProductDto> products = productService.getProductsByName(name);

        assertNotNull(products);

        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i), listOfProductDto.get(i));
        }

        verify(productRepository, times(1)).findAllByNameIgnoreCase(anyString());
        verify(productMapper, times(listOfProductDto.size())).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductsByName_ListIsEmpty_ShouldReturnListOfProductDto() {
        String name = "smartphone";

        List<Product> productList = new ArrayList<>();

        when(productRepository.findAllByNameIgnoreCase(name)).thenReturn(productList);

        List<ProductDto> products = productService.getProductsByName(name);

        assertNotNull(products);
        assertEquals(0, products.size());
        verify(productRepository, times(1)).findAllByNameIgnoreCase(anyString());
        verify(productMapper, never()).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductsByCategory_Successfully_ShouldReturnListOfProductDto() {
        String categoryName = "Electronics";

        Category electronicsCategory = new Category(1, "Electronics");

        Product smartphone = new Product(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());
        ProductDto smartphoneDto = new ProductDto(1L, "Smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        Product otherSmartphone = new Product(2L, "smartphone", new BigDecimal(2000), electronicsCategory, Set.of());
        ProductDto otherSmartphoneDto = new ProductDto(2L, "smartphone", new BigDecimal(2000), electronicsCategory, Set.of());

        List<Product> productList = new ArrayList<>();
        productList.add(smartphone);
        productList.add(otherSmartphone);

        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(smartphoneDto);
        productDtoList.add(otherSmartphoneDto);

        when(productRepository.findAllByCategory_NameIgnoreCase(categoryName)).thenReturn(productList);
        when(productMapper.toProductDto(smartphone)).thenReturn(smartphoneDto);
        when(productMapper.toProductDto(otherSmartphone)).thenReturn(otherSmartphoneDto);

        List<ProductDto> products = productService.getProductsByCategory(categoryName);

        assertNotNull(products);
        for (int i = 0; i < products.size(); i++) {
            assertEquals(productDtoList.get(i), products.get(i));
        }
        verify(productRepository, times(1)).findAllByCategory_NameIgnoreCase(anyString());
        verify(productMapper, times(2)).toProductDto(any(Product.class));
    }

    @Test
    public void testGetProductsByCategory_ListIsEmpty_ShouldReturnListOfProductDto() {
        String categoryName = "electronics";

        List<Product> productList = new ArrayList<>();

        when(productRepository.findAllByCategory_NameIgnoreCase(categoryName)).thenReturn(productList);

        List<ProductDto> products = productService.getProductsByCategory(categoryName);

        assertNotNull(products);
        assertEquals(0, products.size());
        verify(productRepository, times(1)).findAllByCategory_NameIgnoreCase(anyString());
        verify(productMapper, never()).toProductDto(any(Product.class));
    }

    @Test
    public void testCreateProduct_Successfully_ShouldReturnProductDto() {
        Category electronicsCategory = new Category(1, "Electronics");

        CreateProductRequest createProductRequest = new CreateProductRequest("smartphone", new BigDecimal(1000), electronicsCategory);
        Product product = new Product(1L, "smartphone", new BigDecimal(1000), electronicsCategory, Set.of());
        ProductDto productDto = new ProductDto(1L, "smartphone", new BigDecimal(1000), electronicsCategory, Set.of());

        when(productMapper.fromCreateProductRequestToProduct(createProductRequest)).thenReturn(product);
        when(categoryService.returnNewCategoryIfNotExists(product.getCategory())).thenReturn(electronicsCategory);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDto(product)).thenReturn(productDto);

        ProductDto newProductDto = productService.createProduct(createProductRequest);

        assertNotNull(newProductDto);
        assertEquals(productDto.getId(), newProductDto.getId());
        assertEquals(productDto.getName(), newProductDto.getName());
        assertEquals(productDto.getPrice(), newProductDto.getPrice());
        assertEquals(productDto.getCategory(), newProductDto.getCategory());
        assertEquals(productDto.getImages(), newProductDto.getImages());
    }

    @Test
    void testCreateProduct_ShouldSetCategoryFromService() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCategory(new Category(1, "Books"));

        Product initialProduct = new Product();
        initialProduct.setCategory(new Category(1, "Placeholder"));

        Category realCategory = new Category(1, "Books");

        when(productMapper.fromCreateProductRequestToProduct(request)).thenReturn(initialProduct);
        when(categoryService.returnNewCategoryIfNotExists(initialProduct.getCategory())).thenReturn(realCategory);
        when(productMapper.toProductDto(any())).thenReturn(new ProductDto());

        productService.createProduct(request);

        assertEquals("Books", initialProduct.getCategory().getName());
    }

    @Test
    void testUpdateProduct_ShouldReturnUpdatedDto() {
        Long id = 1L;
        UpdateProductRequest request = new UpdateProductRequest();
        request.setCategory(new Category(1, "New Category"));

        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old Name");
        existingProduct.setCategory(new Category(2, "Old Category"));

        Category newCategory = new Category(1, "New Category");

        ProductDto expectedDto = new ProductDto();
        expectedDto.setName("Updated Name");
        expectedDto.setCategory(new Category(1, "New Category"));

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(categoryService.returnNewCategoryIfNotExists(new Category(1, "New Category"))).thenReturn(newCategory);
        when(productMapper.toProductDto(existingProduct)).thenReturn(expectedDto);

        ProductDto result = productService.updateProduct(id, request);

        verify(productRepository).findById(anyLong());
        verify(categoryService).returnNewCategoryIfNotExists(any(Category.class));
        verify(productMapper).fromUpdateProductRequestToProduct(request, existingProduct);
        verify(productMapper).toProductDto(existingProduct);

        assertEquals(expectedDto, result);
    }

    @Test
    void testUpdateProduct_WhenProductNotFound_ShouldThrowException() {
        Long id = 1L;
        UpdateProductRequest request = new UpdateProductRequest();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(id, request);
        });
    }

    @Test
    void testDeleteProduct_ShouldDeleteSuccessfully() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Test Product");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.deleteProduct(id);

        verify(productRepository).findById(id);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_WhenProductNotFound_ShouldThrowException() {

        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });

        verify(productRepository, never()).delete(any());
    }

}
