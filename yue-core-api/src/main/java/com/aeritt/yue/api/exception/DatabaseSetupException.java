package com.aeritt.yue.api.exception;

public class DatabaseSetupException extends RuntimeException {
	public DatabaseSetupException(String string, Throwable cause) {
		super(string, cause);
		System.exit(1);
	}
}
