package com.devrima.enotesapiservice.repositories;

import com.devrima.enotesapiservice.dto.ActiveCategory;
import com.devrima.enotesapiservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    public List<Category> findByIsActiveTrue();
}
