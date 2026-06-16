package com.placeti.projetoExercicioIndividual.controller;

import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
import com.placeti.projetoExercicioIndividual.services.FilmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
    public final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<FilmeDto> buscarPeloId(@PathVariable("id") Long id)
    {
        FilmeDto filmeDto = filmeService.pesquisarFilme(id);
        return ResponseEntity.ok(filmeDto);
    }
    @GetMapping("/filmes")
    public ResponseEntity<List<FilmeDto>> buscarFilmes()
    {
        return ResponseEntity.ok().body(filmeService.listarFilmes());
    }
}
