package io.sunyi.link.core.registry;

import io.sunyi.link.core.LinkScalableComponent;
import io.sunyi.link.core.server.ServerConfig;

import java.util.List;

/**
 * 注册中心
 *
 * @author sunyi
 *         Created on 16/2/15
 */
public interface Registry extends LinkScalableComponent {

	void init();

	void setRegistryUrl(String url);

	/**
	 * 发布一个服务
	 */
	void exportServer(ServerConfig serverConfig);


	/**
	 * 获取服务列表
	 * @return
	 */
	List<ServerConfig> getServerList(Class interfaceClass);

	List<ServerConfig> watching(Class interfaceClass, RegistryListener listener);


	void close();


}