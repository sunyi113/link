package io.sunyi.link.core.exception;

public class LinkException extends RuntimeException {

	/**
	 * 不确定的异常
	 */
	public static final int UNKNOWN_ERROR = 0;

	/**
	 * 发送消息超时异常
	 */
	public static final int SEND_TIMEOUT_ERROR = 1;

	/**
	 * 等待响应超时异常
	 */
	public static final int TIMEOUT_ERROR = 2;


	private int code = UNKNOWN_ERROR;

	public LinkException() {
	}

	public LinkException(int code) {
		this.code = code;
	}

	public LinkException(String message) {
		super(message);
	}

	public LinkException(int code, String message) {
		super(message);
		this.code = code;
	}

	public LinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public LinkException(Throwable cause) {
		super(cause);
	}

}