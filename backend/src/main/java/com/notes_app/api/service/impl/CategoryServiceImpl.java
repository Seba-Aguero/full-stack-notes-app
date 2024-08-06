package com.notes_app.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.notes_app.api.dto.CategoryDto;
import com.notes_app.api.entity.Category;
import com.notes_app.api.exception.ResourceNotFoundException;
import com.notes_app.api.mapper.CategoryMapper;
import com.notes_app.api.repository.ICategoryRepository;
import com.notes_app.api.repository.INoteCategoryRepository;
import com.notes_app.api.service.ICategoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final INoteCategoryRepository noteCategoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new com.notes_app.api.exception.BadRequestException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = categoryMapper.dtoToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.entityToDto(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        return categoryMapper.entityToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::entityToDto)
                .toList();
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new com.notes_app.api.exception.BadRequestException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category updatedCategory = categoryMapper.dtoToEntity(categoryDto);
        updatedCategory.setId(id);
        categoryRepository.save(updatedCategory);
        return categoryMapper.entityToDto(updatedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        noteCategoryRepository.deleteByIdCategoryId(id);
        categoryRepository.deleteById(id);
    }

}
