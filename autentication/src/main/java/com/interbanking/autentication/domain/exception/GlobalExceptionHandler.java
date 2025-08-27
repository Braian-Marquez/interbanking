package com.interbanking.autentication.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.interbanking.commons.exception.ErrorResponse;
import com.interbanking.commons.exception.ErrorDto;
import com.interbanking.commons.exception.InvalidCredentialsException;
import com.interbanking.commons.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorDto> handleInvalidCredentialsException(InvalidCredentialsException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException e) {
		ErrorDto response = new ErrorDto();
		ErrorResponse error = buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
		response.setError(error);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			String errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});

		ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación");
		errorResponse.setDescription(errors);

		ErrorDto response = new ErrorDto();
		response.setError(errorResponse);

		return ResponseEntity.badRequest().body(response);
	}

	private ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message) {
		ErrorResponse error = new ErrorResponse();
		error.setHttpStatusCode(httpStatus.value());
		error.getDescription().add(message);
		error.setTimestamp(LocalDateTime.now());
		return error;
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
	    String errorMessage = "Token has expired. Please reauthenticate or obtain a new token.";
	    logger.error("Token expired: {}", ex.getMessage());
	    Date expirationDate = ex.getClaims().getExpiration();
	    errorMessage += " Token expired at: " + expirationDate;
	    ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED, errorMessage);
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
} 