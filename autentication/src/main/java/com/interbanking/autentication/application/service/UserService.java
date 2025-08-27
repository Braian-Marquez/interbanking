package com.interbanking.autentication.application.service;

import com.interbanking.autentication.domain.port.in.AuthenticateUserUseCase;
import com.interbanking.autentication.domain.port.in.CreateUserUseCase;
import com.interbanking.autentication.domain.port.in.GetUserUseCase;
import com.interbanking.autentication.domain.port.in.ValidateTokenUseCase;
import com.interbanking.autentication.domain.port.out.PasswordService;
import com.interbanking.autentication.domain.port.out.RoleRepository;
import com.interbanking.autentication.domain.port.out.TokenService;
import com.interbanking.autentication.domain.port.out.UserRepository;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.AuthenticationResponse;
import com.interbanking.autentication.infrastructure.adapter.out.persistence.CustomerRepository;
import com.interbanking.commons.exception.InvalidCredentialsException;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.commons.models.entity.Customer;
import com.interbanking.commons.models.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, AuthenticateUserUseCase, ValidateTokenUseCase, GetUserUseCase {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final CustomerRepository customerRepository;

    @Override
    public AuthenticationResponse execute(CreateUserCommand command) {
        String email = command.getEmail();
        
        if (userRepository.existsByEmail(email)) {
            throw new InvalidCredentialsException("User with email " + email + " already exists");
        }

        String encodedPassword = passwordService.encode(command.getPassword());
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(encodedPassword);
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new IllegalArgumentException("Role not found: ROLE_USER"))));
        UserEntity savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setFirstName(command.getFirstName());
        customer.setLastName(command.getLastName());
        customer.setIdUser(savedUser.getId());
        customerRepository.save(customer);

        String token = tokenService.generateToken(savedUser);

        return AuthenticationResponse.builder()
            .token(token)
            .userId(savedUser.getId())
            .email(savedUser.getEmail())
            .build();
    }

    @Override
    public AuthenticationResult execute(AuthenticationCommand command) {
        UserEntity user = userRepository.findByUsername(command.getUsername())
            .orElseThrow(() -> new NotFoundException("User not found: " + command.getUsername()));

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("User account is disabled");
        }

        if (!passwordService.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = tokenService.generateToken(user);
        return new AuthenticationResult(token, user.getId(), user.getEmail());
    }

    @Override
    public TokenValidationResult execute(String token) {
        try {
            if (tokenService.validateToken(token)) {
                UserEntity user = tokenService.getUserFromToken(token);
                return new TokenValidationResult(true, user);
            }
            return new TokenValidationResult(false, null);
        } catch (Exception e) {
            return new TokenValidationResult(false, null);
        }
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
}