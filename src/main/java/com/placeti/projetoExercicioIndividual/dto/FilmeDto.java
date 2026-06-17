package com.placeti.projetoExercicioIndividual.dto;

import com.placeti.projetoExercicioIndividual.model.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilmeDto(Long id,
                       @NotBlank
                       String nome,
                       @NotNull
                       Boolean assistido,
                       @NotNull(message = "Id do gênero não pode ser nula")
                       Long idGenero,
                       @NotNull(message = "Id dos atores não pode ser nula")
                       List<Long> idAtores)
{
}
