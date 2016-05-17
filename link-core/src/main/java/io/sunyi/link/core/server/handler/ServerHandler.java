package io.sunyi.link.core.server.handler;

import io.sunyi.link.core.body.AttachmentKeys;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.filter.ServerFilter;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.server.ServerConfig;
import org.apache.commons.lang.time.StopWatch;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sunyi
 */
public class ServerHandler {

	private static volatile ServerHandler instance = new ServerHandler();

	private ServerHandler() {
	}

	public static ServerHandler getInstance() {
		return instance;
	}

	/**
	 * @param rpcRequest
	 */
	public RpcResponse received(RpcRequest rpcRequest) {

		StopWatch timeConsuming = new StopWatch(); // 计时器
		timeConsuming.start();

		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setId(rpcRequest.getId());

		try {
			Class interfaceClass = rpcRequest.getInterfaceClass();
			String methodName = rpcRequest.getMethodName();
			Class[] parameterTypes = rpcRequest.getParameterTypes();
			Object[] params = rpcRequest.getParams();

			ServerConfig serverConfig = ServerBootstrap.getInstance().getServerConfig(interfaceClass);
			Method method = interfaceClass.getMethod(methodName, parameterTypes);

			List<ServerFilter> filters = LinkApplicationContext.getServerFilters();

			for (ServerFilter filter : filters) {
				filter.preInvoke(rpcRequest);
			}

			Object result = method.invoke(serverConfig.getServerImplement(), params); // 反射调用

			rpcResponse.setResult(result);


		} catch (Throwable e) {
			rpcResponse.setHasException(true);
			rpcResponse.setException(e);
		} finally {
			timeConsuming.stop();
			rpcResponse.addAttachment(AttachmentKeys.TIME_CONSUMING, String.valueOf(timeConsuming.getTime()));
		}

		List<ServerFilter> filters = LinkApplicationContext.getServerFilters();
		for (ServerFilter filter : filters) {
			filter.afterInvoke(rpcRequest,rpcResponse);
		}


		return rpcResponse;
	}

}