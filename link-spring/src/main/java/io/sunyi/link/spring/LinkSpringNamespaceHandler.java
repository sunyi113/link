package io.sunyi.link.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author sunyi
 */
public class LinkSpringNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new LinkApplicationBeanDefinitionParser());
		registerBeanDefinitionParser("server", new LinkServerBeanDefinitionParser());
		registerBeanDefinitionParser("invocation", new LinkInvocationBeanDefinitionParser());
	}


}
