package io.sunyi.link.core.invocation;

/**
 * @author sunyi
 */
public class InvocationConfig<T> {

	/**
	 * 接口
	 */
	private Class<T> interfaceClass;

	/**
	 * 请求、响应超时时间
	 */
	private Long timeout = 1000L;

	/**
	 * 重试次数
	 */
	private int retryTimes = 0;

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Long getTimeout() {
		return timeout;
	}

	/**
	 * 请求、响应超时时间，单位毫秒
	 *
	 * @param timeout
	 */
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
}
