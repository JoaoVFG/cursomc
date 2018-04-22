package com.nelioalves.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandartError> objectNotFound(ObjectNotFoundException error, 
														HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.NOT_FOUND.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standartError);
		
	}
	
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandartError> dataIntegrityException(DataIntegrityException error,
																HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.BAD_REQUEST.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandartError> validation(MethodArgumentNotValidException error,
										  HttpServletRequest request){
		
		ValidatationError validatationError = new ValidatationError(HttpStatus.BAD_REQUEST.value(), 
														"Erro de Validação", 
														System.currentTimeMillis());
		
		
		for(FieldError fieldError: error.getBindingResult().getFieldErrors()) {
			validatationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
		}

		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validatationError);
		
	}
	
	
}
