package org.example.teamshop.mapper;

import org.example.teamshop.dto.ProductDto;
import org.example.teamshop.model.Product;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProductFromCreateProductRequest(CreateProductRequest updateProductRequest);

    ProductDto toProductDto(Product product);

    Product fromCreateProductRequestToProduct(CreateProductRequest request);

    void fromUpdateProductRequestToProduct(UpdateProductRequest product,@MappingTarget Product oldProduct);
}
