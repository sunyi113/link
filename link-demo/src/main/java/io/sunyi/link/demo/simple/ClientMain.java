package io.sunyi.link.demo.simple;

import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");// 注册中心地址，写法按照 具体注册中心实现要求，默认 zk

		LinkApplicationContext.initialization();

		InvocationBootstrap invocationBootstrap = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> helloServiceInvocationConfig = new InvocationConfig<HelloService>();
		helloServiceInvocationConfig.setInterfaceClass(HelloService.class);
		helloServiceInvocationConfig.setTimeout(1000L);

		HelloService proxy = invocationBootstrap.getProxy(helloServiceInvocationConfig);

		String result = proxy.say("我想去旅游...");
		System.out.println(result);

	}
}