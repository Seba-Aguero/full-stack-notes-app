package com.notes_app.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_app.api.entity.Note;

@Repository
public interface INoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByArchived(boolean b);

}
