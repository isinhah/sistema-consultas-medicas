package com.agendador.api_agendador.security;

import com.agendador.api_agendador.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("name", user.getName())
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(generateExpirationDate()))
                .sign(getAlgorithm());
    }

    public boolean isTokenValid(String token) {
        try {
            getDecodedJWT(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return Long.parseLong(decodedJWT.getSubject());
    }

    public String getUserRoleFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getClaim("role").asString();
    }

    private DecodedJWT getDecodedJWT(String token) {
        return JWT.require(getAlgorithm()).build().verify(token);
    }

    public Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusMinutes(30)
                .toInstant(ZoneOffset.UTC);
    }
}