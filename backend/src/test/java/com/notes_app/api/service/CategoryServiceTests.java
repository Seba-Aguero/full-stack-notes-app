package com.notes_app.api.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.notes_app.api.dto.CategoryDto;
import com.notes_app.api.entity.Category;
import com.notes_app.api.mapper.CategoryMapper;
import com.notes_app.api.repository.ICategoryRepository;
import com.notes_app.api.repository.INoteCategoryRepository;
import com.notes_app.api.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

  @Mock
  private ICategoryRepository categoryRepository;

  @Mock
  private INoteCategoryRepository noteCategoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private Category category;
  private CategoryDto categoryDto;

  @BeforeEach
  void setUp() {
    category = Category.builder()
      .id(1L)
      .name("Test Category")
      .build();

    categoryDto = CategoryDto.builder()
      .id(1L)
      .name("Test Category")
      .build();
  }

  @Test
  public void CategoryService_CreateCategory_ReturnCreatedCategory() {

    when(categoryMapper.dtoToEntity(Mockito.any(CategoryDto.class))).thenReturn(category);
    when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);
    when(categoryMapper.entityToDto(Mockito.any(Category.class))).thenReturn(categoryDto);

    CategoryDto savedCategory = categoryService.createCategory(categoryDto);

    assertThat(savedCategory).isNotNull();
    assertThat(savedCategory.getName()).isEqualTo("Test Category");
    assertThat(savedCategory).isEqualTo(categoryDto);
  }

  @Test
  public void CategoryService_GetCategoryById_ReturnCategory() {

    when(categoryRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(category));
    when(categoryMapper.entityToDto(Mockito.any(Category.class))).thenReturn(categoryDto);

    CategoryDto foundCategory = categoryService.getCategoryById(category.getId());

    assertThat(foundCategory).isNotNull();
    assertThat(foundCategory.getName()).isEqualTo("Test Category");
    assertThat(foundCategory).isEqualTo(categoryDto);
  }

  @Test
  public void CategoryService_GetAllCategories_ReturnAllCategories() {

    Category anotherCategory = Category.builder()
      .id(2L)
      .name("Another Test Category")
      .build();

    when(categoryRepository.findAll()).thenReturn(java.util.Arrays.asList(category, anotherCategory));

    List<CategoryDto> categories = categoryService.getAllCategories();

    assertThat(categories).isNotEmpty();
    assertThat(categories).hasSize(2);
  }

  @Test
  public void CategoryService_UpdateCategory_ReturnUpdatedCategory() {

    categoryDto.setName("Updated Category");

    Category updatedCategory = Category.builder()
      .id(1L)
      .name("Updated Category")
      .build();

    when(categoryRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(category));
    when(categoryMapper.dtoToEntity(Mockito.any(CategoryDto.class))).thenReturn(updatedCategory);
    when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(updatedCategory);
    when(categoryMapper.entityToDto(Mockito.any(Category.class))).thenReturn(categoryDto);

    CategoryDto result = categoryService.updateCategory(category.getId(), categoryDto);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Updated Category");
    assertThat(result).isEqualTo(categoryDto);
  }

  @Test
  public void CategoryService_DeleteCategoryById_ReturnDeletedCategory() {

    when(categoryRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(category));
    //doNothing since these methods return void
    doNothing().when(noteCategoryRepository).deleteByIdCategoryId(category.getId());
    doNothing().when(categoryRepository).deleteById(category.getId());

    categoryService.deleteCategoryById(category.getId());

    verify(noteCategoryRepository).deleteByIdCategoryId(category.getId());
    verify(categoryRepository).deleteById(category.getId());

  }

}