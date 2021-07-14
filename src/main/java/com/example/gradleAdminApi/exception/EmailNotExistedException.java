package com.example.gradleAdminApi.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailNotExistedException extends RuntimeException {
	
	/**
	 * 
	 */
	public static final String MESSAGE = "Please Check your Email or Password.";
	
	public EmailNotExistedException(String userEmail) {
		super(MESSAGE);
		log.error(MESSAGE + ": " + userEmail);
	}
}
