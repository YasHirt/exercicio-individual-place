package com.placeti.projetoExercicioIndividual.controller;

import com.placeti.projetoExercicioIndividual.dto.AtorDto;
import com.placeti.projetoExercicioIndividual.services.AtorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atores")
public class AtorController {
    private final AtorService atorService;

    public AtorController( AtorService atorService) {
        this.atorService = atorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtorDto> buscarAtorPorId(@PathVariable Long id)
    {
        AtorDto atorDto = atorService.buscarAtorPorId(id);
        return ResponseEntity.ok().body(atorDto);
    }
    @GetMapping()
    public ResponseEntity<List<AtorDto>> buscarAtores()
    {
        List<AtorDto> atores = atorService.buscarAtores();
        return ResponseEntity.ok().body(atores);
    }
    @PostMapping
    public ResponseEntity<AtorDto> incluirAtor(@RequestBody @Valid AtorDto atorDto)
    {
       AtorDto atorDto1 =  atorService.incluirAtor(atorDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(atorDto1);
    }
}
