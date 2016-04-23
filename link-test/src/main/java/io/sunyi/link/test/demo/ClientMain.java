package io.sunyi.link.test.demo;

import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.invocation.loadbalance.RandomLoadBalance;
import io.sunyi.link.core.invocation.proxy.jdk.JdkInvocationProxyFactory;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.zookeeper.ZookeeperRegistry;
import io.sunyi.link.core.serialize.hessian.HessianSerializeFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {
		Registry registry = new ZookeeperRegistry("192.168.1.120:2181");


		ApplicationContext.setRegistry(registry);
		ApplicationContext.setSerializeFactory(HessianSerializeFactory.getInstance());
		ApplicationContext.setInvocationProxyFactory(new JdkInvocationProxyFactory());
		ApplicationContext.setLoadBalance(new RandomLoadBalance());

		InvocationBootstrap instance = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> invocationConfig = new InvocationConfig<HelloService>();
		invocationConfig.setInterfaceClass(HelloService.class);
		invocationConfig.setTimeout(1000L);


		HelloService proxy = instance.getProxy(invocationConfig);

		for (int i = 0; i < 10; i++) {
			System.out.println("return: " + proxy.say( String.valueOf(System.currentTimeMillis())));
			TimeUnit.MILLISECONDS.sleep(100);

		}


	}
}
