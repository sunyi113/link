package io.sunyi.link.core.invocation.loadbalance;

import io.sunyi.link.core.invocation.invoker.Invoker;

import java.util.List;

/**
 *
 * 服务均衡
 *
 * @author sunyi
 */
public interface LoadBalance {

	<T> Invoker<T> getInvoker(List<Invoker<T>> invokers);

}
