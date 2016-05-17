package io.sunyi.link.demo.simple;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyi
 */
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String content) {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Server say: [" + content + "]";
	}
}
