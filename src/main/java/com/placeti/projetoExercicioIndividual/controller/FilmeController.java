package com.placeti.projetoExercicioIndividual.controller;

import com.placeti.projetoExercicioIndividual.dto.FilmeDto;
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
        FilmeDto filmeDto = filmeService.pesquisarFilme(id);
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

    // REVISÃO: O exercício pede PATCH /filmes/{id} para atualização parcial, não PUT sem id na URL.
    // PUT é para substituição completa do recurso; PATCH é para atualização parcial.
    // Corrija para @PatchMapping("/{id}"), receba o id como @PathVariable
    // e use FilmePatchDTO no body (que representa atualização parcial).
    // O método que você implementou em FilmeService (atualizarFilme) é o que deve ser chamado aqui.
    // Também falta o endpoint PATCH conectado ao método atualizarFilme do service.
    @PutMapping()
    public ResponseEntity<FilmeDto> alterarFilmes(@Valid @RequestBody FilmeDto filmeDto)
    {
        FilmeDto FilmeDtoDeRetorno = filmeService.alterarFilme(filmeDto);
        return ResponseEntity.status(HttpStatus.OK).body(FilmeDtoDeRetorno);

    }
    //Método que deleta um filme do db
    @DeleteMapping("/{id}")
    // REVISÃO: @Valid em @PathVariable não tem efeito para validações de Bean Validation
    // (como @NotNull, @Min etc.) sem adicionar @Validated na classe.
    // Para PathVariable simples como Long id, isso não causa problema funcional agora,
    // mas o @Valid aqui é desnecessário e enganoso. Remova-o.
    public ResponseEntity<String> deletarFilme(@Valid @PathVariable Long id)
    {
        filmeService.deletarFilme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
