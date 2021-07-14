package com.example.gradleAdminApi.exception.handler;

import com.example.gradleAdminApi.exception.*;
import com.example.gradleAdminApi.exception.dto.ErrorResponse;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmailNotExistedException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerEmailNotExistedException(EmailNotExistedException ex) {
		return ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public  ErrorResponse handlerNoSuchElementException(NoSuchElementException ex) {
		return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(LoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handlerLoginException(LoginException ex) {
		return ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		StringBuilder errorMessages = new StringBuilder();
		String[] splitErrorParameter = Objects.requireNonNull(ex.getParameter().getMethod()).toGenericString().split("\\(")[0].split(" ");
		errorMessages.append("{Controller_Method: ").append(splitErrorParameter[splitErrorParameter.length - 1]).append("}").append(", {Validation: ");
		ex.getBindingResult().getAllErrors().forEach(objectError ->
				errorMessages.append(Arrays.toString(objectError.toString().split("codes ")).split(",")[2]).append(", ")
		);
		return ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessages.append("}").toString().replace(", }", "}"));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerConstraintViolationException(ConstraintViolationException ex) {
		return  ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage().split(", ")[4]);
	}

	@ExceptionHandler(ServletException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handlerBadRequestException(ServletException ex) {
		return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(UnauthenticatedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handlerUnauthenticatedException(UnauthenticatedException ex) {
		return ErrorResponse.of(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(GoogleStorageException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handlerGoogleStorageException(GoogleStorageException ex) {
		return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(URISyntaxException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handlerURISyntaxException(URISyntaxException ex) {
		return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handlerIOException(IOException ex) {
		return  ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handlerRuntimeException(RuntimeException ex) {
		log.error("Server Error : {}", ex.getMessage(), ex);
		return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error.");
	}
}
