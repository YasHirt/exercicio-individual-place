package com.placeti.projetoExercicioIndividual.services;
import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
import com.placeti.projetoExercicioIndividual.dto.FilmePatchDTO;
import com.placeti.projetoExercicioIndividual.exceptions.FilmeNotFoundException;
import com.placeti.projetoExercicioIndividual.exceptions.GeneroNotFoundException;
import com.placeti.projetoExercicioIndividual.model.Ator;
import com.placeti.projetoExercicioIndividual.model.Filme;
import com.placeti.projetoExercicioIndividual.model.Genero;
import com.placeti.projetoExercicioIndividual.repository.AtorRepository;
import com.placeti.projetoExercicioIndividual.repository.FilmeRepository;
import com.placeti.projetoExercicioIndividual.repository.GeneroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class FilmeService {
    private final FilmeRepository filmeRepository;
    private final GeneroRepository generoRepository;
    private final AtorRepository atorRepository;

    public FilmeService(FilmeRepository filmeRepository, GeneroRepository generoRepository, AtorRepository atorRepository) {
        this.filmeRepository = filmeRepository;
        this.generoRepository = generoRepository;
        this.atorRepository = atorRepository;
    }

    //Método que vai mostrar filme pelo seu id

    public FilmeDto pesquisarFilme(Long id)
    {
        Filme f = filmeRepository.findById(id).orElseThrow(
                () -> new FilmeNotFoundException("Filme não encontrado com esse id " + id)
        );
        return new FilmeDto(f.getId(), f.getNome(), f.getAssistido(), f.getGenero().getId(), f.getAtores().stream().map(Ator::getId).toList());
    }

    //Método que vai listar todos os filmes

    public List<FilmeDto> listarFilmes()
    {
        //TODO: CUSTOMIZAR ESSE ERRO
        return filmeRepository.findAll().stream().map(filme -> new FilmeDto(filme.getId(), filme.getNome(), filme.getAssistido(), filme.getGenero().getId(), filme.getAtores().stream().map(Ator::getId).toList())).toList();
    }

    //Método que vai incluir os filmes no banco
    @Transactional //Garante atomicidade, princípio do tudo ou nada
    public FilmeDto incluirFilme(FilmeDto filmeDto)
    {
        if (filmeDto.id() != null)
        {
            throw new RuntimeException("Id deve ser nula para criação");
        }
        Genero genero = generoRepository.findById(filmeDto.idGenero()).orElseThrow(
                () -> new GeneroNotFoundException("Genero não encontrado com esse id " + filmeDto.idGenero())
        );
        //TODO: RESOLVER O BUG SE UM ID DE ATOR NÃO FOR ENCONTRADO
        List<Ator> listaDeAtores = atorRepository.findAllById(filmeDto.idAtores());
        Filme filme = new Filme();
        filme.setNome(filmeDto.nome());
        filme.setAssistido(filmeDto.assistido());
        filme.setGenero(genero);
        filme.setAtores(listaDeAtores);
        //TODO: RETORNAR DTO DE ATORES E GENEROS PARA NÃO EXPOR AS ENTIDADES DIRETAMENTE E DAR DADOS MAIS PERSONALIZADOS DE RETORNO
        Filme filmeDeRetorno = filmeRepository.save(filme);
        return new FilmeDto(filmeDeRetorno.getId(), filmeDeRetorno.getNome(), filmeDeRetorno.getAssistido(), filmeDeRetorno.getGenero().getId(), filmeDeRetorno.getAtores().stream().map(Ator::getId).toList());
    }
    //Método que vai atualizar tudo
    public FilmeDto alterarFilme(FilmeDto filmeDto)
    {
        if (filmeDto.id() == null)
        {
            throw new IllegalArgumentException("Id para alteração não pode ser nulo");

        }
        Filme filmeExistente = filmeRepository.findById(filmeDto.id()).orElseThrow(
                () -> new FilmeNotFoundException("Filme não encontrado com esse ID")
        );
        Genero genero = generoRepository.findById(filmeDto.idGenero()).orElseThrow(
                () -> new GeneroNotFoundException("Gênero não encontrado com esse id")
        );
        List<Ator> atores = atorRepository.findAllById(filmeDto.idAtores());
        filmeExistente.setNome(filmeDto.nome());
        filmeExistente.setAssistido(filmeDto.assistido());
        filmeExistente.setGenero(genero);
        filmeExistente.setAtores(atores);
        filmeRepository.save(filmeExistente);
        return new FilmeDto(filmeExistente.getId(), filmeExistente.getNome(), filmeExistente.getAssistido(), filmeExistente.getGenero().getId(), filmeExistente.getAtores().stream().map(Ator::getId).toList());
    }
    public void deletarFilme(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id para deletar não pode ser nula");
        }
        Filme f = filmeRepository.findById(id).orElseThrow(
                () -> new FilmeNotFoundException("Filme não encontrado com esse id")
        );
        filmeRepository.delete(f);
    }
    @Transactional
    public FilmeDto atualizarFilme(FilmePatchDTO dtoPatch)
    {       Genero g = new Genero();

            Filme f = filmeRepository.findById(dtoPatch.id()).orElseThrow(
                    () -> new FilmeNotFoundException("Filme não encontrado com esse id")
            );
            if (dtoPatch.idGenero() != null) {
                 g = generoRepository.findById(dtoPatch.idGenero()).orElseThrow(
                        () -> new GeneroNotFoundException("Genero não encontrado com esse id")
                );
            }
            //e se nao achar um ator?
            if (!dtoPatch.idAtores().isEmpty()) {
                 List<Ator> atores = atorRepository.findAllById(dtoPatch.idAtores());
                 if (a)

            }
            if (!dtoPatch.nome().isBlank() && !f.getNome().equals(dtoPatch.nome()))
            {
                f.setNome(dtoPatch.nome());
            }
            if (dtoPatch.assistido() != null && f.getAssistido() != dtoPatch.assistido())
            {
                f.setAssistido(dtoPatch.assistido());
            }
            if (dtoPatch.idGenero() != null && !Objects.equals(f.getGenero().getId(), dtoPatch.idGenero()))
            {
                f.setGenero(g);
            }

    }

}
