package com.example.gradleAdminApi.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchElementException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String MESSAGE = "There is no data.";

    public NoSuchElementException() {
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
