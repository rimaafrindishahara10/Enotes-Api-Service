package com.devrima.enotesapiservice.repositories;

import com.devrima.enotesapiservice.models.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailsRepo extends JpaRepository<FileDetails,Integer> {
}
