package io.sunyi.link.core.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by sunyi on 15/9/23.
 */
public interface NetworkClient {

	InetSocketAddress getServerAddress();

	Object send(Object requestBody);


}
