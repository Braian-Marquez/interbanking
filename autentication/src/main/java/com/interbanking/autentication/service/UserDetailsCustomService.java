package com.interbanking.autentication.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import com.interbanking.autentication.models.repository.CustomerRepository;
import com.interbanking.autentication.models.repository.RoleRepository;
import com.interbanking.autentication.models.repository.UserRepository;
import com.interbanking.autentication.models.request.AuthenticationRequest;
import com.interbanking.autentication.models.request.LoginRequest;
import com.interbanking.autentication.models.response.UserResponse;
import com.interbanking.autentication.security.JwtService;
import com.interbanking.autentication.utils.Messenger;
import com.interbanking.commons.exception.InvalidCredentialsException;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.commons.models.entity.Customer;
import com.interbanking.commons.models.entity.UserEntity;
import com.interbanking.commons.models.enums.CodeEnum;
import com.interbanking.commons.models.enums.RoleType;

@Service
@Validated
@RequiredArgsConstructor
public class UserDetailsCustomService {

	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final Messenger messenger;
	private final JwtService jwtService;

	public UserEntity login(LoginRequest request) throws AuthenticationException, InvalidCredentialsException {
		UserEntity user = userRepository.findByEmailOrUsername(request.getEmail(), request.getPassword())
				.orElseThrow(() -> new InvalidCredentialsException(messenger.getMessage(CodeEnum.USERNAME_NOT_FOUND)));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException(messenger.getMessage(CodeEnum.WRONG_PASSWORD));
		}
		return user;
	}

@Transactional
public ResponseEntity<?> saveCustomer(AuthenticationRequest userRequest) throws IOException, InvalidCredentialsException {
    try {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userRequest.getEmail());

        if (existingUser.isPresent()) {
            throw new NotFoundException("El usuario ya existe.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(userRequest.getEmail());
        newUser.setUsername(userRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setRoles(new ArrayList<>(List.of(roleRepository.findByName(RoleType.USER.getFullRoleName()))));

        userRepository.save(newUser);

		Customer entity = new Customer();
		entity.setIdUser(newUser.getId());
		entity.setFirstName(formatName(userRequest.getFirstName()));
		entity.setLastName(formatName(userRequest.getLastName()));
		customerRepository.save(entity);

		UserResponse result = new UserResponse();
		result.setIdUser(newUser.getId());
		result.setIdProfile(entity.getId());
		result.setToken(jwtService.generateToken(newUser));

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);

	} catch (DataIntegrityViolationException e) {
        throw new NotFoundException("El usuario ya existe.");
    }
}

	private String formatName(String name) {
		if (name == null || name.isEmpty()) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

}
