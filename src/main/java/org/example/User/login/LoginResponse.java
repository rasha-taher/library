package org.example.User.login;

import org.example.User.RoleEnum;

public record LoginResponse(Long id, String email, RoleEnum role, String accessToken , String refreshToken) {}
