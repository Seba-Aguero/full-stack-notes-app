package com.notes_app.api.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.notes_app.api.dto.CategoryDto;
import com.notes_app.api.dto.NoteDto;
import com.notes_app.api.entity.Note;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoteMapper {

    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    public Note dtoToEntity(NoteDto dto) {
        return modelMapper.map(dto, Note.class);
    }

    public NoteDto entityToDto(Note note) {
        Set<CategoryDto> categories = note.getCategories().stream()
            .map(categoryMapper::entityToDto)
            .collect(Collectors.toSet());

        NoteDto noteDto = modelMapper.map(note, NoteDto.class);
        noteDto.setCategories(categories);
        return noteDto;
    }
}
