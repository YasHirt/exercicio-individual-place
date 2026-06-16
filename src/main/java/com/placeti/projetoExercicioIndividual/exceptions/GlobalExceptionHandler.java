package com.placeti.projetoExercicioIndividual.exceptions;

import com.placeti.projetoExercicioIndividual.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FilmeNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleFilmeNotFoundException(FilmeNotFoundException ex)
    {
        ApiErrorDto error = new ApiErrorDto(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
