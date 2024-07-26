package com.notes_app.api.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private HttpStatus status;

    private String message;

    private List<String> errors;

}
