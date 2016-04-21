package io.sunyi.link.core.body;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RPC 调用时的请求报文对象
 */
public class RpcRequest implements Serializable {

	private static final AtomicLong gid = new AtomicLong(Long.MIN_VALUE);

	/**
	 * 一个 PRC 请求的 ID ，需要保证每个 PRC 请求的 ID 都不一样
	 */
	private Long id;

	/**
	 * 接口的 Class
	 */
	private Class interfaceClass;
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 参数类型
	 */
	private Class<?>[] parameterTypes;
	private Object[] params;
	private Map<String, String> attachments = new ConcurrentHashMap<String, String>();

	public RpcRequest() {
		id = gid.getAndIncrement();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Class getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
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

}
