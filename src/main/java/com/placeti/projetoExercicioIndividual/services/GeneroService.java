package com.placeti.projetoExercicioIndividual.services;

import com.placeti.projetoExercicioIndividual.dto.GeneroDto;
import com.placeti.projetoExercicioIndividual.exceptions.GeneroNotFoundException;
import com.placeti.projetoExercicioIndividual.model.Filme;
import com.placeti.projetoExercicioIndividual.model.Genero;
import com.placeti.projetoExercicioIndividual.repository.FilmeRepository;
import com.placeti.projetoExercicioIndividual.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroService {
    private final GeneroRepository generoRepository;
    private final FilmeRepository filmeRepository;

    public GeneroService(GeneroRepository generoRepository, FilmeRepository filmeRepository) {
        this.generoRepository = generoRepository;
        this.filmeRepository = filmeRepository;
    }
    //Estudar sobre problema N + 1
    public List<GeneroDto> listarGeneros()
    {
        return generoRepository.findAll()
                .stream()
                .map(g -> new GeneroDto(g.getId(), g.getNome(), g.getFilmes()
                        .stream()
                        .map(Filme::getId)
                        .toList())).toList();
    }

    public GeneroDto listarGeneroPorId(Long id)
    {
        Genero g = generoRepository.findById(id)
                .orElseThrow(
                        () -> new GeneroNotFoundException("Genero não encontrado com esse id")
                );
        return new GeneroDto(g.getId(), g.getNome(), g.getFilmes()
                .stream()
                .map(Filme::getId)
                .toList());
    }
    public GeneroDto incluirGenero(GeneroDto generoDto)
    {
        if (generoDto.id() != null)
        {
            throw new IllegalArgumentException("Id precisa ser nulo");
        }
        //E se não achar um filme?
        List<Filme> filmes = filmeRepository.findAllById(generoDto.filmesId());

        Genero g = new Genero();
        g.setNome(generoDto.nome());
        g.setFilmes(filmes);
        Genero gPersistido = generoRepository.save(g);
        System.out.println(gPersistido.getId());
        return new GeneroDto(gPersistido.getId(), gPersistido.getNome(), gPersistido.getFilmes()
                .stream()
                .map(Filme::getId)
                .toList());
    }
}
