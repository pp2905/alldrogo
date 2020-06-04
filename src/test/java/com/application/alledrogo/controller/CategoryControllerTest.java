package com.application.alledrogo.controller;

import com.application.alledrogo.model.Category;
import com.application.alledrogo.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    private Category category;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Name");
        category.setDescription("Desc");

        categoryList = Arrays.asList(category);
    }

    @Test
    void shouldGetAllCategories() throws Exception {
        given(categoryService.getAllCategories()).willReturn(categoryList);

        mvc.perform(get("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(category.getName())));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        int id = category.getId();
        given(categoryService.getCategoryById(id)).willReturn(category);

        mvc.perform(get("/api/categories/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(category.getName())))
                .andExpect(jsonPath("description", is(category.getDescription())));
    }

    @Test
    void shouldAddNewCategory() throws Exception {
        given(categoryService.addCategory(category)).willReturn(category);

        mvc.perform(post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"name\": \"Name\", \"description\": \"Desc\"}")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(category.getName())))
                .andExpect(jsonPath("description", is(category.getDescription())));
    }

    @Test
    void shouldUpdateCategoryById() throws Exception {
        given(categoryService.updateCategory(category)).willReturn(category);

        mvc.perform(put("/api/categories/{id}", category.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{ \"name\": \"Name\", \"description\": \"Desc\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(category.getName())))
                .andExpect(jsonPath("description", is(category.getDescription())));
    }

    @Test
    void shouldDeleteCategoryById() throws Exception {
        mvc.perform(delete("/api/categories/{id}", category.getId()))
                .andExpect(status().isOk());
    }
}