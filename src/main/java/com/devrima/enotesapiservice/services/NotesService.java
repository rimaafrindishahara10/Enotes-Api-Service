package com.devrima.enotesapiservice.services;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;

import java.util.List;

public interface NotesService {

    //Save-method
    Boolean saveNotes(NotesDto notesDto) throws ResourceNotFoundException;

    //Get-All-Notes
    List<NotesDto> getAllNotes();

}
