package com.devrima.enotesapiservice.controllers;

import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.services.impl.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
        Boolean saveCategory = categoryService.saveCategory ( category );
        if(saveCategory){
            return new ResponseEntity<> ( "Category Created Successfully", HttpStatus.CREATED );
        }else {
            return new ResponseEntity<> ( "Category Not Created Successfully", HttpStatus.INTERNAL_SERVER_ERROR );

        }
    }

    //Get-All-Category
    @GetMapping()
    public ResponseEntity<?> getAllCategory(){
        List<Category> allCategory = categoryService.getAllCategory ();
        if(CollectionUtils.isEmpty ( allCategory )){
           return ResponseEntity.noContent ().build ();
        }else {
            return new ResponseEntity<> ( allCategory,HttpStatus.OK );
        }

    }



}
