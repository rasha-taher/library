package org.example.utils.RefreshToken;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.example.User.User;
import org.example.utils.JwtService;
import org.example.utils.TokenHasher;
import org.example.utils.exceptions.InvalidRefreshTokenException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public String createRefreshToken(User user) {
        String raw = jwtService.generateRefreshToken(user);

        RefreshToken entity = RefreshToken.builder()
                .token(TokenHasher.sha256(raw))
                .user(user)
                .expiryDate(jwtService.extractExpiration(raw))
                .build();

        refreshTokenRepository.save(entity);
        return raw;
    }

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public TokenResponse refresh(String rawToken) {

        // 1. Validate the JWT itself (signature, expiry, type)
        String email;
        try {
            email = jwtService.extractEmail(rawToken);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidRefreshTokenException();
        }

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new InvalidRefreshTokenException();
        }

        if (!jwtService.isTokenValid(rawToken, userDetails, JwtService.REFRESH)) {
            throw new InvalidRefreshTokenException();
        }

        // 2. Find the row by the hash of the incoming token
        RefreshToken stored = refreshTokenRepository
                .findByToken(TokenHasher.sha256(rawToken))
                .orElseThrow(InvalidRefreshTokenException::new);

        // 3. Reuse detection: an already-used token came back, so revoke the whole family
        if (stored.isRevoked()) {
            refreshTokenRepository.revokeAllByUserId(stored.getUser().getId());
            throw new InvalidRefreshTokenException();
        }

        // 4. Expired row
        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException();
        }

        // 5. Rotate: retire the old token, issue a new pair
        stored.setRevoked(true);
        User user = stored.getUser();

        String newRefresh = createRefreshToken(user);
        String newAccess = jwtService.generateAccessToken(user);

        return new TokenResponse(newAccess, newRefresh);
    }

    @Transactional
    public void logout(String rawToken) {
        refreshTokenRepository.findByToken(TokenHasher.sha256(rawToken))
                .ifPresent(t -> t.setRevoked(true));
    }
}
