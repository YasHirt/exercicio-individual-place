package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(
        @NotEmpty
        String email,
        @NotEmpty
        String senha
) {}
