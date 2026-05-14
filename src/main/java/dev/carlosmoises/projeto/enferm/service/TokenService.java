package dev.carlosmoises.projeto.enferm.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import dev.carlosmoises.projeto.enferm.config.TokenExpiredException;
import dev.carlosmoises.projeto.enferm.model.RefreshToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        var refreshToken = new RefreshToken();
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(14));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException("Token de acesso expirado. Faça login novamente.");
        }
        return token;
    }
}
