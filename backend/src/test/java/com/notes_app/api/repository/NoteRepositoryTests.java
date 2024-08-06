package com.notes_app.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.notes_app.api.entity.Note;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
public class NoteRepositoryTests {

  @Autowired
  private INoteRepository noteRepository;

  @Test
  public void NoteRepository_Save_ReturnSavedNote() {

    Note note = Note.builder()
      .title("Test Note")
      .content("Test Content")
      .archived(false)
      .build();

    Note savedNote = noteRepository.save(note);

    assertThat(savedNote).isNotNull();
    assertThat(savedNote.getId()).isGreaterThan(0);
  }

  @Test
  public void NoteRepository_FindAll_ReturnAllNotes() {

    Note note1 = Note.builder()
      .title("Test Note 1")
      .content("Test Content 1")
      .archived(false)
      .build();

    Note note2 = Note.builder()
      .title("Test Note 2")
      .content("Test Content 2")
      .archived(false)
      .build();

    noteRepository.save(note1);
    noteRepository.save(note2);

    List<Note> categories = noteRepository.findAll();

    assertThat(categories).isNotEmpty();
    assertThat(categories).hasSize(2);
  }

  @Test
  public void NoteRepository_FindById_ReturnNote() {

    Note note = Note.builder()
      .title("Test Note")
      .content("Test Content")
      .archived(false)
      .build();

    noteRepository.save(note);

    Note foundNote = noteRepository.findById(note.getId()).get();

    assertThat(foundNote).isNotNull();
    assertThat(foundNote.getId()).isEqualTo(note.getId());
  }

  @Test
  public void NoteRepository_Update_ReturnUpdatedNote() {

    Note note = Note.builder()
      .title("Test Note")
      .content("Test Content")
      .archived(false)
      .build();

    noteRepository.save(note);

    Note updatedNote = Note.builder()
      .id(note.getId())
      .title("Updated Note")
      .content("Updated Content")
      .archived(false)
      .build();

    noteRepository.save(updatedNote);

    Note foundNote = noteRepository.findById(note.getId()).get();

    assertThat(foundNote).isNotNull();
    assertThat(foundNote.getId()).isEqualTo(note.getId());
    assertThat(foundNote.getTitle()).isEqualTo("Updated Note");
    assertThat(foundNote.getContent()).isEqualTo("Updated Content");
  }

  @Test
  public void NoteRepository_Delete_ReturnDeletedNote() {

    Note note = Note.builder()
      .title("Test Note")
      .content("Test Content")
      .archived(false)
      .build();

    noteRepository.save(note);

    noteRepository.deleteById(note.getId());

    Note foundNote = noteRepository.findById(note.getId()).orElse(null);

    assertThat(foundNote).isNull();
  }

  @Test
  public void NoteRepository_findByArchived_ReturnNotes() {

    Note note1 = Note.builder()
      .title("Test Note 1")
      .content("Test Content 1")
      .archived(false)
      .build();

    Note note2 = Note.builder()
      .title("Test Note 2")
      .content("Test Content 2")
      .archived(false)
      .build();

    Note note3 = Note.builder()
      .title("Test Note 3")
      .content("Test Content 3")
      .archived(true)
      .build();

    noteRepository.save(note1);
    noteRepository.save(note2);
    noteRepository.save(note3);

    List<Note> notes = noteRepository.findByArchived(false);

    assertThat(notes).isNotEmpty();
    assertThat(notes).hasSize(2);
  }

}
