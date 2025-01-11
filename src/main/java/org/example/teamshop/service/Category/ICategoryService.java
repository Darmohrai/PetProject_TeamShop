package org.example.teamshop.service.Category;

import org.example.teamshop.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    Category getCategoryByName(String name);
    Category createCategory(Category category);

    boolean existsCategoryByName(String name);

    Category returnNewCategoryIfNotExists(Category category);
}
