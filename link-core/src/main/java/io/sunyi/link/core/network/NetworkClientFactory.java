package io.sunyi.link.core.network;

import io.sunyi.link.core.commons.LinkScalableComponent;

/**
 * 具体实现需要支持无参构造函数, 默认使用 newInstance 的方式来创建具体实现。
 *
 * @author sunyi
 */
public interface NetworkClientFactory  extends LinkScalableComponent {

	NetworkClient getNetworkClient();

}