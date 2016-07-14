package io.sunyi.link.spring;

import io.sunyi.link.core.commons.LinkApplicationContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author sunyi
 */
public class LinkApplicationBeanDefinitionParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {

		String registryUrl = element.getAttribute("registry-url");
		String serverPortStr = element.getAttribute("server-port");

		LinkApplicationContext.setRegistryUrl(registryUrl);
		if (serverPortStr != null && serverPortStr.length() > 0) {
			LinkApplicationContext.setServerPort(Integer.valueOf(serverPortStr));
		}

		//  TODO Component

		LinkApplicationContext.initialization();


		return null;
	}
}
