package com.tongbanjie.link.core.body;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sunyi on 15/9/21.
 */
public class RpcResponse implements Serializable {

	private Long id;
	private Object result;
	private Map<String, String> attachments = new ConcurrentHashMap<String, String>();
	private boolean hasException;
	private Throwable exception;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isHasException() {
		return hasException;
	}

	public void setHasException(boolean hasException) {
		this.hasException = hasException;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public void addAttachment(String key, String value) {
		attachments.put(key, value);
	}

	public String getAttachement(String key) {
		return attachments.get(key);
	}

	public String getAttachement(String key, String defaultValue) {
		String value = attachments.get(key);
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	/**
	 * 重现当时的结果，如果有异常则抛出异常，没有则返回结果。
	 *
	 * @return
	 * @throws Throwable
	 */
	public Object recur() throws Throwable {
		if (hasException && exception != null) {
			throw exception;
		}
		return result;
	}

}
