package org.example.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.User.login.LoginRequest;
import org.example.User.login.LoginResponse;
import org.example.dtos.UserCreationDTO;
import org.example.utils.RefreshToken.RefreshRequest;
import org.example.utils.RefreshToken.RefreshTokenService;
import org.example.utils.RefreshToken.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login")
    public LoginResponse login (@RequestBody LoginRequest request){
        return userService.login(request);
    }

    @PostMapping("/signup")
    public Long createUser(@Valid @RequestBody UserCreationDTO dto){
        return userService.createUser(dto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(refreshTokenService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        refreshTokenService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
