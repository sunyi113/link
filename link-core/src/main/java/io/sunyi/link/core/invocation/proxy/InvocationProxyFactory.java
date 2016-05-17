package io.sunyi.link.core.invocation.proxy;

import io.sunyi.link.core.invocation.InvocationConfig;

/**
 * 代理对象工厂<br/>
 * 需要返回一个代理对象。
 *
 * @author sunyi
 */
public interface InvocationProxyFactory {

	<T> T getProxy(InvocationConfig<T> invocationConfig);

}
