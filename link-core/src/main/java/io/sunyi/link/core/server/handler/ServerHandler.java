package io.sunyi.link.core.server.handler;

import io.sunyi.link.core.body.AttachmentKeys;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.server.ServerConfig;
import org.apache.commons.lang.time.StopWatch;

import java.lang.reflect.Method;

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
		StopWatch timeConsumingWithoutFilter = new StopWatch(); // 计时器
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


			//TODO 嵌入 Filter before

			timeConsumingWithoutFilter.start();
			Object result = method.invoke(serverConfig.getServerImplement(), params); // 反射调用
			timeConsumingWithoutFilter.stop();

			//TODO 嵌入 Filter  after

			rpcResponse.setResult(result);

			timeConsuming.stop();
		} catch (Throwable e) {
			rpcResponse.setHasException(true);
			rpcResponse.setException(e);
		} finally {
			rpcResponse.addAttachment(AttachmentKeys.TIME_CONSUMING, String.valueOf(timeConsuming.getTime()));
			rpcResponse.addAttachment(AttachmentKeys.TIME_CONSUMING_WITHOUT_FILTER, String.valueOf(timeConsumingWithoutFilter.getTime()));
		}


		return rpcResponse;
	}

}