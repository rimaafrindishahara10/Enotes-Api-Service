package com.devrima.enotesapiservice.services;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.dto.CategoryDto;
import com.devrima.enotesapiservice.models.Category;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto) throws Exception;
    public List<CategoryDto> getAllCategory();
    public List<ActiveCategory> getALlActiveCategory();

    CategoryDto getByCategoryId(Integer id) throws Exception;

    Boolean deleteCategoryById(Integer id);
}
