package io.sunyi.link.test.demo;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyi
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String content) {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return content;
	}
}
