package org.example.teamshop.service.ProductService;

import org.example.teamshop.dto.ProductDto;
import org.example.teamshop.model.Product;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);
    ProductDto getProductDtoById(Long id);
    List<ProductDto> getProductDtoList();
    List<ProductDto> getProductsByName(String name);
    List<ProductDto> getProductsByCategory(String category);
    List<ProductDto> getProductsByNameAndCategory(String name, String category);

    ProductDto createProduct(CreateProductRequest product);
    ProductDto updateProduct(Long id, UpdateProductRequest product);

    void deleteProduct(Long id);
}
