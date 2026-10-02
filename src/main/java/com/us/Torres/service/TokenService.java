package com.us.Torres.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.us.Torres.models.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret:8f4c9e7a2b6d1f3c9a8e5b7d2c4f6a1e9b3d7c5f8a2e6b4d9c1f7a5e3b8d6}")
    private String secretKey;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("cargo", user.getCargo() != null ? user.getCargo().name() : "")
                    .withClaim("nome", user.getNome())
                    .withExpiresAt(generateExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Erro ao gerar token JWT", ex);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    private Instant generateExpiration() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("+01:00"));
    }
}
