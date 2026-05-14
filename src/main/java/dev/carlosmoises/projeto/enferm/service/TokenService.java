package dev.carlosmoises.projeto.enferm.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.carlosmoises.projeto.enferm.model.RefreshToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public String generateToken(User user) {
        String passwordSecret = System.getenv("PASSWORD_SECRET");
        Algorithm algorithm = Algorithm.HMAC256(passwordSecret);

        Instant tokenNow = Instant.now();
        Instant tokenExpired = tokenNow.plus(2, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer("API EnfermeirEX")
                .withSubject(user.getEmail())
                .withExpiresAt(tokenExpired)
                .sign(algorithm);
    }

    public String getSubject(String tokenJWT) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(System.getenv("PASSWORD_SECRET"));

            return JWT.require(algorithm)
                    .withIssuer("API EnfermeirEX")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (Exception exception) {
            throw new RuntimeException("Token JWT inválido ou expirado");
        }
    }

    public RefreshTokenRepository createRefreshToken(User user) {
        var refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setId(user.getId());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(14));

        return refreshTokenRepository.save(refreshToken);
    }
}
