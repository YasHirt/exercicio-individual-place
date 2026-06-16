package com.placeti.projetoExercicioIndividual.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="genero")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "genero", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Filme> filmes;

}
