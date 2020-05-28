package com.application.alledrogo.controller;

import com.application.alledrogo.model.Category;
import com.application.alledrogo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{categoryId}"
    )
    public Category getCategoryById(@PathVariable("categoryId") int id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Category addNewCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{categoryId}"
    )
    public Category updateCategoryById(@PathVariable("categoryId") int categoryId, @RequestBody Category category) {
        category.setId(categoryId);
        return categoryService.updateCategory(category);
    }

    @DeleteMapping(
            path = "{categoryId}"
    )
    public void deleteCategoryById(@PathVariable("categoryId") int id) {
        categoryService.deleteCategoryById(id);
    }
}
