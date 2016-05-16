package io.sunyi.link.demo.simple;

import io.sunyi.link.core.context.LinkApplicationContext;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");
		LinkApplicationContext.setNetworkServerPort(10001);

		LinkApplicationContext.loadComponents();

		InvocationBootstrap invocationBootstrap = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> helloServiceInvocationConfig = new InvocationConfig<HelloService>();
		helloServiceInvocationConfig.setInterfaceClass(HelloService.class);
		helloServiceInvocationConfig.setTimeout(100L);

		HelloService proxy = invocationBootstrap.getProxy(helloServiceInvocationConfig);

		for (int i = 0; i < 100; i++) {

			String say = proxy.say(String.valueOf(i));
			System.out.println(say);
		}
	}
}