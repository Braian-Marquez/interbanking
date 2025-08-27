package com.interbanking.autentication.domain.port.out;

import com.interbanking.commons.models.entity.UserEntity;

public interface TokenService {
    String generateToken(UserEntity user);
    boolean validateToken(String token);
    UserEntity getUserFromToken(String token);
    String refreshToken(String token);
}