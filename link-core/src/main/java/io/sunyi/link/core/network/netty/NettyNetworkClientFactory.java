package io.sunyi.link.core.network.netty;

import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.network.NetworkClientFactory;

/**
 * @author sunyi
 */
public class NettyNetworkClientFactory implements NetworkClientFactory {
	
	@Override
	public NetworkClient getNetworkClient() {
		return new NettyNetworkClient();
	}

}
