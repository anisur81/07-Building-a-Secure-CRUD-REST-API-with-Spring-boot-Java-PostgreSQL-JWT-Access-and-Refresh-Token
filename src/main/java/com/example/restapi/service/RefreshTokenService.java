package com.example.restapi.service;

import com.example.restapi.entity.RefreshToken;
import com.example.restapi.entity.User;
import com.example.restapi.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createRefreshToken(User user) {

        RefreshToken token = repository
                .findByUser(user)
                .orElse(new RefreshToken());

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );

        return repository.save(token);
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {

            repository.delete(refreshToken);

            throw new RuntimeException("Refresh token has expired");
        }

        return refreshToken;
    }
}