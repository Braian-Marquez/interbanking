package com.interbanking.autentication.domain.port.in;

import com.interbanking.commons.models.entity.UserEntity;

public interface ValidateTokenUseCase {
    TokenValidationResult execute(String token);

    class TokenValidationResult {
        private final boolean valid;
        private final UserEntity user;

        public TokenValidationResult(boolean valid, UserEntity user) {
            this.valid = valid;
            this.user = user;
        }

        public boolean isValid() {
            return valid;
        }

        public UserEntity getUser() {
            return user;
        }
    }
}