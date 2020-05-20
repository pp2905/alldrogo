package com.application.alledrogo.Service;

import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Model.SubCategory;
import com.application.alledrogo.Repository.SubCategoryRepository;
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

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class SubCategoryServiceTest {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @InjectMocks
    private SubCategoryService subCategoryService;

    private SubCategory subCategory;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("name");
        category.setDescription("desc");

        subCategory = new SubCategory();
        subCategory.setName("name");
        subCategory.setCategory(category);
    }

    @Test
    void shouldGetAllSubCategories() {
        List<SubCategory> subCategoryList = new ArrayList<>();
        subCategoryList.add(subCategory);

        given(subCategoryRepository.findAll()).willReturn(subCategoryList);

        List<SubCategory> foundedSubCategories = subCategoryRepository.findAll();

        assertThat(foundedSubCategories).isNotEmpty();
        assertThat(foundedSubCategories).hasSize(1);
        assertThat(foundedSubCategories.get(0)).isEqualToComparingFieldByField(subCategory);
    }

    @Test
    void shouldGetSubCategoryById() {
        given(subCategoryRepository.findById(category.getId())).willReturn(Optional.ofNullable(subCategory));

        SubCategory found = subCategoryService.getSubCategoryById(category.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualToComparingFieldByField(subCategory);
    }

    @Test
    void shouldAddSubCategory() {
        given(subCategoryRepository.findById(subCategory.getId())).willReturn(Optional.empty());
        given(subCategoryRepository.save(subCategory)).willReturn(subCategory);

        SubCategory addedSubCategory = subCategoryService.addSubCategory(subCategory);

        assertThat(addedSubCategory).isNotNull();
        assertThat(addedSubCategory).isEqualToComparingFieldByField(subCategory);
    }

    @Test
    void updateSubCategoryById() {
    }

    @Test
    void deleteSubCategoryById() {
    }
}