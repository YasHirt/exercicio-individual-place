package com.placeti.projetoExercicioIndividual.services;


import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
import com.placeti.projetoExercicioIndividual.exceptions.FilmeNotFoundException;
import com.placeti.projetoExercicioIndividual.model.Filme;
import com.placeti.projetoExercicioIndividual.repository.FilmeRepository;
import org.springframework.stereotype.Service;

@Service
public class FilmeService {
    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    //Método que vai mostrar filme pelo seu id

    public FilmeDto pesquisarFilme(Long id)
    {
        Filme f = filmeRepository.findById(id).orElseThrow(
                () -> new FilmeNotFoundException("Filme não encontrado com esse id " + id)
        );
        return new FilmeDto(f.getId(), f.getNome(), f.getAssistido(), f.getGenero());
    }
}
