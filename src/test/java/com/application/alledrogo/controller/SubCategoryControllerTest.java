package com.application.alledrogo.controller;

import com.application.alledrogo.model.Category;
import com.application.alledrogo.model.SubCategory;
import com.application.alledrogo.service.SubCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubCategoryController.class)
class SubCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SubCategoryService subCategoryService;

    private SubCategory subCategory;
    private List<SubCategory> subCategoryList;

    @BeforeEach
    void setUp() {
        subCategory = new SubCategory();
        subCategory.setName("Name");
        subCategory.setDescription("Desc");

        Category category = new Category(1, "Name", "Desc");
        subCategory.setCategory(category);

        subCategoryList = Arrays.asList(subCategory);
    }

    @Test
    void shouldGetAllSubCategories() throws Exception {
        given(subCategoryService.getAllSubCategories()).willReturn(subCategoryList);

        mvc.perform(get("/api/subcategories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(subCategory.getName())));
    }

    @Test
    void shouldGetSubCategoryById() throws Exception {
        int id = subCategory.getId();
        given(subCategoryService.getSubCategoryById(id)).willReturn(subCategory);

        mvc.perform(get("/api/subcategories/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(subCategory.getName())))
                .andExpect(jsonPath("description", is(subCategory.getDescription())))
                .andExpect(jsonPath("category.id", is(subCategory.getCategory().getId())));
    }

    @Test
    void shouldAddNewSubCategory() throws Exception {
        given(subCategoryService.addSubCategory(subCategory)).willReturn(subCategory);

        mvc.perform(post("/api/subcategories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"name\": \"Name\", \"description\": \"Desc\", \"categoryId\": 1 }")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(subCategory.getName())))
                .andExpect(jsonPath("description", is(subCategory.getDescription())))
                .andExpect(jsonPath("category.id", is(subCategory.getCategory().getId())));
    }

    @Test
    void shouldUpdateSubCategoryById() throws Exception {
        given(subCategoryService.updateSubCategory(subCategory)).willReturn(subCategory);

        mvc.perform(put("/api/subcategories/{id}", subCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{ \"name\": \"Name\", \"description\": \"Desc\", \"categoryId\": 1 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(subCategory.getName())))
                .andExpect(jsonPath("description", is(subCategory.getName())))
                .andExpect(jsonPath("category.id", is(subCategory.getCategory().getId())));
    }

    @Test
    void shouldDeleteSubCategoryById() throws Exception {
        mvc.perform(delete("/api/subcategories/{id}", subCategory.getId()))
                .andExpect(status().isOk());
    }
}