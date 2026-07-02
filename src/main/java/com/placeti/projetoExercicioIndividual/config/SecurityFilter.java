package com.placeti.projetoExercicioIndividual.config;

import com.placeti.projetoExercicioIndividual.dto.JWTUserData;
import com.placeti.projetoExercicioIndividual.services.CustomUserDetailsService;
import com.placeti.projetoExercicioIndividual.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//Criar um filtro que execute antes dos controllers.
// No filtro, extrair o Bearer Token.
@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

   /* Pedir ao TokenService para validar o token e obter o identificador do usuário.
    Usar o loadUserByUsername() para buscar o usuário.
    Criar o objeto de autenticação.
    Registrar essa autenticação no contexto do Spring.-> SecurityContextHolder*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizedHeader = request.getHeader("Authorization");

        if (Strings.isNotEmpty(authorizedHeader) && authorizedHeader.startsWith("Bearer"))
        {
            String token = authorizedHeader.substring("Bearer ".length());
            Optional<JWTUserData> optUser = tokenService.validateToken(token);
            if (optUser.isPresent())
            {
                JWTUserData userData = optUser.get();
                UserDetails u;
                try {
                    u = customUserDetailsService.loadUserByUsername(userData.email());
                } catch (UsernameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication); // Registrar essa autenticação no contexto do Spring.-> SecurityContextHolder
            }
        }
        filterChain.doFilter(request, response); //Impede que a requisição morra no meu filtro e faz com que ela chegue até o controller

    }
}
