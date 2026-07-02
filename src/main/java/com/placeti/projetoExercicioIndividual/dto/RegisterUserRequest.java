package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(

        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String senha
) {}
