package com.vraj.library.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vraj.library.model.ApiError;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
@RestController
public class ControllerAdviser extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
		ApiError apiError = ApiError.builder()
							.errorCode(HttpStatus.NOT_FOUND.value())
							.message(ex.getMessage())
							.timeStamp(LocalDateTime.now())
							.build();
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserAlreadyPresentException.class)
	public ResponseEntity<ApiError> handleUserAlreadyPresentException(UserAlreadyPresentException ex) {
		ApiError apiError = ApiError.builder()
				.errorCode(HttpStatus.CONFLICT.value())
				.message(ex.getMessage())
				.timeStamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(SignatureException.class)
	public ApiError handleSignatureException() {
		ApiError apiError = ApiError.builder()
							.errorCode(HttpStatus.FORBIDDEN.value())
							.message("Unauthorised Access")
							.timeStamp(LocalDateTime.now())
							.build();
		return apiError;
	}
}
