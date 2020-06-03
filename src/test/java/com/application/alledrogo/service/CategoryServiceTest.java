package com.application.alledrogo.service;

import com.application.alledrogo.model.Category;
import com.application.alledrogo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("name");
        category.setDescription("desc");
    }

    @Test
    void shouldGetAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);

        given(categoryRepository.findAll()).willReturn(categoryList);

        List<Category> foundCategories = categoryService.getAllCategories();

        assertThat(foundCategories).isNotEmpty();
        assertThat(foundCategories).hasSize(1);
        assertThat(foundCategories.get(0)).isEqualToComparingFieldByField(category);
    }

    @Test
    void shouldGetCategoryById() {
        given(categoryRepository.findById(category.getId())).willReturn(Optional.ofNullable(category));

        Category found = categoryService.getCategoryById(category.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(category.getId());
    }

    @Test
    void shouldAddCategory() {
        given(categoryRepository.findById(category.getId())).willReturn(Optional.empty());
        given(categoryRepository.save(category)).willReturn(category);

        Category addedCategory = categoryService.addCategory(category);

        assertThat(addedCategory).isNotNull();
        assertThat(addedCategory).isEqualToComparingFieldByField(category);
    }

    @Test
    void shouldUpdateCategory() {
        int id = category.getId();

        given(categoryRepository.findById(id)).willReturn(Optional.ofNullable(category));
        given(categoryRepository.save(category)).willReturn(category);

        category.setName("Testowy");
        Category expected = categoryService.updateCategory(category);

        assertThat(expected).isNotNull();
        assertThat(expected).isEqualToComparingFieldByField(category);
    }

    @Test
    void shouldDeleteCategoryById() {
        int id = category.getId();

        given(categoryRepository.findById(id)).willReturn(Optional.ofNullable(category));
        categoryService.deleteCategoryById(id);

        verify(categoryRepository).delete(category);
    }
}