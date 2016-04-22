package io.sunyi.link.test;

import java.net.InetSocketAddress;

/**
 * @author sunyi
 */
public class Test {

	public static void main(String args[]) {


		InetSocketAddress i1 = new InetSocketAddress("192.168.1.2", 100);
		InetSocketAddress i2 = new InetSocketAddress("192.168.1.2", 100);


		System.out.println(i1.equals(i2) && i1.hashCode() == i2.hashCode());

	}

}
