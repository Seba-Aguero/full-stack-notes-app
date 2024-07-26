package com.notes_app.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_app.api.entity.NoteCategory;

@Repository
public interface INoteCategoryRepository extends JpaRepository<NoteCategory, NoteCategory.NoteCategoryId> {
    void deleteByIdCategoryId(Long categoryId);

    void deleteByIdNoteId(Long noteId);
}