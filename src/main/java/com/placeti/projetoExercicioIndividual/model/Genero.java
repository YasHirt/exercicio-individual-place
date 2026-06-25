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
    // REVISÃO: @ToString.Exclude evita loop infinito no toString, mas o @Data também gera
    // equals() e hashCode() usando esse campo. Acessar filmes dentro de equals/hashCode
    // pode disparar uma consulta extra ou causar LazyInitializationException fora de transação.
    private List<Filme> filmes;

}
