package com.placeti.projetoExercicioIndividual.dto;

public record ApiErrorDto(
        int status,
        String error,
        String message
)  {}