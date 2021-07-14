package com.example.gradleAdminApi.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MESSAGE = "Login Error";

	public LoginException() {
		super(MESSAGE);
		log.error(MESSAGE);
	}
}
