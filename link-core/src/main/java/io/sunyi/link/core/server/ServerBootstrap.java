package io.sunyi.link.core.server;

import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.NetworkServerFactory;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.utils.NetUtils;
import io.sunyi.link.core.utils.PidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * link 启动器
 *
 * @author sunyi
 */
public class ServerBootstrap {

	private Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

	private volatile static ServerBootstrap instance;


	/**
	 * 网络服务
	 */
	private NetworkServer networkServer;


	/**
	 * 注册中心
	 */
	private Registry registry;


	private ConcurrentHashMap<Class, ServerConfig> serverConfigMap = new ConcurrentHashMap<Class, ServerConfig>();

	private ServerBootstrap() {
	}

	public static ServerBootstrap getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (ServerBootstrap.class) {
			if (instance != null) {
				return instance;
			}

			final ServerBootstrap serverBootstrap = new ServerBootstrap();

			// 启动网络端口
			NetworkServerFactory networkServerFactory = LinkApplicationContext.getNetworkServerFactory();
			serverBootstrap.networkServer = networkServerFactory.getNetworkServer();
			serverBootstrap.networkServer.start();

			serverBootstrap.registry = LinkApplicationContext.getRegistry();
			serverBootstrap.registry.init();

			// close hook
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					serverBootstrap.shutdown();
				}
			});


			serverBootstrap.logger.info("Link server bootstrap has already started.");

			instance = serverBootstrap;
		}

		return instance;
	}


	/**
	 * 发布服务
	 */
	public void exportServer(ServerConfig serverConfig) {


		if (serverConfig == null) {
			throw new LinkException("ServerConfig must not be null");
		}

		if (serverConfig.getInterfaceClass() == null) {
			throw new LinkException("ServerConfig.interfaceClass must not be null");
		}

		if (serverConfig.getServerImplement() == null) {
			throw new LinkException("ServerConfig.interfaceClass serverImplement must not be null");
		}

		fillingServerConfig(serverConfig); // 填充一些信息

		serverConfigMap.put(serverConfig.getInterfaceClass(), serverConfig);

		// 在注册中心发布服务
		registry.exportServer(serverConfig);

		logger.info("Export link server, InterfaceClass:[{}],ImplementClass:[{}]", serverConfig.getInterfaceClass().getName(), serverConfig.getServerImplement().getClass().getName());


	}

	private ServerConfig fillingServerConfig(ServerConfig serverConfig) {

		if (serverConfig.getPid() == null) {
			serverConfig.setPid(PidUtils.getPid());
		}

		if (serverConfig.getIp() == null) {
			serverConfig.setIp(NetUtils.getLocalAddressIp());
		}

		if (serverConfig.getPort() == null) {
			serverConfig.setPort(networkServer.getPort());
		}

		return serverConfig;
	}

	public ServerConfig getServerConfig(Class interfaceClass) {
		return serverConfigMap.get(interfaceClass);
	}

	public void shutdown() {
		try {
			networkServer.shutdown();
		} catch (Exception e) {
			throw new LinkException("An exception occurs when the server bootstrap shutdown", e);
		}

		try {
			registry.close();
		} catch (Exception e) {
			throw new LinkException("An exception occurs when the server bootstrap shutdown", e);
		}


	}

}
