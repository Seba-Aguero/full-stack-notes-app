package com.notes_app.api.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.notes_app.api.dto.NoteDto;
import com.notes_app.api.entity.Note;
import com.notes_app.api.mapper.NoteMapper;
import com.notes_app.api.repository.ICategoryRepository;
import com.notes_app.api.repository.INoteRepository;
import com.notes_app.api.service.impl.NoteServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTests {

  @Mock
  private INoteRepository noteRepository;

  @Mock
  private ICategoryRepository categoryRepository;

  @Mock
  private NoteMapper noteMapper;

  @InjectMocks
  private NoteServiceImpl noteService;

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
  public void NoteService_CreateNote_ReturnCreatedNote() {

    when(noteMapper.dtoToEntity(Mockito.any(NoteDto.class))).thenReturn(note);
    when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
    when(noteMapper.entityToDto(Mockito.any(Note.class))).thenReturn(noteDto);

    NoteDto createdNote = noteService.createNote(noteDto);

    assertThat(createdNote).isNotNull();
    assertThat(createdNote.getTitle()).isEqualTo("Test Note");
    assertThat(createdNote).isEqualTo(noteDto);
  }

  @Test
  public void NoteService_GetNoteById_ReturnNote() {

    when(noteRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(note));
    when(noteMapper.entityToDto(Mockito.any(Note.class))).thenReturn(noteDto);

    NoteDto foundNote = noteService.getNoteById(note.getId());

    assertThat(foundNote).isNotNull();
    assertThat(foundNote.getTitle()).isEqualTo("Test Note");
    assertThat(foundNote).isEqualTo(noteDto);
}

  @Test
  public void NoteService_GetAllNotes_ReturnAllNotes() {
    Note anotherNote = Note.builder()
      .id(2L)
      .title("Another Note")
      .content("Another Content")
      .categories(new HashSet<>())
      .build();

    when(noteRepository.findAll()).thenReturn(List.of(note, anotherNote));
    when(noteMapper.entityToDto(note)).thenReturn(noteDto);

    List<NoteDto> notes = noteService.getAllNotes();

    assertThat(notes).isNotEmpty();
    assertThat(notes).hasSize(2);
  }

  @Test
  public void NoteService_UpdateNote_ReturnUpdatedNote() {
    when(noteRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(note));
    when(noteMapper.dtoToEntity(Mockito.any(NoteDto.class))).thenReturn(note);
    when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
    when(noteMapper.entityToDto(Mockito.any(Note.class))).thenReturn(noteDto);

    NoteDto updatedNote = noteService.updateNote(note.getId(), noteDto);

    assertThat(updatedNote).isNotNull();
    assertThat(updatedNote.getTitle()).isEqualTo(noteDto.getTitle());
    assertThat(updatedNote).isEqualTo(noteDto);
  }

  @Test
  public void NoteService_DeleteNoteById_DeletesNote() {
    when(noteRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(note));
    doNothing().when(noteRepository).delete(Mockito.any(Note.class));

    noteService.deleteNoteById(note.getId());

    verify(noteRepository).delete(note);
  }

  @Test
  public void NoteService_ArchiveNote_ReturnArchivedNote() {
    note.setArchived(true);
    when(noteRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(note));
    when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
    when(noteMapper.entityToDto(Mockito.any(Note.class))).thenReturn(noteDto);

    NoteDto archivedNote = noteService.archiveNote(note.getId());

    assertThat(archivedNote).isNotNull();
    assertThat(archivedNote.getTitle()).isEqualTo(noteDto.getTitle());
    assertThat(archivedNote).isEqualTo(noteDto);
  }

  @Test
  public void NoteService_UnarchiveNote_ReturnUnarchivedNote() {
    note.setArchived(false);
    when(noteRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(note));
    when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);
    when(noteMapper.entityToDto(Mockito.any(Note.class))).thenReturn(noteDto);

    NoteDto unarchivedNote = noteService.unarchiveNote(note.getId());

    assertThat(unarchivedNote).isNotNull();
    assertThat(unarchivedNote.getTitle()).isEqualTo(noteDto.getTitle());
    assertThat(unarchivedNote).isEqualTo(noteDto);
  }

  @Test
  public void NoteService_GetAllActiveNotes_ReturnAllActiveNotes() {
    when(noteRepository.findByArchived(false)).thenReturn(List.of(note));
    when(noteMapper.entityToDto(note)).thenReturn(noteDto);

    List<NoteDto> activeNotes = noteService.getAllActiveNotes();

    assertThat(activeNotes).isNotEmpty();
    assertThat(activeNotes).hasSize(1);
  }

  @Test
  public void NoteService_GetAllArchivedNotes_ReturnAllArchivedNotes() {
    note.setArchived(true);
    when(noteRepository.findByArchived(true)).thenReturn(List.of(note));
    when(noteMapper.entityToDto(note)).thenReturn(noteDto);

    List<NoteDto> archivedNotes = noteService.getAllArchivedNotes();

    assertThat(archivedNotes).isNotEmpty();
    assertThat(archivedNotes).hasSize(1);
  }

}
