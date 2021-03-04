package com.diefesson.filesync.io;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class AuthException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

}
