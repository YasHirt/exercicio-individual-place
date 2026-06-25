package com.placeti.projetoExercicioIndividual.dto;

// REVISÃO: AtorDto está declarado como interface, não como um DTO.
// DTOs são usados para transportar dados entre camadas, então deve ser um record (ou classe).
// Defina os campos que um Ator precisa expor (ex.: id e nome) e troque para record.
// Exemplo: public record AtorDto(Long id, String nome) {}
public interface AtorDto {
}
