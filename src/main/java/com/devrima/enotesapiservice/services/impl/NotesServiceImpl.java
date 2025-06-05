package com.devrima.enotesapiservice.services.impl;

import com.devrima.enotesapiservice.dto.NotesDto;
import com.devrima.enotesapiservice.exception.ResourceNotFoundException;
import com.devrima.enotesapiservice.models.Notes;
import com.devrima.enotesapiservice.repositories.CategoryRepo;
import com.devrima.enotesapiservice.repositories.NotesRepo;
import com.devrima.enotesapiservice.services.NotesService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepo notesRepo;
    private final ModelMapper mapper;
    private final CategoryRepo categoryRepo;
    public NotesServiceImpl(NotesRepo notesRepo, ModelMapper mapper, CategoryRepo categoryRepo) {
        this.notesRepo = notesRepo;
        this.mapper = mapper;
        this.categoryRepo = categoryRepo;
    }


    @Override
    public Boolean saveNotes(NotesDto notesDto) throws ResourceNotFoundException {

        checkCategoryIsExist(notesDto.getCategory ());
        Notes notes = mapper.map ( notesDto, Notes.class );
        Notes saveNotes = notesRepo.save ( notes );
        if(!ObjectUtils.isEmpty ( saveNotes )){
            return true;
        }
        return false;
    }

    private void checkCategoryIsExist(NotesDto.CategoryDto category) throws ResourceNotFoundException {
        categoryRepo.findById ( category.getId () ).orElseThrow (()->new ResourceNotFoundException ( "Category is not exist in database" ));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        List<Notes> allNotes = notesRepo.findAll ();
        return allNotes.stream ().map ( notes -> mapper.map ( notes, NotesDto.class ) ).toList ();

    }
}
