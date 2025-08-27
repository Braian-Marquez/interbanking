package com.interbanking.autentication.application.service;

import com.interbanking.autentication.domain.port.in.AuthenticateUserUseCase.*;
import com.interbanking.autentication.domain.port.in.CreateUserUseCase.*;
import com.interbanking.autentication.domain.port.in.ValidateTokenUseCase.*;
import com.interbanking.autentication.domain.port.out.PasswordService;
import com.interbanking.autentication.domain.port.out.RoleRepository;
import com.interbanking.autentication.domain.port.out.TokenService;
import com.interbanking.autentication.domain.port.out.UserRepository;
import com.interbanking.autentication.infrastructure.adapter.in.web.response.AuthenticationResponse;
import com.interbanking.autentication.infrastructure.adapter.out.persistence.CustomerRepository;
import com.interbanking.commons.exception.InvalidCredentialsException;
import com.interbanking.commons.exception.NotFoundException;
import com.interbanking.commons.models.entity.Customer;
import com.interbanking.commons.models.entity.Role;
import com.interbanking.commons.models.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private PasswordService passwordService;
    
    @Mock
    private TokenService tokenService;
    
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private Role testRole;
    private CreateUserCommand createUserCommand;
    private AuthenticationCommand authCommand;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("ROLE_USER");

        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRoles(List.of(testRole));

        createUserCommand = CreateUserCommand.builder()
            .email("test@example.com")
            .password("password123")
            .firstName("John")
            .lastName("Doe")
            .build();

        authCommand = AuthenticationCommand.builder()
            .username("test@example.com")
            .password("password123")
            .build();
    }

    @Test
    void execute_CreateUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordService.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());
        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token123");

        AuthenticationResponse response = userService.execute(createUserCommand);

        assertNotNull(response);
        assertEquals("token123", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("test@example.com", response.getEmail());
        
        verify(userRepository).save(any(UserEntity.class));
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void execute_CreateUser_UserAlreadyExists_ThrowsException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(InvalidCredentialsException.class, 
            () -> userService.execute(createUserCommand));
        
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void execute_AuthenticateUser_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordService.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any(UserEntity.class))).thenReturn("token123");

        AuthenticationResult result = userService.execute(authCommand);

        assertNotNull(result);
        assertEquals("token123", result.getToken());
        assertEquals(1L, result.getUserId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void execute_AuthenticateUser_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, 
            () -> userService.execute(authCommand));
    }

    @Test
    void execute_AuthenticateUser_InvalidPassword_ThrowsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordService.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, 
            () -> userService.execute(authCommand));
    }



    @Test
    void execute_ValidateToken_ValidToken_ReturnsTrue() {
        when(tokenService.validateToken(anyString())).thenReturn(true);
        when(tokenService.getUserFromToken(anyString())).thenReturn(testUser);

        TokenValidationResult result = userService.execute("validToken");

        assertTrue(result.isValid());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void execute_ValidateToken_InvalidToken_ReturnsFalse() {
        when(tokenService.validateToken(anyString())).thenReturn(false);

        TokenValidationResult result = userService.execute("invalidToken");

        assertFalse(result.isValid());
        assertNull(result.getUser());
    }

    @Test
    void execute_ValidateToken_ExceptionThrown_ReturnsFalse() {
        when(tokenService.validateToken(anyString())).thenThrow(new RuntimeException("Token error"));

        TokenValidationResult result = userService.execute("errorToken");

        assertFalse(result.isValid());
        assertNull(result.getUser());
    }

    @Test
    void getById_UserExists_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserEntity result = userService.getById(1L);

        assertEquals(testUser, result);
    }

    @Test
    void getById_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, 
            () -> userService.getById(1L));
    }

    @Test
    void getByUsername_UserExists_ReturnsUser() {
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(testUser));

        UserEntity result = userService.getByUsername("test@example.com");

        assertEquals(testUser, result);
    }

    @Test
    void getByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, 
            () -> userService.getByUsername("test@example.com"));
    }

    @Test
    void getByEmail_UserExists_ReturnsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserEntity result = userService.getByEmail("test@example.com");

        assertEquals(testUser, result);
    }

    @Test
    void getByEmail_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, 
            () -> userService.getByEmail("test@example.com"));
    }

    @Test
    void getAll_ReturnsAllUsers() {
        List<UserEntity> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAll();

        assertEquals(users, result);
        assertEquals(1, result.size());
    }
}