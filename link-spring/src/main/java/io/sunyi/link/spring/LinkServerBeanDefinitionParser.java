package io.sunyi.link.spring;

import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.spring.bean.LinkServerSpringBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author sunyi
 */
public class LinkServerBeanDefinitionParser implements BeanDefinitionParser {
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {

		RootBeanDefinition serverBean = new RootBeanDefinition();

		try {
			String interf = element.getAttribute("interface");
			String ref = element.getAttribute("ref");

			if (interf == null || interf.length() == 0) {
				throw new LinkException("interf must not be blank");
			}

			if (ref == null || ref.length() == 0) {
				throw new LinkException("ref must not be blank");
			}


			serverBean.setBeanClass(LinkServerSpringBean.class);
			serverBean.setLazyInit(false);
			serverBean.setScope(BeanDefinition.SCOPE_SINGLETON);

			Class<?> interfaceClass = Class.forName(interf);
			serverBean.getPropertyValues().addPropertyValue("interfaceClass", interfaceClass);
			serverBean.getPropertyValues().addPropertyValue("ref", ref);

			parserContext.getRegistry().registerBeanDefinition(interf + "$" + LinkServerSpringBean.class.getSimpleName(), serverBean);
		} catch (Exception e) {
			throw new LinkException("Parse link:server element fail.", e);
		}

		return serverBean;
	}

}
