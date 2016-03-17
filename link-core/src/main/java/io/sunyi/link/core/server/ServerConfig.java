package io.sunyi.link.core.server;

/**
 * @author sunyi
 */
public class ServerConfig {


	private Integer pid;

	private String ip;
	private String port;
	private Class interfaceClass;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Class getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
}
