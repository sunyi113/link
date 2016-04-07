package io.sunyi.link.core.server;

import com.alibaba.fastjson.JSONObject;

/**
 * @author sunyi
 */
public class ServerConfig<T> {

	/**
	 * 进程ID
	 */
	private Integer pid;

	/**
	 * 服务器IP
	 */
	private String ip;

	/**
	 * 端口
	 */
	private Integer port;

	/**
	 * 接口
	 */
	private Class<T> interfaceClass;

	private Object serverImplement;

	/**
	 * 优先级,默认5
	 */
	private Integer priority = 5;


	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Object getServerImplement() {
		return serverImplement;
	}

	public void setServerImplement(Object serverImplement) {
		this.serverImplement = serverImplement;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
