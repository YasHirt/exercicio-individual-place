package com.placeti.projetoExercicioIndividual.dto;

import com.placeti.projetoExercicioIndividual.model.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilmeDto(Long id,
                       @NotBlank
                       String nome,
                       @NotNull
                       Boolean assistido,
                       @NotBlank
                       Genero genero) {
}
