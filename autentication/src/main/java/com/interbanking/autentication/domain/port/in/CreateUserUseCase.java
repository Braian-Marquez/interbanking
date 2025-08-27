package com.interbanking.autentication.domain.port.in;

import com.interbanking.autentication.infrastructure.adapter.in.web.response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateUserUseCase {
    AuthenticationResponse execute(CreateUserCommand command);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class CreateUserCommand {
        private String firstName;
        private String lastName;
        private String email;
        private String password;

    }
}