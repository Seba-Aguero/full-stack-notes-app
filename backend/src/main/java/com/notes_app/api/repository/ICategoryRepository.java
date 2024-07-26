package com.notes_app.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_app.api.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByName(String name);

}
