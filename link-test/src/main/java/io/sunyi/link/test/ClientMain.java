package io.sunyi.link.test;

import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.zookeeper.ZookeeperRegistry;
import io.sunyi.link.core.serialize.hessian.HessianSerializeFactory;
import io.sunyi.link.core.server.ServerReceivedHandler;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) {
		Registry registry = new ZookeeperRegistry("192.168.1.120:2181");


		ApplicationContext.setRegistry(registry);
		ApplicationContext.setSerializeFactory(HessianSerializeFactory.getInstance());
		ApplicationContext.setServerReceivedHandler(new ServerReceivedHandler());

		InvocationBootstrap instance = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> invocationConfig = new InvocationConfig<HelloService>();
		invocationConfig.setInterfaceClass(HelloService.class);
		invocationConfig.setTimeout(10000L);


		HelloService proxy = instance.getProxy(invocationConfig);

		System.out.println("return:" + proxy.say("123"));

	}
}
