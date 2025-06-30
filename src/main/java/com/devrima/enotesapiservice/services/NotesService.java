package com.devrima.enotesapiservice.services;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.dto.NotesResponse;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.FileDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NotesService {

    //Save-method
    Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, IOException;

    //Get-All-Notes
//    List<NotesDto> getAllNotes();

    FileDetails getFileDetails(Integer id) throws ResourceNotFoundException;

    byte[] getDownloadFile(FileDetails fileDetails) throws IOException;
    //Get-All-Notes-By-User
    NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

    void softDeleteNotes(Integer id) throws ResourceNotFoundException;

    void restoreDeleteNotes(Integer id) throws ResourceNotFoundException;

    List<NotesDto> recycleBinUserById(Integer userId);
}
