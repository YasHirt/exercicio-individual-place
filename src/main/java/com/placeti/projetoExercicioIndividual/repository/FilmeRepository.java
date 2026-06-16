package com.placeti.projetoExercicioIndividual.repository;


import com.placeti.projetoExercicioIndividual.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
}
