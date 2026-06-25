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
// REVISÃO: Prefira usar org.springframework.transaction.annotation.Transactional em vez de
// jakarta.transaction.Transactional. A versão do Spring oferece mais opções de configuração
// (propagation, isolation, readOnly) e é a convencional em projetos Spring Boot.
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
        // REVISÃO: f.getGenero() pode ser null se o filme foi salvo sem gênero (genero_id nullable no banco).
        // f.getGenero().getId() vai lançar NullPointerException nesse caso.
        // Verifique se genero não é null antes de chamar getId(), ou garanta que genero_id é NOT NULL no banco.
        return new FilmeDto(f.getId(), f.getNome(), f.getAssistido(), f.getGenero().getId(), f.getAtores().stream().map(Ator::getId).toList());
    }

    //Método que vai listar todos os filmes

    public List<FilmeDto> listarFilmes()
    {
        //TODO: CUSTOMIZAR ESSE ERRO
        // REVISÃO: O mesmo risco de NullPointerException existe aqui: filme.getGenero() pode ser null.
        // Corrija a constraint no banco (NOT NULL) ou trate o null no mapeamento.
        return filmeRepository.findAll().stream().map(filme -> new FilmeDto(filme.getId(), filme.getNome(), filme.getAssistido(), filme.getGenero().getId(), filme.getAtores().stream().map(Ator::getId).toList())).toList();
    }

    //Método que vai incluir os filmes no banco
    @Transactional //Garante atomicidade, princípio do tudo ou nada
    public FilmeDto incluirFilme(FilmeDto filmeDto)
    {
        // REVISÃO: Use IllegalArgumentException em vez de RuntimeException genérica.
        // Além disso, o exercício pede que a validação do campo de texto (nome em branco)
        // fique no Service, mas aqui ela está sendo feita pelo @NotBlank no DTO (nível de controller).
        // Adicione uma verificação explícita: if (filmeDto.nome() == null || filmeDto.nome().isBlank())
        // e lance uma exceção adequada com status 400.
        if (filmeDto.id() != null)
        {
            throw new RuntimeException("Id deve ser nula para criação");
        }
        Genero genero = generoRepository.findById(filmeDto.idGenero()).orElseThrow(
                () -> new GeneroNotFoundException("Genero não encontrado com esse id " + filmeDto.idGenero())
        );
        // REVISÃO: findAllById silenciosamente ignora IDs que não existem no banco.
        // Se você enviar [1, 2, 999] e o ator 999 não existir, a lista retorna só [ator1, ator2] sem erro.
        // Após a chamada, compare listaDeAtores.size() com filmeDto.idAtores().size().
        // Se forem diferentes, lance um AtorNotFoundException (depois de corrigir essa classe).
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
    // REVISÃO: alterarFilme faz uma atualização completa (PUT), mas está sem @Transactional.
    // Sem essa anotação, se save() falhar no meio do processo, as mudanças anteriores
    // no objeto filmeExistente não serão revertidas automaticamente pelo Spring.
    // Adicione @Transactional aqui igual ao incluirFilme.
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
    {
            // REVISÃO (CRÍTICO): Esse método não compila por dois motivos:
            // 1. "if (a)" na linha 117 referencia uma variável 'a' que não existe.
            //    Você provavelmente queria aplicar os atores ao filme: f.setAtores(atores).
            //    Mova a lógica de setAtores para dentro do bloco if e remova o "if (a)".
            // 2. O método tem retorno FilmeDto mas não retorna nada.
            //    Após atualizar o objeto 'f', salve com filmeRepository.save(f)
            //    e retorne um novo FilmeDto com os dados atualizados.
            //
            // REVISÃO (DESIGN): Genero g = new Genero() inicializa um Genero vazio antes da verificação null.
            // Se idGenero for null, 'g' fica como Genero vazio e pode ser usado por engano.
            // Declare como: Genero g = null; e trate o caso null no bloco abaixo.
            //
            // REVISÃO: dtoPatch.idAtores() pode ser null, e chamar .isEmpty() em null
            // lança NullPointerException. Verifique null antes: if (dtoPatch.idAtores() != null && !dtoPatch.idAtores().isEmpty())
            //
            // REVISÃO: dtoPatch.nome() pode ser null no PATCH (campo opcional).
            // Chamar .isBlank() em null lança NullPointerException.
            // Verifique: if (dtoPatch.nome() != null && !dtoPatch.nome().isBlank() && ...)
            Genero g = new Genero();

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
            // REVISÃO: falta filmeRepository.save(f) e o return aqui.

    }

    // (REVISAO) O que eu recomendaria para o método acima:
//    @Transactional
//    public FilmeDto atualizarFilme(FilmePatchDTO dto) {
//        Filme f = filmeRepository.findById(dto.id())
//                .orElseThrow(() -> new FilmeNotFoundException("Filme não encontrado com esse id"));
//
//        if (dto.idGenero() != null) {
//            Genero genero = generoRepository.findById(dto.idGenero())
//                    .orElseThrow(() -> new GeneroNotFoundException("Genero não encontrado com esse id"));
//            f.setGenero(genero);
//        }
//
//        if (dto.idAtores() != null && !dto.idAtores().isEmpty()) {
//            List<Ator> atores = atorRepository.findAllById(dto.idAtores());
//            if (atores.size() != dto.idAtores().size()) {
//                throw new AtorNotFoundException("Um ou mais atores não foram encontrados");
//            }
//            f.setAtores(atores);
//        }
//
//        if (dto.nome() != null && !dto.nome().isBlank()) {
//            f.setNome(dto.nome());
//        }
//
//        if (dto.assistido() != null) {
//            f.setAssistido(dto.assistido());
//        }
//
//        return new FilmeDto(f);
//    }
}
