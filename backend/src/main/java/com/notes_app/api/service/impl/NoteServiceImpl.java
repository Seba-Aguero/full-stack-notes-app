package com.notes_app.api.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notes_app.api.dto.NoteDto;
import com.notes_app.api.entity.Category;
import com.notes_app.api.entity.Note;
import com.notes_app.api.exception.ResourceNotFoundException;
import com.notes_app.api.mapper.NoteMapper;
import com.notes_app.api.repository.ICategoryRepository;
import com.notes_app.api.repository.INoteRepository;
import com.notes_app.api.service.INoteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements INoteService{

    @Autowired
    private INoteRepository noteRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public NoteDto createNote(NoteDto noteDto) {
      Note note = noteMapper.dtoToEntity(noteDto);

      Set<Category> managedCategories = new HashSet<>();
      for (Category category : note.getCategories()) {
          if (category.getId() != null) {
              managedCategories.add(categoryRepository.getReferenceById(category.getId()));
          } else {
              managedCategories.add(category);
          }
      }
      note.setCategories(managedCategories);

      Note savedNote = noteRepository.save(note);
      return noteMapper.entityToDto(savedNote);
    }

    @Override
    public NoteDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        return noteMapper.entityToDto(note);
    }

    @Override
    public List<NoteDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream()
                .map(noteMapper::entityToDto)
                .toList();
    }

    @Override
    public NoteDto updateNote(Long id, NoteDto noteDto) {
        noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        Note updatedNote = noteMapper.dtoToEntity(noteDto);
        updatedNote.setId(id);
        noteRepository.save(updatedNote);
        return noteMapper.entityToDto(updatedNote);
    }

    /* @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Note not found with id: " + id));
        noteRepository.delete(note);
    } */

    @Override
    public void deleteNoteById(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        note.getCategories().clear();
        noteRepository.save(note);

        noteRepository.delete(note);
    }

    @Override
    public NoteDto archiveNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        note.setArchived(true);
        Note archivedNote = noteRepository.save(note);
        return noteMapper.entityToDto(archivedNote);
    }

    @Override
    public NoteDto unarchiveNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        note.setArchived(false);
        Note unarchivedNote = noteRepository.save(note);
        return noteMapper.entityToDto(unarchivedNote);
    }

    @Override
    public List<NoteDto> getAllActiveNotes() {
        List<Note> activeNotes = noteRepository.findByArchived(false);
        return activeNotes.stream().map(noteMapper::entityToDto).toList();
    }

    @Override
    public List<NoteDto> getAllArchivedNotes() {
        List<Note> archivedNotes = noteRepository.findByArchived(true);
        return archivedNotes.stream().map(noteMapper::entityToDto).toList();
    }

}
