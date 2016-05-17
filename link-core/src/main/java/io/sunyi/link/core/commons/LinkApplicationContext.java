package io.sunyi.link.core.commons;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.filter.InvocationFilter;
import io.sunyi.link.core.filter.ServerFilter;
import io.sunyi.link.core.invocation.loadbalance.LoadBalance;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;
import io.sunyi.link.core.network.NetworkClientFactory;
import io.sunyi.link.core.network.NetworkServerFactory;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.serialize.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加载依赖的具体组件，根据配置文件加载。
 *
 * @author sunyi
 */
public class LinkApplicationContext {

	private static Logger logger = LoggerFactory.getLogger(LinkApplicationContext.class);

	/**
	 * <li> 0: Initial</li>
	 * <li> 1: Starting and loading </li>
	 * <li> 2: Start over </li>
	 */
	private static volatile AtomicInteger stat = new AtomicInteger(0);


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
	private static volatile InvocationProxyFactory invocationProxyFactory;

	/**
	 * 调用时服务在均衡策略
	 */
	private static volatile LoadBalance loadBalance;

	private static List<InvocationFilter> invocationFilters = new CopyOnWriteArrayList<InvocationFilter>();

	private static List<ServerFilter> serverFilters = new CopyOnWriteArrayList<ServerFilter>();


	/**
	 * 上下文初始化,
	 */
	public static synchronized void initialization() {

		checkStat();

		stat.incrementAndGet(); // 1

		try {

			loadUnspecifiedComponents(); // 加载未指定的组件

		} catch (Exception e) {
			throw new LinkException("LinkApplicationContext loading component error", e);
		}

		stat.incrementAndGet(); // 2

	}


	private static void checkStat() {
		if (stat.get() > 0) {
			throw new LinkException("Link application context stat is not initial, at this point has been unable to do so ");
		}
	}


	/**
	 * 先加载默认配置文件，再寻找用户自己定义配置文件，以自己定义配置文件为准
	 *
	 * @param conf
	 */
	private static void loadConfig(Properties conf) {
		try {
			URL defaultConf = LinkApplicationContext.class.getResource("/" + Constants.DEFAULT_CONFIG_FILE_NAME);
			conf.load(defaultConf.openStream());
		} catch (Exception e) {
			throw new LinkException("Load default config failed", e);
		}

		try {
			URL customConf = LinkApplicationContext.class.getResource("/" + Constants.CONFIG_FILE_NAME);
			if (customConf != null) {
				conf.load(customConf.openStream());
			}
		} catch (Exception e) {
			logger.warn("Load custom config failed", e);
		}
	}

	/**
	 * 根据配置文件，将未指定的组件加载到 {@link LinkApplicationContext} ，如果组件已经指定则跳过。  <br/>
	 * 配置文件在 classpath 下面寻找，优先使用配置 {@link Constants#CONFIG_FILE_NAME} , <br/>
	 * 之后加载默认配置 {@link Constants#DEFAULT_CONFIG_FILE_NAME} ,
	 */
	private static void loadUnspecifiedComponents() throws IllegalAccessException, InstantiationException, ClassNotFoundException {

		Properties conf = new Properties();
		loadConfig(conf);

		ClassLoader classLoader = LinkApplicationContext.class.getClassLoader();

		if (LinkApplicationContext.getRegistry() == null) {
			Registry component = (Registry) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_REGISTRY);
			component.setRegistryUrl(LinkApplicationContext.getRegistryUrl());
			LinkApplicationContext.registry = component;
		}

		if (LinkApplicationContext.getNetworkServerFactory() == null) {
			NetworkServerFactory component = (NetworkServerFactory) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_NETWORKSERVERFACTORY);
			component.setPort(LinkApplicationContext.getNetworkServerPort());
			LinkApplicationContext.networkServerFactory = component;
		}


		if (LinkApplicationContext.getNetworkClientFactory() == null) {
			NetworkClientFactory component = (NetworkClientFactory) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_NETWORKCLIENTFACTORY);
			LinkApplicationContext.networkClientFactory = component;
		}

		if (LinkApplicationContext.getSerializeFactory() == null) {
			SerializeFactory component = (SerializeFactory) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_SERIALIZEFACTORY);
			LinkApplicationContext.serializeFactory = component;
		}

		if (LinkApplicationContext.getInvocationProxyFactory() == null) {
			InvocationProxyFactory component = (InvocationProxyFactory) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_INVOCATIONPROXYFACTORY);
			LinkApplicationContext.invocationProxyFactory = component;
		}

		if (LinkApplicationContext.getLoadBalance() == null) {
			LoadBalance component = (LoadBalance) loadComponentFromConfig(classLoader, conf, Constants.CONFIG_COMPONENT_LOADBALANCE);
			LinkApplicationContext.loadBalance = component;
		}
	}

	private static Object loadComponentFromConfig(ClassLoader classLoader, Properties conf, String configKey) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		String confValue = conf.getProperty(configKey);
		Class<?> confClass = classLoader.loadClass(confValue);
		return confClass.newInstance();
	}


	/* ---------------------- getter/setter ---------------------- */

	public static Registry getRegistry() {
		return registry;
	}


	public static void setRegistry(Registry registry) {
		checkStat();
		LinkApplicationContext.registry = registry;
	}

	public static String getRegistryUrl() {
		return registryUrl;
	}

	public static void setRegistryUrl(String registryUrl) {
		checkStat();
		LinkApplicationContext.registryUrl = registryUrl;
	}

	public static NetworkServerFactory getNetworkServerFactory() {
		return networkServerFactory;
	}

	public static void setNetworkServerFactory(NetworkServerFactory networkServerFactory) {
		checkStat();
		LinkApplicationContext.networkServerFactory = networkServerFactory;
	}

	public static NetworkClientFactory getNetworkClientFactory() {
		return networkClientFactory;
	}

	public static void setNetworkClientFactory(NetworkClientFactory networkClientFactory) {
		checkStat();
		LinkApplicationContext.networkClientFactory = networkClientFactory;
	}

	public static SerializeFactory getSerializeFactory() {
		return serializeFactory;
	}

	public static void setSerializeFactory(SerializeFactory serializeFactory) {
		checkStat();
		LinkApplicationContext.serializeFactory = serializeFactory;
	}

	public static InvocationProxyFactory getInvocationProxyFactory() {
		return invocationProxyFactory;
	}

	public static void setInvocationProxyFactory(InvocationProxyFactory invocationProxyFactory) {
		checkStat();
		LinkApplicationContext.invocationProxyFactory = invocationProxyFactory;
	}

	public static LoadBalance getLoadBalance() {
		return loadBalance;
	}

	public static void setLoadBalance(LoadBalance loadBalance) {
		checkStat();
		LinkApplicationContext.loadBalance = loadBalance;
	}

	public static Integer getNetworkServerPort() {
		return networkServerPort;
	}

	public static void setNetworkServerPort(Integer networkServerPort) {
		checkStat();
		LinkApplicationContext.networkServerPort = networkServerPort;
	}

	public static List<ServerFilter> getServerFilters() {
		return Collections.unmodifiableList(serverFilters);
	}

	public static void addServerFilters(ServerFilter serverFilter) {
		checkStat();
		LinkApplicationContext.serverFilters.add(serverFilter);
	}

	public static List<InvocationFilter> getInvocationFilters() {
		return Collections.unmodifiableList(invocationFilters);
	}

	public static void addInvocationFilters(InvocationFilter invocationFilter) {
		checkStat();
		LinkApplicationContext.invocationFilters.add(invocationFilter);
	}
}
