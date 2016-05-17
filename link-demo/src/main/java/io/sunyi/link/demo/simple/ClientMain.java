package io.sunyi.link.demo.simple;

import io.sunyi.link.core.body.AttachmentKeys;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.filter.InvocationFilter;
import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.invocation.invoker.Invoker;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");
		LinkApplicationContext.setNetworkServerPort(10001);

		LinkApplicationContext.addInvocationFilters(new InvocationFilter() {

			private int count = 0;
			private long tc = 0L;

			@Override
			public void preInvoke(Invoker invoker, RpcRequest rpcRequest) {

			}

			@Override
			public void afterInvoke(Invoker invoker, RpcRequest rpcRequest, RpcResponse rpcResponse) {
				count++;
				tc += Long.valueOf(rpcResponse.getAttachement(AttachmentKeys.TIME_CONSUMING));

				if (count % 100 == 0) {
					System.out.println("已经调用 " + count + " 次，总共耗时 " + tc + " ， 平均每次耗时 " + tc / count + "");
				}

			}
		});

		LinkApplicationContext.initialization();

		InvocationBootstrap invocationBootstrap = InvocationBootstrap.getInstance();

		InvocationConfig<HelloService> helloServiceInvocationConfig = new InvocationConfig<HelloService>();
		helloServiceInvocationConfig.setInterfaceClass(HelloService.class);
		helloServiceInvocationConfig.setTimeout(1000L);

		HelloService proxy = invocationBootstrap.getProxy(helloServiceInvocationConfig);

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				String say = proxy.say(String.valueOf(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}