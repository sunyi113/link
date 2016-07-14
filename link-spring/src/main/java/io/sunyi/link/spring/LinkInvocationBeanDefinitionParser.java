package io.sunyi.link.spring;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.spring.bean.LinkInvocationSpringBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author sunyi
 */
public class LinkInvocationBeanDefinitionParser implements BeanDefinitionParser {
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {

		try {
			String id = element.getAttribute("id");
			Class interf = Class.forName(element.getAttribute("interface"));
			int timeout = Integer.valueOf(element.getAttribute("timeout"));
			int retryTimes = Integer.valueOf(element.getAttribute("retry-times"));

			RootBeanDefinition invocationBean = new RootBeanDefinition();
			invocationBean.setBeanClass(LinkInvocationSpringBean.class);
			invocationBean.setLazyInit(false);
			invocationBean.setScope(BeanDefinition.SCOPE_SINGLETON);

			invocationBean.getPropertyValues().addPropertyValue("interfaceClass", interf);
			invocationBean.getPropertyValues().addPropertyValue("timeout", timeout);
			invocationBean.getPropertyValues().addPropertyValue("retryTimes", retryTimes);
			invocationBean.getPropertyValues().addPropertyValue("objectType", interf);

			parserContext.getRegistry().registerBeanDefinition(id, invocationBean);

		} catch (Exception e) {
			throw new LinkException("Parse link:invocation element fail.", e);
		}

		return null;
	}
}
