package org.example.teamshop.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ProductDto;
import org.example.teamshop.mapper.ProductMapper;
import org.example.teamshop.model.Category;
import org.example.teamshop.model.Product;
import org.example.teamshop.repository.ProductRepository;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;
import org.example.teamshop.service.Category.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ICategoryService categoryService;

    @Override
    public Product getProductById(Long id) {
        return productRepository.
                findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public ProductDto getProductDtoById(Long id) {
        var product = getProductById(id);
        return productMapper.toProductDto(product);
    }

    @Override
    public List<ProductDto> getProductDtoList() {
        return productRepository.findAll().stream().
                map(productMapper::toProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByName(String name) {
        return productRepository.findAllByNameIgnoreCase(name).stream().
                map(productMapper::toProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findAllByCategory_NameIgnoreCase(category).stream().
                map(productMapper::toProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByNameAndCategory(String name, String category) {
        return productRepository.findAllByNameIgnoreCaseAndCategory_NameIgnoreCase(name,category).stream().
                map(productMapper::toProductDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto createProduct(CreateProductRequest product) {
        var newProduct = productMapper.fromCreateProductRequestToProduct(product);

        Category category = categoryService.returnNewCategoryIfNotExists(newProduct.getCategory());

        newProduct.setCategory(category);

        productRepository.save(newProduct);
        return productMapper.toProductDto(newProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductRequest product) {
        var oldProduct = getProductById(id);
        Category category = categoryService.returnNewCategoryIfNotExists(product.getCategory());
        oldProduct.setCategory(category);
        productMapper.fromUpdateProductRequestToProduct(product, oldProduct);
        return productMapper.toProductDto(oldProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        var product = getProductById(id);
        productRepository.delete(product);
    }
}
