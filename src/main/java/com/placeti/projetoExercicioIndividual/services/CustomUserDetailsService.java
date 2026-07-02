package com.placeti.projetoExercicioIndividual.services;
import com.placeti.projetoExercicioIndividual.repository.UsuariorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuariorRepository usuariorRepository;

    public CustomUserDetailsService(UsuariorRepository usuariorRepository) {
        this.usuariorRepository = usuariorRepository;
    }
    //Necessário quando a autenticação não for manual
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuariorRepository.findUsuarioByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );
    }

}
