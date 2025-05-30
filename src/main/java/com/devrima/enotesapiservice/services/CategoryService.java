package com.devrima.enotesapiservice.services;

import com.devrima.enotesapiservice.models.Category;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(Category category);
    public List<Category> getAllCategory();
}
