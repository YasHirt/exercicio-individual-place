package com.placeti.projetoExercicioIndividual.repository;

import com.placeti.projetoExercicioIndividual.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariorRepository extends JpaRepository<Usuario, Long> {
    Optional<UserDetails> findUsuarioByEmail(String email);
}
