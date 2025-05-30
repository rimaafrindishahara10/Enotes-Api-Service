package com.devrima.enotesapiservice.controllers;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.dto.CategoryDto;
import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.services.impl.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    //Post-Mapping
    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
        Boolean saveCategory = categoryService.saveCategory ( categoryDto );
        if(saveCategory){
            return new ResponseEntity<> ( "Category Created Successfully", HttpStatus.CREATED );
        }else {
            return new ResponseEntity<> ( "Category Not Created Successfully", HttpStatus.INTERNAL_SERVER_ERROR );

        }
    }

    //Get-All-Category
    @GetMapping()
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory ();
        if(CollectionUtils.isEmpty ( allCategory )){
           return ResponseEntity.noContent ().build ();
        }else {
            return new ResponseEntity<> ( allCategory,HttpStatus.OK );
        }

    }
    //GetAll-Active-Category
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveCategory(){
        List<ActiveCategory> allActiveCategory = categoryService.getALlActiveCategory ();
        if(CollectionUtils.isEmpty ( allActiveCategory )){
            return ResponseEntity.noContent ().build ();
        }else {
            return new ResponseEntity<> ( allActiveCategory,HttpStatus.OK );
        }

    }

    //GetById-Category
    @GetMapping("/{id}")
    public ResponseEntity<?> getByCategoryId(@PathVariable Integer id){

         CategoryDto categoryDto = categoryService.getByCategoryId(id);
         if (ObjectUtils.isEmpty ( categoryDto )){
             return new ResponseEntity<> ( "Category not found by id : "+id,HttpStatus.NOT_FOUND  );
         }
         return new ResponseEntity<> ( categoryDto,HttpStatus.OK );


    }

    //Delete-Category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        Boolean deleted= categoryService.deleteCategoryById(id);
        if(deleted){
            return new ResponseEntity<> ( "Category has been deleted ",HttpStatus.OK );
        }
        return new ResponseEntity<> ( "Category could not delete by id : "+id,HttpStatus.INTERNAL_SERVER_ERROR  );

    }



}
