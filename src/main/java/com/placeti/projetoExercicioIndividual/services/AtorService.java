package com.placeti.projetoExercicioIndividual.services;

import com.placeti.projetoExercicioIndividual.dto.AtorDto;
import com.placeti.projetoExercicioIndividual.exceptions.AtorNotFoundException;
import com.placeti.projetoExercicioIndividual.model.Ator;
import com.placeti.projetoExercicioIndividual.repository.AtorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtorService {
    private final AtorRepository atorRepository;

    public AtorService(AtorRepository atorRepository) {
        this.atorRepository = atorRepository;
    }
    public AtorDto buscarAtorPorId(Long id)
    {
       Ator a = atorRepository.findById(id)
               .orElseThrow(
                       () -> new AtorNotFoundException("Ator não encontrado com esse id")
               );
        return new AtorDto(a.getId(), a.getNome());
    }
    public List<AtorDto> buscarAtores()
    {
        return atorRepository.findAll()
                .stream()
                .map(a -> new AtorDto(a.getId(), a.getNome()))
                .toList();
    }
    public AtorDto incluirAtor(AtorDto atorDto)
    {
        Ator a = new Ator();
        a.setNome(atorDto.nome());
        System.out.println(atorDto.nome());
        Ator atorpersistido = atorRepository.save(a);
        return new AtorDto(atorpersistido.getId(), atorpersistido.getNome());
    }
}
