package io.sunyi.link.demo.simple;

import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");// 注册中心地址，写法按照 具体注册中心实现要求，默认 zk
		LinkApplicationContext.setNetworkServerPort(10001); // 服务端网络服务端口，默认 netty

		LinkApplicationContext.initialization();

		InvocationBootstrap invocationBootstrap = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> helloServiceInvocationConfig = new InvocationConfig<HelloService>();
		helloServiceInvocationConfig.setInterfaceClass(HelloService.class);
		helloServiceInvocationConfig.setTimeout(1000L);

		HelloService proxy = invocationBootstrap.getProxy(helloServiceInvocationConfig);

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				TimeUnit.SECONDS.sleep(1);
				String say = proxy.say(String.valueOf(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}