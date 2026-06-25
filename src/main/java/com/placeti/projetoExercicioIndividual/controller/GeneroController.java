package com.placeti.projetoExercicioIndividual.controller;
import com.placeti.projetoExercicioIndividual.dto.GeneroDto;
import com.placeti.projetoExercicioIndividual.services.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO: Melhorar as dtos de resposta
@RestController
@RequestMapping("/generos")
public class GeneroController {
        private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @GetMapping()
    public ResponseEntity<List<GeneroDto>> ListarGenero()
    {
         List<GeneroDto> generoDtos = generoService.listarGeneros();
         return ResponseEntity.status(HttpStatus.OK).body(generoDtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GeneroDto> listarGenerosPorId(@PathVariable Long id)
    {
        GeneroDto generoDto = generoService.listarGeneroPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(generoDto);
    }
    @PostMapping()
    public ResponseEntity<GeneroDto> incluirGenero(@Valid @RequestBody GeneroDto generoDto)
    {
        GeneroDto generoDto1 = generoService.incluirGenero(generoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(generoDto1);
    }
}
