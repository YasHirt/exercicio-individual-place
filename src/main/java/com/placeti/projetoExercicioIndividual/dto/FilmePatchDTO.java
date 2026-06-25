package com.placeti.projetoExercicioIndividual.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

// REVISÃO: O id não deve vir no corpo do PATCH. Em REST, o identificador do recurso
// a ser atualizado deve vir na URL (ex.: PATCH /filmes/{id}).
// Remova o campo id daqui e receba-o como @PathVariable no controller.
// Também remova o @NotNull do id após essa correção.
public record FilmePatchDTO(
        @NotNull
        Long id,
        String nome,
        Boolean assistido,
        Long idGenero, List<Long> idAtores ) {}
