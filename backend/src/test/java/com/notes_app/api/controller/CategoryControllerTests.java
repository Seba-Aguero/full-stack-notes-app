package com.notes_app.api.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notes_app.api.dto.CategoryDto;
import com.notes_app.api.entity.Category;
import com.notes_app.api.service.impl.CategoryServiceImpl;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryServiceImpl categoryService;

  @Autowired
  private ObjectMapper objectMapper;

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
  public void CategoryController_CreateCategory_ReturnsCreatedStatus() throws Exception {

    when(categoryService.createCategory(categoryDto)).thenReturn(categoryDto);

    mockMvc.perform(post("/api/v1/categories")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(categoryDto)))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category"));
  }

  @Test
  public void CategoryController_GetCategoryById_ReturnsOkStatus() throws Exception {

    when(categoryService.getCategoryById(category.getId())).thenReturn(categoryDto);

    mockMvc.perform(get("/api/v1/categories/{id}", category.getId()))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category"));
  }

  @Test
  public void CategoryController_GetAllCategories_ReturnsOkStatus() throws Exception {

    when(categoryService.getAllCategories()).thenReturn(java.util.Arrays.asList(categoryDto));

    mockMvc.perform(get("/api/v1/categories"))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Test Category"));
  }

  @Test
  public void CategoryController_UpdateCategory_ReturnsOkStatus() throws Exception {

    when(categoryService.updateCategory(category.getId(), categoryDto)).thenReturn(categoryDto);

    mockMvc.perform(put("/api/v1/categories/{id}", category.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(categoryDto)))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Category"));
  }

  @Test
  public void CategoryController_DeleteCategory_ReturnsNoContentStatus() throws Exception {

    mockMvc.perform(delete("/api/v1/categories/{id}", category.getId()))
      .andExpect(status().isNoContent());
  }
}
