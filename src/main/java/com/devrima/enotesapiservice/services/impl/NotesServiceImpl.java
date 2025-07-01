package com.devrima.enotesapiservice.services.impl;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.dto.NotesResponse;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.FileDetails;
import com.devrima.enotesapiservice.models.Notes;
import com.devrima.enotesapiservice.repositories.CategoryRepo;
import com.devrima.enotesapiservice.repositories.FileDetailsRepo;
import com.devrima.enotesapiservice.repositories.NotesRepo;
import com.devrima.enotesapiservice.services.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepo notesRepo;
    private final ModelMapper mapper;
    private final CategoryRepo categoryRepo;
    private final FileDetailsRepo fileDetailsRepo;
    public NotesServiceImpl(NotesRepo notesRepo, ModelMapper mapper, CategoryRepo categoryRepo, FileDetailsRepo fileDetailsRepo) {
        this.notesRepo = notesRepo;
        this.mapper = mapper;
        this.categoryRepo = categoryRepo;
        this.fileDetailsRepo = fileDetailsRepo;
    }

    @Value ( "${file.upload.path}" )
    private String uploadPath;

    //SAVE-NOTES-METHOD
    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws ResourceNotFoundException, IOException {

        ObjectMapper ob = new ObjectMapper ();
        NotesDto notesDto = ob.readValue ( notes, NotesDto.class );
        notesDto.setIsDeleted(false);
        notesDto.setDeleteOn ( null );
        checkCategoryIsExist(notesDto.getCategory ());
        Notes notesMap = mapper.map ( notesDto, Notes.class );
        FileDetails fileDetails= saveFileDetails(file);

        if(!ObjectUtils.isEmpty ( notesDto.getId () )){
            updateNotes(notesDto,file);
        }

        if(!ObjectUtils.isEmpty ( fileDetails )){
            notesMap.setFileDetails ( fileDetails );
        }else {
            if(ObjectUtils.isEmpty ( notesDto.getId () )) {
                notesMap.setFileDetails ( null );
            }
        }

        Notes saveNotes = notesRepo.save ( notesMap );
        if(!ObjectUtils.isEmpty ( saveNotes )){
            return true;
        }
        return false;
    }
    //UPDATE-NOTES-METHOD
    private void updateNotes(NotesDto notesDto, MultipartFile file) throws ResourceNotFoundException {
        Notes existsNotes = notesRepo.findById ( notesDto.getId () ).orElseThrow ( () -> new ResourceNotFoundException ( "We don't found this user by this id" + notesDto.getId () ));

        if(!ObjectUtils.isEmpty ( file )){
            notesDto.setFileDetails ( mapper.map ( existsNotes.getFileDetails (), NotesDto.FileDetailsDto.class ) );
        }

    }

    //SAVE -FILE-Details
    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if (!ObjectUtils.isEmpty ( file ) &&!file.isEmpty ()){
            String originalFileName = file.getOriginalFilename ();
            String extension = FilenameUtils.getExtension ( originalFileName );

            List<String> extensionAllowed = Arrays.asList ( "pdf", "xlsx", "jpg","png" );
            if (!extensionAllowed.contains ( extension )){
                throw new IllegalAccessError ("Could not upload with this extension !!,You can upload this kind of extension(pdf,xlsx,jpg)");
            }

            String rndFileName = UUID.randomUUID ().toString ();
            String uploadFileName = rndFileName+"."+extension; //dsgrfefeff.pfd

            File fileIo = new File (uploadPath);
            if (!fileIo.exists ()){
                fileIo.mkdirs ();
            }
            //path : enotes/serviceiml/notes/dsgrfefeff.pfd
            String storePath = uploadPath.concat ( uploadFileName );
            //Upload - file
            long uploadFile = Files.copy ( file.getInputStream (), Paths.get ( storePath ) );
            if (uploadFile!=0){
                FileDetails fileDetails = new FileDetails ();
                fileDetails.setOriginalFileName ( originalFileName );
                fileDetails.setDisplayFileName ( getDisplayName(originalFileName) );
                fileDetails.setUploadFileName ( uploadFileName );
                fileDetails.setFileSize ( file.getSize () );
                fileDetails.setFilePath ( storePath );
                FileDetails saveFileDetails = fileDetailsRepo.save ( fileDetails );
                return saveFileDetails;
            }


        }
        return null;

    }

    //Create DISPLAY FILE-NAME
    private String getDisplayName(String originalFileName) {

        //java_programming.pdf
        String extension = FilenameUtils.getExtension ( originalFileName );
        String fileName = FilenameUtils.removeExtension ( originalFileName );
        if (fileName.length ()>8){
            fileName = fileName.substring ( 0, 7 );
        }
        fileName = fileName+"."+extension;

        return fileName;
    }

    private void checkCategoryIsExist(NotesDto.CategoryDto category) throws ResourceNotFoundException {
        categoryRepo.findById ( category.getId () ).orElseThrow (()->new ResourceNotFoundException ( "Category is not exist in database" ));
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of ( pageNo,pageSize );
        Page<Notes> pageNotes = notesRepo.findByCreatedByAndIsDeletedFalse (userId,pageable);

        List<NotesDto> notesDto = pageNotes.get ().map ( notes -> mapper.map ( notes, NotesDto.class ) ).toList ();
        NotesResponse notesResponse = NotesResponse.builder ()
                .notes ( notesDto )
                .pageNo ( pageNotes.getNumber () )
                .pageSize ( pageNotes.getSize () )
                .totalElements ( pageNotes.getTotalElements () )
                .totalPages ( pageNotes.getTotalPages () )
                .isFirst ( pageNotes.isFirst () )
                .isLast ( pageNotes.isLast () )
                .build ();
            return notesResponse;

    }
    //DELETE-NOTES->API
    @Override
    public void softDeleteNotes(Integer id) throws ResourceNotFoundException {
        Notes notes = notesRepo.findById ( id ).orElseThrow ( () -> new ResourceNotFoundException ( "Notes not found by this is" + id ) );
        notes.setIsDeleted ( true );
        notes.setDeleteOn ( new Date () );
        notesRepo.save ( notes );

    }

    //RESTORE-DELETE-NOTES->API
    @Override
    public void restoreDeleteNotes(Integer id) throws ResourceNotFoundException {
        Notes notes = notesRepo.findById ( id ).orElseThrow ( () -> new ResourceNotFoundException ( "Notes not found by id" + id ) );
        notes.setIsDeleted ( false );
        notes.setDeleteOn ( null );
        notesRepo.save ( notes );
    }

    //RECYCLE-BIN-DELETED-NOTES

    @Override
    public List<NotesDto> recycleBinUserById(Integer userId) {
        List<Notes> notes= notesRepo.findByCreatedByAndIsDeletedTrue (userId );
        List<NotesDto> notesDtoList= notes.stream ().map ( note->mapper.map ( note,NotesDto.class ) ).toList ();
        return notesDtoList;

    }

    @Override
    public byte[] getDownloadFile(FileDetails fileDetails) throws IOException {
        InputStream fileInputStream = new FileInputStream ( fileDetails.getFilePath () );

        return StreamUtils.copyToByteArray ( fileInputStream );
    }

    //Get the file wit Id;
    @Override
    public FileDetails getFileDetails(Integer id) throws ResourceNotFoundException {
        FileDetails fileDetails = fileDetailsRepo.findById ( id ).orElseThrow (()-> new ResourceNotFoundException ( "File Not Found by id "+id ));

        return  fileDetails;
    }


}
