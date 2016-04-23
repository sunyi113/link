package io.sunyi.link.core.invocation.proxy.jdk;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.invocation.invoker.Invoker;
import io.sunyi.link.core.invocation.loadbalance.LoadBalance;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.network.NetworkClientSharedHolder;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.RegistryListener;
import io.sunyi.link.core.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 */
public class JdkInvocationProxyFactory implements InvocationProxyFactory {


	private static Logger logger = LoggerFactory.getLogger(JdkInvocationProxyFactory.class);


	public <T> T getObject(InvocationConfig<T> invocationConfig) {
		InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(invocationConfig);
		Class<T> interfaceClass = invocationConfig.getInterfaceClass();
		return (T) Proxy.newProxyInstance(JdkInvocationProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
	}


	private static class InvocationHandlerImpl<T> implements InvocationHandler {


		private final Class<T> interfaceClass;
		private final InvocationConfig<T> invocationConfig;

		private final Random random = new Random();

		private final ConcurrentHashMap<InetSocketAddress, Invoker<T>> invokers = new ConcurrentHashMap<InetSocketAddress, Invoker<T>>();


		private Registry registry = ApplicationContext.getRegistry();


		public InvocationHandlerImpl(InvocationConfig<T> invocationConfig) {
			this.invocationConfig = invocationConfig;
			this.interfaceClass = invocationConfig.getInterfaceClass();

			List<ServerConfig> serverConfigs = registry.getServerList(interfaceClass);
			refresh(serverConfigs);

			registry.watching(interfaceClass, new RegistryListener() {
				@Override
				public void onServerChange(List<ServerConfig> serverConfigs) {
					refresh(serverConfigs);
				}
			});
		}


		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

			// 封装 PpcRequest 对象
			RpcRequest request = new RpcRequest();

			request.setInterfaceClass(interfaceClass);
			request.setMethodName(method.getName());
			request.setParameterTypes(method.getParameterTypes());
			request.setParams(objects);


			// 选择一个服务提供者
			LoadBalance loadBalance = ApplicationContext.getLoadBalance();
			Collection<Invoker<T>> values = invokers.values();
			Invoker<T> invoker = loadBalance.getInvoker(new ArrayList<Invoker<T>>(values));


			// 发送数据
			RpcResponse response = invoker.invoke(request);

			// 返回结果
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


			// 删除失效的 Invoker
			Set<Map.Entry<InetSocketAddress, Invoker<T>>> entries = invokers.entrySet();
			for (Map.Entry<InetSocketAddress, Invoker<T>> entry : entries) {
				if (!newAddressMap.containsKey(entry.getKey())) {
					Invoker<T> destroyInvoker = invokers.remove(entry.getKey());
					try {
						destroyInvoker.close();
					} catch (Exception e) {
						logger.warn("Close destroy invoker fail。", e);
					}
				}
			}

			// 增加新的服务端
			Set<Map.Entry<InetSocketAddress, ServerConfig>> newAddressSet = newAddressMap.entrySet();
			for (Map.Entry<InetSocketAddress, ServerConfig> entry : newAddressSet) {
				if (!invokers.containsKey(entry.getKey())) {
					NetworkClient networkClient = NetworkClientSharedHolder.getSharedNetworkClient(entry.getKey());
					Invoker<T> invoker = new Invoker<T>(invocationConfig, entry.getValue(), networkClient);
					invokers.put(entry.getKey(), invoker);
				}
			}


		}
	}


}
