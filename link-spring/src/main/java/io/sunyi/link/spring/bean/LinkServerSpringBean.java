package io.sunyi.link.spring.bean;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.server.ServerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunyi
 */
public class LinkServerSpringBean extends ServerConfig implements ApplicationContextAware, ApplicationListener, BeanNameAware {

	private ApplicationContext springAc;

	private String ref;

	private String beanName;

	private AtomicBoolean isExported = new AtomicBoolean(false);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springAc = applicationContext;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		boolean isExportedNow = isExported.compareAndSet(false, true);

		if (!isExportedNow) {
			return;
		}

		try {
			ServerBootstrap serverBootstrap = ServerBootstrap.getInstance();

			Object serverImplement = springAc.getBean(ref);
			this.setServerImplement(serverImplement);

			serverBootstrap.exportServer(this);

		} catch (LinkException e) {
			throw e;
		} catch (Throwable e) {
			throw new LinkException("Export link server error", e);
		}

	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public String getBeanName() {
		return beanName;
	}


	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
}
