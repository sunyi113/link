package io.sunyi.link.core.utils;

import java.lang.management.ManagementFactory;

/**
 * @author sunyi
 */
public class PidUtils {


	public static Integer getPid() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		if (name != null && name.indexOf("@") > 0) {
			String pid = name.split("@")[0];
			return Integer.valueOf(pid);
		} else {
			return -1;
		}
	}

}
