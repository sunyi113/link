package io.sunyi.link.core.context;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.invocation.loadbalance.LoadBalance;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;
import io.sunyi.link.core.network.NetworkClientFactory;
import io.sunyi.link.core.network.NetworkServerFactory;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.serialize.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 加载依赖的具体组件，根据配置文件加载。
 *
 * @author sunyi
 */
public class LinkApplicationContext {

	private static Logger logger = LoggerFactory.getLogger(LinkApplicationContext.class);

	private static volatile AtomicBoolean isLoadComponentsOver = new AtomicBoolean(false);


	/**
	 * 注册中心
	 */
	private static volatile Registry registry;

	/**
	 * 注册中心地址，可选
	 */
	private static volatile String registryUrl;


	/**
	 * 请求者的网络服务工厂
	 */
	private static volatile NetworkClientFactory networkClientFactory;

	/**
	 * 服务端的网络服务工厂
	 */
	private static volatile NetworkServerFactory networkServerFactory;

	/**
	 * 服务端的端口
	 */
	private static volatile Integer networkServerPort;

	/**
	 * 网络通讯报文的序列化工厂
	 */
	private static volatile SerializeFactory serializeFactory;

	/**
	 * 代理生成调用者代理类工厂
	 */
	private static volatile InvocationProxyFactory invocationProxyFactory ;

	/**
	 * 调用时服务在均衡策略
	 */
	private static volatile LoadBalance loadBalance;


	/**
	 * 根据配置文件，将未指定的组件加载到 {@link LinkApplicationContext} ，如果组件已经指定则跳过。  <br/>
	 * 配置文件在 classpath 下面寻找，优先使用配置 {@link ComponentConfigConstants#CONFIG_FILE_NAME} , <br/>
	 * 之后加载默认配置 {@link ComponentConfigConstants#DEFAULT_CONFIG_FILE_NAME} ,
	 */
	public static synchronized void loadComponents() {

		Properties conf = new Properties();

		try {
			URL defaultConf = LinkApplicationContext.class.getResource("/" + ComponentConfigConstants.DEFAULT_CONFIG_FILE_NAME);
			conf.load(defaultConf.openStream());
		} catch (Exception e) {
			throw new LinkException("Load default config failed", e);
		}

		try {
			URL customConf = LinkApplicationContext.class.getResource("/" + ComponentConfigConstants.CONFIG_FILE_NAME);
			if (customConf != null) {
				conf.load(customConf.openStream());
			}
		} catch (Exception e) {
			logger.warn("Load custom config failed", e);
		}

		try {
			ClassLoader classLoader = LinkApplicationContext.class.getClassLoader();

			if (LinkApplicationContext.getRegistry() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.Registry);
				Class<?> confClass = classLoader.loadClass(confValue);
				Registry component = (Registry) confClass.newInstance();
				component.setRegistryUrl(LinkApplicationContext.getRegistryUrl());
				LinkApplicationContext.setRegistry(component);
				logger.debug("With conf load Registry component, class is: " + confValue);
			}


			if (LinkApplicationContext.getNetworkServerFactory() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.NetworkServerFactory);
				Class<?> confClass = classLoader.loadClass(confValue);
				NetworkServerFactory component = (NetworkServerFactory) confClass.newInstance();
				component.setPort(LinkApplicationContext.getNetworkServerPort());
				LinkApplicationContext.setNetworkServerFactory(component);
				logger.debug("With conf load NetworkServerFactory component, class is: " + confValue);
			}


			if (LinkApplicationContext.getNetworkClientFactory() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.NetworkClientFactory);
				Class<?> confClass = classLoader.loadClass(confValue);
				NetworkClientFactory component = (NetworkClientFactory) confClass.newInstance();
				LinkApplicationContext.setNetworkClientFactory(component);
				logger.debug("With conf load NetworkClientFactory component, class is: " + confValue);
			}

			if (LinkApplicationContext.getSerializeFactory() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.SerializeFactory);
				Class<?> confClass = classLoader.loadClass(confValue);
				SerializeFactory component = (SerializeFactory) confClass.newInstance();
				LinkApplicationContext.setSerializeFactory(component);
				logger.debug("With conf load SerializeFactory component, class is: " + confValue);
			}

			if (LinkApplicationContext.getInvocationProxyFactory() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.InvocationProxyFactory);
				Class<?> confClass = classLoader.loadClass(confValue);
				InvocationProxyFactory component = (InvocationProxyFactory) confClass.newInstance();
				LinkApplicationContext.setInvocationProxyFactory(component);
				logger.debug("With conf load InvocationProxyFactory component, class is: " + confValue);
			}

			if (LinkApplicationContext.getLoadBalance() == null) {
				String confValue = conf.getProperty(ComponentConfigConstants.LoadBalance);
				Class<?> confClass = classLoader.loadClass(confValue);
				LoadBalance component = (LoadBalance) confClass.newInstance();
				LinkApplicationContext.setLoadBalance(component);
				logger.debug("With conf load LoadBalance component, class is: " + confValue);
			}

		} catch (Exception e) {
			throw new LinkException("LinkApplicationContext loading component error", e);
		}

	}


	/* ---------------------- getter/setter ---------------------- */

	public static Registry getRegistry() {
		return registry;
	}


	public static void setRegistry(Registry registry) {
		LinkApplicationContext.registry = registry;
	}

	public static String getRegistryUrl() {
		return registryUrl;
	}

	public static void setRegistryUrl(String registryUrl) {
		LinkApplicationContext.registryUrl = registryUrl;
	}

	public static NetworkServerFactory getNetworkServerFactory() {
		return networkServerFactory;
	}

	public static void setNetworkServerFactory(NetworkServerFactory networkServerFactory) {
		LinkApplicationContext.networkServerFactory = networkServerFactory;
	}

	public static NetworkClientFactory getNetworkClientFactory() {
		return networkClientFactory;
	}

	public static void setNetworkClientFactory(NetworkClientFactory networkClientFactory) {
		LinkApplicationContext.networkClientFactory = networkClientFactory;
	}

	public static SerializeFactory getSerializeFactory() {
		return serializeFactory;
	}

	public static void setSerializeFactory(SerializeFactory serializeFactory) {
		LinkApplicationContext.serializeFactory = serializeFactory;
	}

	public static InvocationProxyFactory getInvocationProxyFactory() {
		return invocationProxyFactory;
	}

	public static void setInvocationProxyFactory(InvocationProxyFactory invocationProxyFactory) {
		LinkApplicationContext.invocationProxyFactory = invocationProxyFactory;
	}

	public static LoadBalance getLoadBalance() {
		return loadBalance;
	}

	public static void setLoadBalance(LoadBalance loadBalance) {
		LinkApplicationContext.loadBalance = loadBalance;
	}

	public static Integer getNetworkServerPort() {
		return networkServerPort;
	}

	public static void setNetworkServerPort(Integer networkServerPort) {
		LinkApplicationContext.networkServerPort = networkServerPort;
	}
}
