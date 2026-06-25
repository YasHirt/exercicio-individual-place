package com.placeti.projetoExercicioIndividual.controller;

import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
import com.placeti.projetoExercicioIndividual.dto.FilmePatchDTO;
import com.placeti.projetoExercicioIndividual.services.FilmeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<FilmeDto> buscarPeloId(@PathVariable("id") Long id)
    {
        FilmeDto filmeDto = filmeService.pesquisarFilmePorId(id);
        return ResponseEntity.ok(filmeDto);
    }
    @GetMapping()
    public ResponseEntity<List<FilmeDto>> buscarFilmes()
    {
        return ResponseEntity.ok().body(filmeService.listarFilmes());
    }
    @PostMapping()
    public ResponseEntity<FilmeDto> incluirFilmes(@Valid @RequestBody FilmeDto filmeDto)
    {
        FilmeDto filmedtoDeRetorno = filmeService.incluirFilme(filmeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmedtoDeRetorno);
    }

    //Método que altera todos os atributos da entidade
    @PutMapping()
    public ResponseEntity<FilmeDto> alterarFilmes(@Valid @RequestBody FilmeDto filmeDto)
    {
        FilmeDto FilmeDtoDeRetorno = filmeService.alterarFilme(filmeDto);
        return ResponseEntity.status(HttpStatus.OK).body(FilmeDtoDeRetorno);

    }
    //Método que deleta um filme do db
    @DeleteMapping("/{id}")
    public ResponseEntity deletarFilme(@Valid @PathVariable Long id)
    {
        filmeService.deletarFilme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<FilmeDto> atualizarFilmes(@RequestBody @Valid FilmePatchDTO filmePatchDTO, @PathVariable Long id)
    {
        /*
        * Exemplo de Requisição:
        * {   "id": 2,
              "nome": "Matrix",
              "assistido": true,
              "idGenero": null,
              "idAtores": [1]
          }
        * O que for ser mantido pode ser null
        */
        FilmeDto filmeDto = filmeService.atualizarFilme(filmePatchDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(filmeDto);
    }

}
