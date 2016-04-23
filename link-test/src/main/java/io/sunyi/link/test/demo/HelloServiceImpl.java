package io.sunyi.link.test.demo;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyi
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String content) {
		return "Server say: [" + content + "]";
	}
}
