package com.businessbank.exceptioncontroller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.businessbank.exception.BankIdNotFoundException;
import com.businessbank.model.ErrorDetails;

@ControllerAdvice
public class BankExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BankIdNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String exception(BankIdNotFoundException ex) {
		return ex.getMessage();

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
				webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		// TODO Auto-generated method stub
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("timestamp", new Date());
		responseBody.put("status", status.value());

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

		List<String> listErrors = new ArrayList<>();

		for (FieldError fieldError : fieldErrors) {
			String errorMessage = fieldError.getDefaultMessage();
			listErrors.add(errorMessage);

		}
		responseBody.put("errors", listErrors);
		return new ResponseEntity<>(responseBody, headers, status);

	}
}
