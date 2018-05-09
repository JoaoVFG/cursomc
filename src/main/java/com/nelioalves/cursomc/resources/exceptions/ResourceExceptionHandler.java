package com.nelioalves.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.nelioalves.cursomc.services.exceptions.AuthorizationException;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.FileException;
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
	
	
	
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandartError> authorization(AuthorizationException error, 
														HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.FORBIDDEN.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(standartError);
		
	}
	
	
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandartError> file(FileException error, HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.BAD_REQUEST.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
		
	}
	
	
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandartError> amazonService(AmazonServiceException error, 
														HttpServletRequest request){
		
		HttpStatus httpStatus = HttpStatus.valueOf(error.getErrorCode());
		
		StandartError standartError = new StandartError(httpStatus.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(httpStatus).body(standartError);
		
	}
	
	
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandartError> amazonClient(AmazonClientException error, 
														HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.BAD_REQUEST.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
		
	}
	
	
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandartError> amazonS3(AmazonS3Exception error, 
														HttpServletRequest request){
		
		
		StandartError standartError = new StandartError(HttpStatus.BAD_REQUEST.value(), 
														error.getMessage(), 
														System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
		
	}
	
}
