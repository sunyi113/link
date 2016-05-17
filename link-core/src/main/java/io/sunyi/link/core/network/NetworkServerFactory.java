package io.sunyi.link.core.network;

import io.sunyi.link.core.commons.LinkScalableComponent;

/**
 * 具体实现需要支持无参构造函数, 默认使用 newInstance 的方式来创建具体实现。
 *
 * @author sunyi
 */
public interface NetworkServerFactory extends LinkScalableComponent {

	/**
	 * 设置服务端接收请求的网络端口，没有默认值，如不需要可忽略。
	 *
	 * @param port
	 */
	void setPort(int port);

	/**
	 * 获取NetworkServer，是否为单例模式由具体实现类决定。
	 *
	 * @return
	 */
	NetworkServer getNetworkServer();


}
