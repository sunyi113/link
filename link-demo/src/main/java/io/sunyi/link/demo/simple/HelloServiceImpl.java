package io.sunyi.link.demo.simple;

/**
 * @author sunyi
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String content) {
		return "Server say: [" + content + "]";
	}
}
