package com.placeti.projetoExercicioIndividual.services;

import com.placeti.projetoExercicioIndividual.dto.RegisterUserRequest;
import com.placeti.projetoExercicioIndividual.dto.RegisterUserResponse;
import com.placeti.projetoExercicioIndividual.model.Usuario;
import com.placeti.projetoExercicioIndividual.repository.UsuariorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private final UsuariorRepository usuariorRepository;

    public AuthService(UsuariorRepository usuariorRepository) {
        this.usuariorRepository = usuariorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuariorRepository.findUsuarioByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
    }
    public RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest)
    {
        Usuario u = new Usuario();
        u.setEmail(registerUserRequest.email());
        u.setName(u.getName());
        u.setPassword();
    }
}
