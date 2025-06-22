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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
        checkCategoryIsExist(notesDto.getCategory ());
        Notes notesMap = mapper.map ( notesDto, Notes.class );
        FileDetails fileDetails= saveFileDetails(file);

        if(!ObjectUtils.isEmpty ( fileDetails )){
            notesMap.setFileDetails ( fileDetails );
        }else {
            notesMap.setFileDetails ( null );
        }

        Notes saveNotes = notesRepo.save ( notesMap );
        if(!ObjectUtils.isEmpty ( saveNotes )){
            return true;
        }
        return false;
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
        Page<Notes> pageNotes = notesRepo.findByCreatedBy (userId,pageable);

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
