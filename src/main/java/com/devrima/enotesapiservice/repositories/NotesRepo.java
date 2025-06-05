package com.devrima.enotesapiservice.repositories;

import com.devrima.enotesapiservice.models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepo extends JpaRepository<Notes,Integer> {
}
