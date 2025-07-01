package com.devrima.enotesapiservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EntityListeners (AuditingEntityListener.class)
public class Notes extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    @ManyToOne
    private Category category;
    @ManyToOne
    private FileDetails fileDetails;
    private Boolean isDeleted;
    private Date deleteOn;


}
