package com.devrima.enotesapiservice.services;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NotesService {

    //Save-method
    Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, IOException;

    //Get-All-Notes
    List<NotesDto> getAllNotes();

}
