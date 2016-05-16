package io.sunyi.link.core.invocation.proxy;

import io.sunyi.link.core.body.AttachmentKeys;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.context.LinkApplicationContext;
import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.invocation.invoker.Invoker;
import io.sunyi.link.core.invocation.loadbalance.LoadBalance;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.network.NetworkClientSharedHolder;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.RegistryListener;
import io.sunyi.link.core.server.ServerConfig;
import io.sunyi.link.core.utils.NetUtils;
import io.sunyi.link.core.utils.PidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的代理对象, 进行 负载均衡， 网络请求， 封装结果 等工作。
 *
 * @author sunyi
 */
public class InvocationProxy<T> {

	private static Logger logger = LoggerFactory.getLogger(InvocationProxy.class);


	private final Class<T> interfaceClass;
	private final InvocationConfig<T> invocationConfig;

	private final Registry registry;

	private final ConcurrentHashMap<InetSocketAddress, Invoker<T>> invokers = new ConcurrentHashMap<InetSocketAddress, Invoker<T>>();

	public InvocationProxy(InvocationConfig<T> invocationConfig) {
		this.interfaceClass = invocationConfig.getInterfaceClass();
		this.invocationConfig = invocationConfig;
		this.registry = LinkApplicationContext.getRegistry();

		List<ServerConfig> serverConfigs = registry.getServerList(interfaceClass);
		refresh(serverConfigs);

		registry.watching(interfaceClass, new RegistryListener() {
			@Override
			public void onServerChange(List<ServerConfig> serverConfigs) {
				refresh(serverConfigs);
			}
		});
	}

	public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

		// 封装 PpcRequest 对象
		RpcRequest request = new RpcRequest();
		request.setInterfaceClass(interfaceClass);
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setParams(objects);
		fillingRpcRequest(request);


		// 选择一个服务提供者
		LoadBalance loadBalance = LinkApplicationContext.getLoadBalance();
		Collection<Invoker<T>> values = invokers.values();
		Invoker<T> invoker = loadBalance.getInvoker(new ArrayList<Invoker<T>>(values));

		if (invoker == null) {
			throw new LinkException("No server available, interfaceClass:[" + interfaceClass.getName() + "]");
		}

		// 发送数据
		RpcResponse response = invoker.invoke(request);

		// 重现结果
		return response.recur();
	}

	private void refresh(List<ServerConfig> serverConfigs) {

		if (serverConfigs == null) {
			serverConfigs = Collections.emptyList();
		}

		HashMap<InetSocketAddress, ServerConfig> newAddressMap = new HashMap<InetSocketAddress, ServerConfig>(serverConfigs.size());

		for (ServerConfig serverConfig : serverConfigs) {
			InetSocketAddress address = new InetSocketAddress(serverConfig.getIp(), serverConfig.getPort());
			newAddressMap.put(address, serverConfig);
		}


		// 增加新的服务端
		Set<Map.Entry<InetSocketAddress, ServerConfig>> newAddressSet = newAddressMap.entrySet();
		for (Map.Entry<InetSocketAddress, ServerConfig> entry : newAddressSet) {
			if (!invokers.containsKey(entry.getKey())) {
				ServerConfig serverConfig = entry.getValue();
				NetworkClient networkClient = NetworkClientSharedHolder.getSharedNetworkClient(entry.getKey());
				Invoker<T> invoker = new Invoker<T>(invocationConfig, entry.getValue(), networkClient);
				invokers.put(entry.getKey(), invoker);
				logger.info("Add {} invoker, the server ip:[{}], port:[{}], pid:[{}]", new Object[]{serverConfig.getInterfaceClass().getName(), serverConfig.getIp(), serverConfig.getPort(), serverConfig.getPid()});
			}
		}

		// 删除失效的 Invoker
		Set<Map.Entry<InetSocketAddress, Invoker<T>>> entries = invokers.entrySet();
		for (Map.Entry<InetSocketAddress, Invoker<T>> entry : entries) {
			if (!newAddressMap.containsKey(entry.getKey())) {
				Invoker<T> destroyInvoker = invokers.remove(entry.getKey());
				try {
					// TODO 延时删除 , 增加删除中的 Invoker 状态。
					destroyInvoker.close();
					ServerConfig<T> serverConfig = destroyInvoker.getServerConfig();
					logger.info("Destroy invoker, server ip:[" + serverConfig.getIp() + "], port:[" + serverConfig.getPort() + "], pid:[" + serverConfig.getPid() + "],interfaceClass:[" + serverConfig.getInterfaceClass().getName() + "]");
				} catch (Exception e) {
					logger.warn("Close destroy invoker fail。", e);
				}
			}
		}
	}

	/**
	 * 为 RpcRequest 填充一些信息。
	 *
	 * @param rpcRequest
	 */
	private void fillingRpcRequest(RpcRequest rpcRequest) {
		rpcRequest.addAttachment(AttachmentKeys.TIME_OUT, String.valueOf(invocationConfig.getTimeout()));
		rpcRequest.addAttachment(AttachmentKeys.INVOCATION_IP, NetUtils.getLocalAddressIp());
		rpcRequest.addAttachment(AttachmentKeys.INVOCATION_PID, String.valueOf(PidUtils.getPid()));
	}

}
