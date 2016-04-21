package io.sunyi.link.core.invocation.proxy;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.network.NetworkClientSharedHolder;
import io.sunyi.link.core.network.ReferenceCountNetworkClient;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.RegistryListener;
import io.sunyi.link.core.server.ServerConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author sunyi
 */
public class InvocationProxyFactory {

	private static Registry registry = ApplicationContext.getRegistry();

	public static <T> T getObject(InvocationConfig<T> invocationConfig) {
		InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(invocationConfig);


		Class<T> interfaceClass = invocationConfig.getInterfaceClass();

		return (T) Proxy.newProxyInstance(InvocationProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
	}


	private static class InvocationHandlerImpl<T> implements InvocationHandler {


		private final Class<T> interfaceClass;
		private final InvocationConfig<T> invocationConfig;

		private final Random random = new Random();

		private final ConcurrentHashMap<InetSocketAddress, NetworkClient> networkClients = new ConcurrentHashMap<InetSocketAddress, NetworkClient>();


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
			//TODO load balance
			int index = random.nextInt(networkClients.size());
			Iterator<Map.Entry<InetSocketAddress, NetworkClient>> iterator = networkClients.entrySet().iterator();

			NetworkClient selectNetworkClient = null;

			for (int i = 0; iterator.hasNext(); i++) {
				Map.Entry<InetSocketAddress, NetworkClient> next = iterator.next();
				if (i == index) {
					selectNetworkClient = next.getValue();
					break;
				}
			}

			// 发送数据
			RpcResponse response = selectNetworkClient.send(request, 1000L, invocationConfig.getTimeout());

			// 返回结果
			return response.recur();
		}

		private void refresh(List<ServerConfig> serverConfigs) {

			HashSet<InetSocketAddress> newAddressSet = new HashSet<InetSocketAddress>();

			for (ServerConfig serverConfig : serverConfigs) {
				InetSocketAddress address = new InetSocketAddress(serverConfig.getIp(), serverConfig.getPort());
				newAddressSet.add(address);
			}

			Set<InetSocketAddress> oldAddressSet = networkClients.keySet();


			// 增加新的服务端
			for (InetSocketAddress newAddress : newAddressSet) {
				if (!oldAddressSet.contains(newAddress)) {
					NetworkClient networkClient = NetworkClientSharedHolder.getSharedNetworkClient(newAddress);
					networkClients.put(newAddress, networkClient);
				}
			}


			// 删除下线的服务端
			for (InetSocketAddress oldAddress : oldAddressSet) {
				if (!newAddressSet.contains(oldAddress)) {
					NetworkClient networkClient = networkClients.get(oldAddress);
					networkClient.close();
				}
			}


		}
	}

	public static void setRegistry(Registry registry) {
		InvocationProxyFactory.registry = registry;
	}
}
