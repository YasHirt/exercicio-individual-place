package com.placeti.projetoExercicioIndividual.services;

import com.placeti.projetoExercicioIndividual.dto.LoginRequest;
import com.placeti.projetoExercicioIndividual.dto.LoginResponse;
import com.placeti.projetoExercicioIndividual.dto.RegisterUserRequest;
import com.placeti.projetoExercicioIndividual.dto.RegisterUserResponse;
import com.placeti.projetoExercicioIndividual.exceptions.EmailDuplicadoException;
import com.placeti.projetoExercicioIndividual.model.Usuario;
import com.placeti.projetoExercicioIndividual.repository.UsuariorRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UsuariorRepository usuariorRepository;
    private final TokenService tokenService;

    public AuthService(PasswordEncoder passwordEncoder, UsuariorRepository usuariorRepository, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.usuariorRepository = usuariorRepository;
        this.tokenService = tokenService;
    }
    public RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest)
    {
        Usuario u = new Usuario();
        Optional<Usuario> usuario = usuariorRepository.findUsuarioByEmail(registerUserRequest.email());
        if (usuario.isEmpty())
        {
            u.setEmail(registerUserRequest.email());
            u.setName(registerUserRequest.name());
            u.setSenha(passwordEncoder.encode(registerUserRequest.senha()));
            usuariorRepository.save(u);
            return new RegisterUserResponse("Usuário registrado");
        }
        throw new EmailDuplicadoException("Requisição ruim");
    }
    public LoginResponse login(LoginRequest loginRequest)
    {//A primeira coisa que o método precisa fazer é pegar o identificador
        // (e-mail ou username) que veio dentro do loginRequest e perguntar ao banco de dados
        // se esse usuário existe.
        Usuario usuario;
            usuario = usuariorRepository.findUsuarioByEmail(loginRequest.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha incorretos"));
        passwordEncoder.matches(loginRequest.senha(), usuario.getSenha());
        return new LoginResponse(tokenService.generateToken(usuario));

    }
}
