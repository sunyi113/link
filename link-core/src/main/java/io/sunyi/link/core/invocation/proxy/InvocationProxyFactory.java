package io.sunyi.link.core.invocation.proxy;

import io.sunyi.link.core.invocation.InvocationConfig;

/**
 * @author sunyi
 */
public interface InvocationProxyFactory {


	<T> T getObject(InvocationConfig<T> invocationConfig);

}
