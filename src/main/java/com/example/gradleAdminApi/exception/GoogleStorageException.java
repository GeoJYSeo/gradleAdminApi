package com.example.gradleAdminApi.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleStorageException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String MESSAGE = "Google Storage Error.";

	public GoogleStorageException() {
		super(MESSAGE);
		log.error(MESSAGE);
	}
}
