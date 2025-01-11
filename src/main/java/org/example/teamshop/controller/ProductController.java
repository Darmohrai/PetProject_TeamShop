package org.example.teamshop.controller;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.dto.ProductDto;
import org.example.teamshop.model.Product;
import org.example.teamshop.request.CreateProductRequest;
import org.example.teamshop.request.UpdateProductRequest;
import org.example.teamshop.service.ProductService.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts() {
        return productService.getProductDtoList();
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getProductDtoById(id);
    }

    @GetMapping("/filter/name")
    public List<ProductDto> getProductsByName(@RequestParam("name") String name) {
        return productService.getProductsByName(name);
    }

    @GetMapping("/filter/category")
    public List<ProductDto> getProductsByCategory(@RequestParam("category") String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/filter/name-category")
    public List<ProductDto> getProductsByNameCategory(@RequestParam("name") String name,
                                                      @RequestParam("category") String category) {
        return productService.getProductsByNameAndCategory(name, category);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody CreateProductRequest product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                    @RequestBody UpdateProductRequest product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
