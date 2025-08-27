package com.interbanking.autentication.infrastructure.adapter.in.web;

import com.interbanking.autentication.domain.port.in.AuthenticateUserUseCase;
import com.interbanking.autentication.domain.port.in.CreateUserUseCase;
import com.interbanking.autentication.domain.port.in.ValidateTokenUseCase;
import com.interbanking.autentication.infrastructure.adapter.in.web.request.AuthenticationRequest;
import com.interbanking.autentication.infrastructure.adapter.in.web.request.CreateUserRequest;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.AuthenticationResponse;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    


    @PostMapping("/users")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody CreateUserRequest request) {
            CreateUserUseCase.CreateUserCommand command = CreateUserUseCase.CreateUserCommand.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
            AuthenticationResponse response = createUserUseCase.execute(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticateUserUseCase.AuthenticationCommand command = 
            new AuthenticateUserUseCase.AuthenticationCommand(request.getUsername(), request.getPassword());
        
        AuthenticateUserUseCase.AuthenticationResult result = authenticateUserUseCase.execute(command);
        AuthenticationResponse response = new AuthenticationResponse(
            result.getToken(),
            result.getUserId(),
            result.getEmail()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String token = authHeader.substring(7);
            ValidateTokenUseCase.TokenValidationResult result = validateTokenUseCase.execute(token);
            if (!result.isValid()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            TokenValidationResponse response = TokenValidationResponse.from(result.getUser());
            return ResponseEntity.ok(response);
    }
}