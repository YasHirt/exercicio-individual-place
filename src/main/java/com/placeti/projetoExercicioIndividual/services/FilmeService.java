package com.placeti.projetoExercicioIndividual.services;


import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
import com.placeti.projetoExercicioIndividual.exceptions.FilmeNotFoundException;
import com.placeti.projetoExercicioIndividual.model.Filme;
import com.placeti.projetoExercicioIndividual.repository.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    //Método que vai listar todos os filmes

    public List<FilmeDto> listarFilmes()
    {
        //TODO: CUSTOMIZAR ESSE ERRO
        return filmeRepository.findAll().stream().map(filme -> new FilmeDto(filme.getId(), filme.getNome(), filme.getAssistido(), filme.getGenero())).toList();
    }
}
