package com.devrima.enotesapiservice.services.impl;

import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.repositories.CategoryRepo;
import com.devrima.enotesapiservice.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Boolean saveCategory(Category category) {
        category.setIsDeleted ( false );
        category.setCreatedBy ( 1 );
        Category saveCategory = categoryRepo.save ( category );
        if(ObjectUtils.isEmpty ( saveCategory )){
            return false;
        }
        return true;
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> allCategory = categoryRepo.findAll ();
        return allCategory;
    }
}
