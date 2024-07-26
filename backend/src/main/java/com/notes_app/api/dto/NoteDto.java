package com.notes_app.api.dto;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Length(max = 50, message = "Title cannot be longer than 50 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull
    private boolean archived;

    @NotNull
    private Set<CategoryDto> categories;
}
