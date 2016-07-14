package io.sunyi.link.demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sunyi
 */
public class ServerMain {

	public static void main(String args[]) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/link-spring-server.xml");
	}
}