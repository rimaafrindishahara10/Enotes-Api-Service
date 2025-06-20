package com.devrima.enotesapiservice.controllers;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.FileDetails;
import com.devrima.enotesapiservice.services.impl.NotesServiceImpl;
import com.devrima.enotesapiservice.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public ResponseEntity<?> saveNotes(@RequestParam String notes , @RequestParam(required = false) MultipartFile file) throws ResourceNotFoundException, IOException {
        Boolean saveNote = notesService.saveNotes ( notes,file );
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

    //Get-File-Id-for->Download-File
    @GetMapping("download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id ) throws ResourceNotFoundException, IOException {
        FileDetails fileDetails = notesService.getFileDetails ( id );
        byte[] downloadFile = notesService.getDownloadFile ( fileDetails );

        HttpHeaders httpHeaders = new HttpHeaders ();
        String contentType = CommonUtil.getContentType ( fileDetails.getOriginalFileName () );
        httpHeaders.setContentType ( MediaType.parseMediaType ( contentType ) );
        httpHeaders.setContentDispositionFormData ("attachment", fileDetails.getOriginalFileName () );

        return ResponseEntity.ok ().headers ( httpHeaders ).body ( downloadFile );


    }

}
