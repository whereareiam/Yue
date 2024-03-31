package com.aeritt.yue.api.exception;

public class InvalidTokenException extends RuntimeException {
	public InvalidTokenException(String message, Throwable cause) {
		super(message, cause);
		System.exit(1);
	}
}
