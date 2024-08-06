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
import com.notes_app.api.dto.NoteDto;
import com.notes_app.api.entity.Note;
import com.notes_app.api.service.impl.NoteServiceImpl;

import java.util.HashSet;
import java.util.List;

@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class NoteControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NoteServiceImpl noteService;

  @Autowired
  private ObjectMapper objectMapper;

  private Note note;
  private NoteDto noteDto;

  @BeforeEach
  void setUp() {
    note = Note.builder()
      .id(1L)
      .title("Test Note")
      .content("Test Content")
      .categories(new HashSet<>())
      .build();

    noteDto = NoteDto.builder()
      .id(1L)
      .title("Test Note")
      .content("Test Content")
      .categories(new HashSet<>())
      .build();
  }

  @Test
  public void NoteController_CreateNote_ReturnsCreatedStatus() throws Exception {

    when(noteService.createNote(noteDto)).thenReturn(noteDto);

    mockMvc.perform(post("/api/v1/notes")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(noteDto)))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Test Content"));
  }

  @Test
  public void NoteController_GetNoteById_ReturnsOkStatus() throws Exception {

    when(noteService.getNoteById(note.getId())).thenReturn(noteDto);

    mockMvc.perform(get("/api/v1/notes/{id}", note.getId()))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Test Content"));
  }

  @Test
  public void NoteController_GetAllNotes_ReturnsOkStatus() throws Exception {

    when(noteService.getAllNotes()).thenReturn(List.of(noteDto));

    mockMvc.perform(get("/api/v1/notes"))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Test Content"));
  }

  @Test
  public void NoteController_UpdateNote_ReturnsOkStatus() throws Exception {

    when(noteService.updateNote(note.getId(), noteDto)).thenReturn(noteDto);

    mockMvc.perform(put("/api/v1/notes/{id}", note.getId())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(noteDto)))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("Test Content"));
  }

  @Test
  public void NoteController_DeleteNote_ReturnsNoContentStatus() throws Exception {

    mockMvc.perform(delete("/api/v1/notes/{id}", note.getId()))
      .andExpect(status().isNoContent());
  }

  @Test
  public void NoteController_GetAllActiveNotes_ReturnsOkStatus() throws Exception {

    when(noteService.getAllActiveNotes()).thenReturn(List.of(noteDto));

    mockMvc.perform(get("/api/v1/notes/active"))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Test Content"));
  }

  @Test
  public void NoteController_GetAllArchivedNotes_ReturnsOkStatus() throws Exception {

    when(noteService.getAllArchivedNotes()).thenReturn(List.of(noteDto));

    mockMvc.perform(get("/api/v1/notes/archived"))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Note"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("Test Content"));
  }
}
