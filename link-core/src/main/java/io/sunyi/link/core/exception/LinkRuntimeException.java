package io.sunyi.link.core.exception;

public class LinkRuntimeException extends RuntimeException {

	public LinkRuntimeException() {
	}

	public LinkRuntimeException(String message) {
		super(message);
	}

	public LinkRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public LinkRuntimeException(Throwable cause) {
		super(cause);
	}

}