package dev.carlosmoises.projeto.enferm.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.carlosmoises.projeto.enferm.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    public String generateToken(User user) {
        String passwordSecret = System.getenv("PASSWORD_SECRET");
        Algorithm algorithm = Algorithm.HMAC256(passwordSecret);

        Instant tokenNow = Instant.now();
        Instant tokenExpired = tokenNow.plus(2, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer("API Enfermagem")
                .withSubject(user.getEmail())
                .withExpiresAt(tokenExpired)
                .sign(algorithm);
    }
}
