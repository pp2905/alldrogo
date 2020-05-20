package com.application.alledrogo.Service;

import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


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
    void updateCategoryById() {
    }

    @Test
    void deleteCategoryById() {
    }
}