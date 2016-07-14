package io.sunyi.link.demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sunyi
 */
public class ClientMain {

	public static void main(String args[]) throws InterruptedException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/link-spring-invocation.xml");

		HelloService helloService1 = ac.getBean(HelloService.class);

		System.out.println(helloService1.say("111"));


		HelloService helloService2 = ac.getBean(HelloService.class);

		System.out.println(helloService2.say("222"));

	}
}