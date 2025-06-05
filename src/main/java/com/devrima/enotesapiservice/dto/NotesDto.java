package com.devrima.enotesapiservice.dto;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotesDto {
    private Integer id;
    private String title;
    private String description;
    private CategoryDto category;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto{
        private Integer id;
        private String name;

    }

}

