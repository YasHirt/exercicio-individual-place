package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilmePatchDTO(
        String nome,
        Boolean assistido,
        Long idGenero, List<Long> idAtores ) {}
