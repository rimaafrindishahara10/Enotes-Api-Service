package com.devrima.enotesapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveCategory {

    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;


}
