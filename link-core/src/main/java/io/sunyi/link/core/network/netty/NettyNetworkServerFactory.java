package io.sunyi.link.core.network.netty;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.NetworkServerFactory;

/**
 * @author sunyi
 */
public class NettyNetworkServerFactory implements NetworkServerFactory {

	private volatile Integer port;
	private volatile NetworkServer networkServer;

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public NetworkServer getNetworkServer() {

		if (port == null) {
			throw new LinkException("The NettyNetworkServer need specify a port, @see LinkApplicationContext.setNetworkServerPort(Integer)");
		}

		if (networkServer == null) {
			synchronized (NettyNetworkServerFactory.class) {
				if (networkServer == null) {
					this.networkServer = new NettyNetworkServer(port);
				}
			}
		}

		return networkServer;
	}
}
