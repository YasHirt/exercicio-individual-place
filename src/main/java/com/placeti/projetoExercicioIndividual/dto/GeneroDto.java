package com.placeti.projetoExercicioIndividual.dto;

import com.placeti.projetoExercicioIndividual.model.Filme;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GeneroDto(Long id,
                        @NotBlank
                        String nome,
                        @NotNull
                        List<Long> filmesId) {}
