package io.sunyi.link.demo.spring;

/**
 * @author sunyi
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String content) {
		return "Server say: [" + content + "]";
	}
}
