package com.interbanking.autentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interbanking.autentication.models.repository.UserRepository;
import com.interbanking.autentication.models.response.TokenValidationResponse;
import com.interbanking.autentication.models.response.UserProfileResponse;
import com.interbanking.autentication.security.JwtService;
import com.interbanking.autentication.utils.Messenger;
import com.interbanking.commons.exception.InvalidCredentialsException;
import com.interbanking.commons.models.enums.CodeEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/public/v1")
public class AuthorizedController {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final Messenger messenger;
	private Logger logger = LoggerFactory.getLogger(AuthorizedController.class);
	

	@GetMapping(value = "/validate-token")
	public ResponseEntity<?> validateAuthToken(@RequestHeader("Authorization") String authHeader) {
	    try {
	        if (authHeader != null && !authHeader.isEmpty()) {
				System.out.println("authHeader: " + authHeader);
	            String jwtToken = authHeader.replace("Bearer ", "").trim();
	            String username = jwtService.extractUsername(jwtToken);
	            UserProfileResponse userProfile = userRepository.findUserProfileByUsername(username).orElseThrow(() -> new InvalidCredentialsException("El usuario no existe"));
				System.out.println("userProfile: " + userProfile.getRoles().get(0));
				System.out.println("userProfile: " + userProfile.getIdCustomer());
				System.out.println("userProfile: " + userProfile.getIdUser());
				System.out.println("userProfile: " + userProfile.getName());
	            TokenValidationResponse validationResponse = new TokenValidationResponse();
	            validationResponse.setId(userProfile.getIdCustomer());
	            validationResponse.setRoles(userProfile.getRoles());
	            return ResponseEntity.status(HttpStatus.ACCEPTED).body(validationResponse);
	        } else {
	            throw new InvalidCredentialsException(messenger.getMessage(CodeEnum.INVALID_CREDENTIALS));
	        }
	    } catch (ExpiredJwtException ex) {
	        String errorMsg = "The provided token has expired.";
	        logger.error("Token expired: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	    } catch (JwtException ex) {
	        String errorMsg = "The token provided is invalid.";
	        logger.error("Invalid token: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
	    } catch (Exception e) {
	        String errorMsg = "An unexpected error occurred.";
	        logger.error("Exception: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
	    }
	}



	
}
