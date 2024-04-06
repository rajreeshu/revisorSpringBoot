package com.revise.leetcode.leetcodeRevision.Exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.revise.leetcode.leetcodeRevision.dto.ErrorResponse;

@ControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<String, String>();
		 ex.getBindingResult().getAllErrors().forEach(error -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errorMap.put(fieldName, errorMessage);
		    });
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.displayMessage(ex.getLocalizedMessage())
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.error(status.getReasonPhrase())
				.errorMessages(errorMap)
				.build();
		
//        ErrorResponse errorResponse = ErrorResponse.builder().build();

        return new ResponseEntity<>(errorResponse, status);
    }
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException ex) {
		Map<String, String> errorMap = new HashMap<String, String>();
		 errorMap.put("Message","Invalid Credentials.");
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.displayMessage(ex.getLocalizedMessage())
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.error(status.getReasonPhrase())
				.errorMessages(errorMap)
				.build();

        return new ResponseEntity<>(errorResponse, status);
    }
	
	@ExceptionHandler(CustomUserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> customUserAlreadyExistsException(CustomUserAlreadyExistsException ex) {
		Map<String, String> errorMap = new HashMap<String, String>();
		 errorMap.put("Message",ex.getMessage());
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.displayMessage(ex.getLocalizedMessage())
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.error(status.getReasonPhrase())
				.errorMessages(errorMap)
				.build();

        return new ResponseEntity<>(errorResponse, status);
    }
	
	
	 @ExceptionHandler(Exception.class)
	    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
	        ErrorResponse errorResponse = ErrorResponse.builder()
	                .timestamp(LocalDateTime.now())
	                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
	                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())  
	                .displayMessage("An unexpected error occurred. We're working to fix the issue.")
	                // Optionally, you can include more details about the error
	                // .detail(ex.toString()) // Be cautious about exposing sensitive error details
	                .build();

	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
}
