package com.application.alledrogo.Service;

import com.application.alledrogo.Exception.NotAcceptableException;
import com.application.alledrogo.Exception.NotFoundException;
import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        List<Category> getCategories = categoryRepository.findAll();
        if(getCategories.isEmpty()) {
            throw new NotFoundException("Not found any Category");
        }

        return getCategories;
    }

    public Category getCategoryById(int id) {
        Optional<Category> getCategory = categoryRepository.findById(id);
        return getCategory.orElseThrow(() -> new NotFoundException(String.format("Not found Category with id %s", id)));
    }

    public Category addCategory(Category category) {
        if(category.getName() == null) {
            throw new NotAcceptableException("Name should not be empty");
        }

        return categoryRepository.save(category);
    }

    public Category updateCategoryById(Category category) {
        //getCategoryById check if the Category exist in the database, if not throw NotFoundException (404 not found)
        Category getCategory = getCategoryById(category.getId());

        if(category.getName() == null) {
            category.setName(getCategory.getName());
        }

        if(category.getDescription() == null) {
            category.setDescription(getCategory.getDescription());
        }

        return categoryRepository.save(category);
    }

    public void deleteCategoryById(int id) {
        //getCategoryById check if the Category exist in the database, if not throw NotFoundException (404 not found)
        Category getCategory = getCategoryById(id);
        categoryRepository.delete(getCategory);
    }
}