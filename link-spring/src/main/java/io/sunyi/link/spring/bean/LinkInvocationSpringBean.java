package io.sunyi.link.spring.bean;

import io.sunyi.link.core.invocation.InvocationBootstrap;
import io.sunyi.link.core.invocation.InvocationConfig;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author sunyi
 */
public class LinkInvocationSpringBean extends InvocationConfig  implements FactoryBean, BeanNameAware{

	private Class objectType;
	private String beanName;

	@Override
	public Object getObject() throws Exception {


		InvocationBootstrap invocationBootstrap = InvocationBootstrap.getInstance();
		Object proxy = invocationBootstrap.getProxy(this);

		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return objectType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public void setObjectType(Class objectType) {
		this.objectType = objectType;
	}
}
