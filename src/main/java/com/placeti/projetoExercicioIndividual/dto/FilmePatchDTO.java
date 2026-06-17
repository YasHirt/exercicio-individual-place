package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilmePatchDTO(
        @NotNull
        Long id,
        String nome,
        Boolean assistido,
        Long idGenero, List<Long> idAtores ) {}
