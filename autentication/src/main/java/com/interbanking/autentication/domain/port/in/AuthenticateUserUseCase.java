package com.interbanking.autentication.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AuthenticateUserUseCase {
    AuthenticationResult execute(AuthenticationCommand command);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class AuthenticationCommand {
        private String username;
        private String password;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class AuthenticationResult {
        private String token;
        private Long userId;
        private String email;
    }
}