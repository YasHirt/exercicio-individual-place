package com.placeti.projetoExercicioIndividual.services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.placeti.projetoExercicioIndividual.dto.JWTUserData;
import com.placeti.projetoExercicioIndividual.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    //Fábrica de token
    public String generateToken(Usuario usuario)
    {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("id", usuario.getId())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(7200))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }
    //Retorna um Optional para evitar problemas com null caso o token seja inválido, expirado, adulterado...
    public Optional<JWTUserData> validateToken(String token) //Recebe o token sem o Bearer
        {
        try{ //Validar um JWT pode lançar exceções como assinatura inválida
             //expirado
             //formato incorreto
            Algorithm algorithm = Algorithm.HMAC256(secret); //"Vou verificar esse token usando HMAC256."
            DecodedJWT decode = JWT.require(algorithm)//"Quero verificar um token usando esse algoritmo."
                    .build() //Cria um objeto verificador.
                    .verify(token); //verifica: Assinatura, algoritmo, token malformado, expiração, etc
            return Optional.of(new JWTUserData(decode.getClaim("userId").asLong(),decode.getSubject()));
        }catch (JWTVerificationException ex)
        {
            return Optional.empty();
        }
    }
}
