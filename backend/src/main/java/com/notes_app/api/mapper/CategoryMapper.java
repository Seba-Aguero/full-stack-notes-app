package com.notes_app.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.notes_app.api.dto.CategoryDto;
import com.notes_app.api.entity.Category;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Category dtoToEntity(CategoryDto dto) {
        return modelMapper.map(dto, Category.class);
    }

    public CategoryDto entityToDto(Category user) {
        return modelMapper.map(user, CategoryDto.class);
    }
}
