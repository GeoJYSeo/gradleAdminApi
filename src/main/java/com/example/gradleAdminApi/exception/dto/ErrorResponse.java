package com.example.gradleAdminApi.exception.dto;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	
	private int code;
	private String message;
	
	public static ErrorResponse of(HttpStatus httpStatus, String message) {
		return new ErrorResponse(httpStatus.value(), message);
	}
	
}
