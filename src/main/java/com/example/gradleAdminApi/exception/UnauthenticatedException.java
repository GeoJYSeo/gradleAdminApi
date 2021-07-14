package com.example.gradleAdminApi.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnauthenticatedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MESSAGE = "Unauthenticated User.";

	public UnauthenticatedException() {
		super(MESSAGE);
		log.error(MESSAGE);
	}
}
