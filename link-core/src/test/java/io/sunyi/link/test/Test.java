package io.sunyi.link.test;

import com.alibaba.fastjson.JSONObject;

/**
 * @author sunyi
 */
public class Test {

	static interface AA {

	}

	static interface A extends AA {

	}

	static class B implements A {
	}

	public static void main(String args[]) {

		JSONObject json = new JSONObject();

		json.put("class", Test.class);

		System.out.println(json.toJSONString());

	}

}
