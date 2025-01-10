package org.example.teamshop.repository;

import org.example.teamshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameIgnoreCase(String name);

    List<Product> findAllByCategory_NameIgnoreCase(String categoryName);

    List<Product> findAllByNameIgnoreCaseAndCategory_NameIgnoreCase(String productName, String categoryName);
}
