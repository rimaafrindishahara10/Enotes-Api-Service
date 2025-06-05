package com.devrima.enotesapiservice.controllers;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.dto.CategoryDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.Category;
import com.devrima.enotesapiservice.services.impl.CategoryServiceImpl;
import com.devrima.enotesapiservice.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.Controller;

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
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws Exception {
        Boolean saveCategory = categoryService.saveCategory ( categoryDto );
        if(saveCategory){
            return CommonUtil.createBuildResponseMessage ( "Category Created Successfully",HttpStatus.CREATED );
//            return new ResponseEntity<> ( "Category Created Successfully", HttpStatus.CREATED );
        }else {
            return CommonUtil.createErrorResponseMessage ( "Category Not Created Successfully",HttpStatus.INTERNAL_SERVER_ERROR );
//            return new ResponseEntity<> ( "Category Not Created Successfully", HttpStatus.INTERNAL_SERVER_ERROR );

        }
    }

    //Get-All-Category
    @GetMapping()
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory ();
        if(CollectionUtils.isEmpty ( allCategory )){
           return ResponseEntity.noContent ().build ();
        }else {
            return CommonUtil.createBuildResponse ( "Get All Categories",allCategory,HttpStatus.OK );
        }

    }
    //GetAll-Active-Category
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveCategory(){
        List<ActiveCategory> allActiveCategory = categoryService.getALlActiveCategory ();
        if(CollectionUtils.isEmpty ( allActiveCategory )){
            return ResponseEntity.noContent ().build ();
        }else {
            return CommonUtil.createBuildResponse ("get all active categories", allActiveCategory,HttpStatus.OK );
        }

    }

    //GetById-Category
    @GetMapping("/{id}")
    public ResponseEntity<?> getByCategoryId(@PathVariable Integer id) throws ResourceNotFoundException {

         CategoryDto categoryDto = categoryService.getByCategoryId(id);
         if (ObjectUtils.isEmpty ( categoryDto )){
             return CommonUtil.createErrorResponseMessage ( "NOT Found",HttpStatus.NOT_FOUND );
//             return new ResponseEntity<> ( "NOT Found: "+id,HttpStatus.NOT_FOUND  );
         }

         return  CommonUtil.createBuildResponse ("Get the category", categoryDto,HttpStatus.OK );
//         return new ResponseEntity<> ( categoryDto,HttpStatus.OK );


    }

    //Delete-Category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        Boolean deleted= categoryService.deleteCategoryById(id);
        if(deleted){
              return CommonUtil.createBuildResponseMessage ( "Category has been deleted", HttpStatus.OK);
//            return new ResponseEntity<> ( "Category has been deleted ",HttpStatus.OK );
        }


        return CommonUtil.createErrorResponseMessage ( "Category could not deleted", HttpStatus.INTERNAL_SERVER_ERROR );
//        return new ResponseEntity<> ( "Category could not delete by id : "+id,HttpStatus.INTERNAL_SERVER_ERROR  );

    }



}
