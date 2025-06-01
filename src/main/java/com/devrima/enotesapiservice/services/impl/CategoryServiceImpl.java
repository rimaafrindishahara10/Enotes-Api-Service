package com.devrima.enotesapiservice.services.impl;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.dto.CategoryDto;
import com.devrima.enotesapiservice.exception.ExistNameException;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.repositories.CategoryRepo;
import com.devrima.enotesapiservice.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper mapper) {
        this.categoryRepo = categoryRepo;
        this.mapper = mapper;
    }

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) throws Exception {

        Category category = mapper.map ( categoryDto, Category.class );
        if (ObjectUtils.isEmpty ( category.getId () )){
            category.setIsDeleted ( false );
//            category.setCreatedBy ( 1 );
//            category.setCreatedOn ( new Date () );

        }
        else {
            updateCategory(category);
        }
        Boolean categoryCheck= categoryRepo.existsByNameAndIsDeletedFalse (category.getName ().trim ());
        if (categoryCheck){
            throw new ExistNameException ("This Category name is already exist in your software");
        }

        Category saveCategory = categoryRepo.save ( category );
        if(ObjectUtils.isEmpty ( saveCategory )){
            return false;
        }
        return true;
    }

    //Update-Category-Method
    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepo.findById ( category.getId () );
        if (findById.isPresent ()){
            Category existCategory = findById.get ();
            category.setCreatedBy ( existCategory.getCreatedBy () );
            category.setIsDeleted ( existCategory.getIsDeleted () );

//            category.setUpdatedOn ( new Date () );
//            category.setUpdatedBy ( 1 );
        }
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> allCategory = categoryRepo.findAllByIsDeletedFalse ();
        List<CategoryDto> categories = allCategory.stream ().map ( category -> mapper.map ( category ,CategoryDto.class ) ).toList ();
        return categories;
    }

    @Override
    public List<ActiveCategory> getALlActiveCategory() {
        List<Category> allByActiveCategory = categoryRepo.findByIsActiveTrueAndIsDeletedFalse ();
        List<ActiveCategory> activeCategories = allByActiveCategory.stream ().map ( cat->mapper.map ( cat,ActiveCategory.class ) ).toList ();

        return activeCategories;
    }

    @Override
    public CategoryDto getByCategoryId(Integer id) throws ResourceNotFoundException {
        Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found by id: " + id));

        if(!ObjectUtils.isEmpty ( category )){
            if(category.getName ()==null){
                throw new IllegalArgumentException ("Name is null");
            }
            return mapper.map ( category, CategoryDto.class );
        }
           return null;
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> deleted = categoryRepo.findById ( id );

        if(deleted.isPresent ()){
            Category category = deleted.get ();
            category.setIsDeleted ( true );
            categoryRepo.save ( category );
            return true;
        }

        return false;
    }
}
