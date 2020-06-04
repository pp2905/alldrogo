package com.application.alledrogo.controller;

import com.application.alledrogo.model.SubCategory;
import com.application.alledrogo.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {
    
    private final SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<SubCategory> getAllSubCategories() {
        return subCategoryService.getAllSubCategories();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{subCategoryId}"
    )
    public SubCategory getSubCategoryById(@PathVariable("subCategoryId") int id) {
        return subCategoryService.getSubCategoryById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public SubCategory addNewSubCategory(@RequestBody SubCategory subCategory) {
        // use objectNode https://stackoverflow.com/questions/12893566/passing-multiple-variables-in-requestbody-to-a-spring-mvc-controller-using-ajax
        return subCategoryService.addSubCategory(subCategory);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{subCategoryId}"
    )
    public SubCategory updateSubCategoryById(@PathVariable("subCategoryId") int id, @RequestBody SubCategory subCategory) {
        subCategory.setId(id);
        return subCategoryService.updateSubCategory(subCategory);
    }

    @DeleteMapping(
            path = "{subCategoryId}"
    )
    public void deleteSubCategoryById(@PathVariable("subCategoryId") int id) {
        subCategoryService.deleteSubCategoryById(id);
    }
}
