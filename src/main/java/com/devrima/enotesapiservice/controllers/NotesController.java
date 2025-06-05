package com.devrima.enotesapiservice.controllers;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.services.impl.NotesServiceImpl;
import com.devrima.enotesapiservice.util.CommonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notes/")
public class NotesController {
    private final NotesServiceImpl notesService;

    public NotesController(NotesServiceImpl notesService) {
        this.notesService = notesService;
    }

    //Save-API
    @PostMapping("save")
    public ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto ) throws ResourceNotFoundException {
        Boolean saveNote = notesService.saveNotes ( notesDto );
        if (saveNote ){
          return   CommonUtil.createBuildResponseMessage ( "Notes has been created successfully", HttpStatus.CREATED );
        }
        return  CommonUtil.createErrorResponseMessage ( "Could not create a notes",HttpStatus.INTERNAL_SERVER_ERROR );

    }

    //Get-All-Notes-> API
    @GetMapping()
    public ResponseEntity<?> getAllNotes(){
        List<NotesDto> allNotes = notesService.getAllNotes ();
        if (CollectionUtils.isEmpty ( allNotes )){
            return  ResponseEntity.noContent ().build ();
        }
        return CommonUtil.createBuildResponse ( "Get All notes successfully",allNotes,HttpStatus.OK );

    }


}
