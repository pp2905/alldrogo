package com.application.alledrogo.Service;

import com.application.alledrogo.Exception.NotAcceptableException;
import com.application.alledrogo.Exception.NotFoundException;
import com.application.alledrogo.Model.Category;
import com.application.alledrogo.Model.SubCategory;
import com.application.alledrogo.Repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<SubCategory> getAllSubCategories() {
        List<SubCategory> getSubCategories = subCategoryRepository.findAll();
        if(getSubCategories.isEmpty()) {
            throw new NotFoundException("Not found any SubCategory");
        }

        return getSubCategories;
    }
    
    public SubCategory getSubCategoryById(int id) {
        Optional<SubCategory> getSubCategory = subCategoryRepository.findById(id);
        return getSubCategory.orElseThrow(() -> new NotFoundException(String.format("Not found SubCategory with id %s", id)));
    }
    
    public SubCategory addSubCategory(SubCategory subCategory) {
        if(subCategory.getName() == null || subCategory.getCategory() != null) {
            throw new NotAcceptableException("Name and CategoryId should not be empty");
        }

        return subCategoryRepository.save(subCategory);
    }
    
    public SubCategory updateSubCategoryById(SubCategory subCategory) {
        //getSubCategoryById check if the subCategory exist in the database, if not throw NotFoundException (404 not found)
        SubCategory getSubCategory = getSubCategoryById(subCategory.getId());

        if(subCategory.getCategory() == null) {
            subCategory.setCategory(getSubCategory.getCategory());
        }

        if(subCategory.getName() == null) {
            subCategory.setName(getSubCategory.getName());
        }

        if(subCategory.getDescription() == null) {
            subCategory.setDescription(getSubCategory.getDescription());
        }

        return subCategoryRepository.save(subCategory);
    }
    
    public void deleteSubCategoryById(int id) {
        //getSubCategoryById check if the subCategory exist in the database, if not throw NotFoundException (404 not found)
        SubCategory getSubCategory = getSubCategoryById(id);
        subCategoryRepository.deleteById(id);
    }
}
