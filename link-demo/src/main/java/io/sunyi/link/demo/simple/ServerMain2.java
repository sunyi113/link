package io.sunyi.link.demo.simple;

import io.sunyi.link.core.context.LinkApplicationContext;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.server.ServerConfig;

/**
 * @author sunyi
 */
public class ServerMain2 {

	public static void main(String args[]) {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");
		LinkApplicationContext.setNetworkServerPort(10002);



		ServerBootstrap serverBootstrap = ServerBootstrap.getInstance();

		ServerConfig<HelloService> serverConfig = new ServerConfig<HelloService>();
		serverConfig.setInterfaceClass(HelloService.class);
		serverConfig.setServerImplement(new HelloServiceImpl());


		serverBootstrap.exportServer(serverConfig);




	}

}
