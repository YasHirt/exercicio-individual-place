package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotBlank;

public record AtorDto(Long id,
                      @NotBlank(message = "Nome do ator não pode ser nulo")
                      String nome ) {
}
