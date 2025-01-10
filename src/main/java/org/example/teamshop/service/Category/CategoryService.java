package org.example.teamshop.service.Category;

import lombok.RequiredArgsConstructor;
import org.example.teamshop.Exception.AlreadyExistingResourceException;
import org.example.teamshop.Exception.ResourceNotFoundException;
import org.example.teamshop.model.Category;
import org.example.teamshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).
                orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistingResourceException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public boolean existsCategoryByName(String name){
        return categoryRepository.existsByName(name);
    }

    @Override
    public Category returnNewCategoryIfNotExists(Category category){
        Category requiredCategory;
        try{
            requiredCategory = getCategoryByName(category.getName());
        }catch (ResourceNotFoundException e){
            requiredCategory = new Category();
            requiredCategory.setName(category.getName());
            categoryRepository.save(requiredCategory);
        }
        return requiredCategory;
    }
}
