package com.notes_app.api.service;

import com.notes_app.api.dto.NoteDto;
import java.util.List;

public interface INoteService {
    NoteDto createNote(NoteDto noteDto);

    NoteDto getNoteById(Long id);

    List<NoteDto> getAllNotes();

    NoteDto updateNote(Long id, NoteDto noteDto);

    //void deleteNote(Long id);

    void deleteNoteById(Long id);

    NoteDto archiveNote(Long id);

    NoteDto unarchiveNote(Long id);

    List<NoteDto> getAllActiveNotes();

    List<NoteDto> getAllArchivedNotes();
}