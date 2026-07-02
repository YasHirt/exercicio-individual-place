package com.placeti.projetoExercicioIndividual.exceptions;

import com.placeti.projetoExercicioIndividual.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FilmeNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleFilmeNotFoundException(FilmeNotFoundException ex) {
        ApiErrorDto error = new ApiErrorDto(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(GeneroNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleGeneroNotFoundException(GeneroNotFoundException ex) {
        ApiErrorDto error = new ApiErrorDto(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AtorNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleAtorNotFoundException(AtorNotFoundException ex) {
        ApiErrorDto error = new ApiErrorDto(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ApiErrorDto> handleEmailDuplicadoException(EmailDuplicadoException ex)
    {
        ApiErrorDto error = new ApiErrorDto(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex)
    {
        ApiErrorDto error = new ApiErrorDto(404, "Not Found", "Usuario ou senha incorretos");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
