package com.devrima.enotesapiservice.services.impl;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.dto.CategoryDto;
import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.repositories.CategoryRepo;
import com.devrima.enotesapiservice.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper mapper) {
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {
//        category.setIsDeleted ( false );
//        category.setCreatedBy ( 1 );
//        category.setCreatedOn ( new Date ());

//        Category category = new Category ();
//        category.setName ( categoryDto.getName () );
//        category.setDescription ( categoryDto.getDescription () );
//        category.setIsActive ( categoryDto.getIsActive () );
//

        Category category = mapper.map ( categoryDto, Category.class );

        Category saveCategory = categoryRepo.save ( category );
        if(ObjectUtils.isEmpty ( saveCategory )){
            return false;
        }
        return true;
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> allCategory = categoryRepo.findAll ();
        List<CategoryDto> categories = allCategory.stream ().map ( category -> mapper.map ( category ,CategoryDto.class ) ).toList ();
        return categories;
    }

    @Override
    public List<ActiveCategory> getALlActiveCategory() {
        List<Category> allByActiveCategory = categoryRepo.findByIsActiveTrue ();
        List<ActiveCategory> activeCategories = allByActiveCategory.stream ().map ( cat->mapper.map ( cat,ActiveCategory.class ) ).toList ();

        return activeCategories;
    }
}
