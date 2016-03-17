package io.sunyi.link.core;

import io.sunyi.link.core.exception.LinkRuntimeException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.netty.NettyNetworkServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * link 启动器
 *
 * @author sunyi
 */
public class Launch {



	/**
	 * 启动锁，防止重复启动，需要用锁控制
	 */
	private static final ReadWriteLock startLock = new ReentrantReadWriteLock();
	private static final Lock startReadLock = startLock.readLock();
	private static final Lock startWriteLock = startLock.writeLock();
	/**
	 * 是否已经启动
	 */
	private static volatile boolean started = false;

	private static NetworkServer networkServer = new NettyNetworkServer();


	/**
	 * 启动服务
	 */
	public static void start() {
		if (isStarted()) {
			return;
		}

		try {
			startWriteLock.lock();

			// 启动网络端口
			networkServer.start();

			// 注册到注册中心


			// close hook
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					shutdown();
				}
			});

		} catch (Exception e) {
			throw new LinkRuntimeException("启动时发生异常.",e);
		} finally {
			startWriteLock.unlock();
		}
	}

	/**
	 * 发布服务
	 */
	public static void exportServer() {
		if (!isStarted()) {
			return;
		}
	}

	public static boolean isStarted() {
		try {
			startReadLock.lock();
			return started;
		} finally {
			startReadLock.unlock();
		}
	}

	public static void shutdown() {
		try {
			networkServer.shutdown();
		} catch (Exception e) {
			throw new LinkRuntimeException("Shutdown时发生异常.",e);
		}
	}





}
