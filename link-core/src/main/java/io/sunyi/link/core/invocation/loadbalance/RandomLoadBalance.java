package io.sunyi.link.core.invocation.loadbalance;

import io.sunyi.link.core.invocation.invoker.Invoker;

import java.util.List;
import java.util.Random;

/**
 * @author sunyi
 */
public class RandomLoadBalance implements LoadBalance {

	private Random random = new Random();

	@Override
	public <T> Invoker<T> getInvoker(List<Invoker<T>> invokers) {

		if (invokers == null || invokers.size() == 0) {
			return null;
		}

		int i = random.nextInt(invokers.size());

		return invokers.get(i);
	}
}
