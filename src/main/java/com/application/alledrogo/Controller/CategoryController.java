package com.application.alledrogo.Controller;

import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Service.CategoryService;
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
        return categoryService.updateCategoryById(category);
    }

    @DeleteMapping(
            path = "{categoryId}"
    )
    public void deleteCategoryById(@PathVariable("categoryId") int id) {
        categoryService.deleteCategoryById(id);
    }
}
