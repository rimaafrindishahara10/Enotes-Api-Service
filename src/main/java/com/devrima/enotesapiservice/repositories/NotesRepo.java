package com.devrima.enotesapiservice.repositories;

import com.devrima.enotesapiservice.models.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepo extends JpaRepository<Notes,Integer> {
    Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);
}
