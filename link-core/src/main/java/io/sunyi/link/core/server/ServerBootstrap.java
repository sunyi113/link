package io.sunyi.link.core.server;

import io.sunyi.link.core.exception.LinkRuntimeException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.utils.NetUtils;
import io.sunyi.link.core.utils.PidUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * link 启动器
 *
 * @author sunyi
 */
public class ServerBootstrap {

	private volatile static ServerBootstrap instance ;

	/**
	 * 启动锁，防止重复启动，需要用锁控制
	 */
	private final ReadWriteLock startLock = new ReentrantReadWriteLock();
	private final Lock startReadLock = startLock.readLock();
	private final Lock startWriteLock = startLock.writeLock();
	/**
	 * 是否已经启动
	 */
	private volatile boolean started = false;


	/**
	 * 网络服务
	 */
	private NetworkServer networkServer = null;
	/**
	 * 注册中心
	 */
	private Registry registry = null;


	private ConcurrentHashMap<Class, ServerConfig> serverConfigMap = new ConcurrentHashMap<Class, ServerConfig>();


	public static ServerBootstrap getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (ServerBootstrap.class) {
			if (instance != null) {
				return instance;
			}
			instance = new ServerBootstrap();
			return instance;
		}
	}


	/**
	 * 启动服务
	 */
	public synchronized void start() {
		if (isStarted()) {
			return;
		}

		try {
			startWriteLock.lock();
			if (isStarted()) {
				return;
			}

			// 启动网络端口
			networkServer.start();

			// close hook
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					shutdown();
				}
			});

			started = true;

		} catch (Exception e) {
			throw new LinkRuntimeException("启动时发生异常.", e);
		} finally {
			startWriteLock.unlock();
		}
	}

	/**
	 * 发布服务
	 */
	public void exportServer(ServerConfig serverConfig) {

		if (!isStarted()) {
			throw new LinkRuntimeException("暴露服务前需要先启动LinkLaunch");
		}

		if (serverConfig == null) {
			throw new LinkRuntimeException("ServerConfig must not be null");
		}

		if (serverConfig.getInterfaceClass() == null) {
			throw new LinkRuntimeException("ServerConfig.interfaceClass must not be null");
		}

		if (serverConfig.getServerImplement() == null) {
			throw new LinkRuntimeException("ServerConfig.interfaceClass serverImplement must not be null");
		}

		fillingServerConfig(serverConfig); // 填充一些信息

		serverConfigMap.put(serverConfig.getInterfaceClass(), serverConfig);

		// 在注册中心发布服务
		registry.exportServer(serverConfig);

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

	public boolean isStarted() {
		try {
			startReadLock.lock();
			return started;
		} finally {
			startReadLock.unlock();
		}
	}

	public void shutdown() {
		try {
			networkServer.shutdown();
			registry.close();
		} catch (Exception e) {
			throw new LinkRuntimeException("Shutdown时发生异常.", e);
		}
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
