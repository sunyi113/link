package io.sunyi.link.core.server;

import io.sunyi.link.core.body.AttachementKeys;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import org.apache.commons.lang.time.StopWatch;

import java.lang.reflect.Method;

/**
 * @author sunyi
 *         Created on 15/9/28
 */
public class ServerReceivedHandler {

	/**
	 * @param rpcRequest
	 */
	public RpcResponse received(RpcRequest rpcRequest) {

		StopWatch watch = new StopWatch(); // 计时器
		watch.start();

		RpcResponse rpcResponse = new RpcResponse();

		try {
			Long id = rpcRequest.getId();
			Class interfaceClass = rpcRequest.getInterfaceClass();
			String methodName = rpcRequest.getMethodName();
			Class[] parameterTypes = rpcRequest.getParameterTypes();
			Object[] params = rpcRequest.getParams();

			rpcResponse.setId(id);

			ServerConfig serverConfig = ServerBootstrap.getServerConfig(interfaceClass);
			Method method = interfaceClass.getMethod(methodName, parameterTypes);

			//TODO 嵌入 Filter

			Object result = method.invoke(serverConfig.getServerImplement(), params); // 反射调用

			rpcResponse.setResult(result);


		} catch (Exception e) {
			rpcResponse.setHasException(true);
			rpcResponse.setException(e);
		}

		watch.stop();

		rpcResponse.addAttachment(AttachementKeys.TIME_CONSUMING, String.valueOf(watch.getTime()));

		return rpcResponse;
	}

}