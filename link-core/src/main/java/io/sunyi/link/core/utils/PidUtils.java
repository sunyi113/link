package io.sunyi.link.core.utils;

import java.lang.management.ManagementFactory;

/**
 * @author sunyi
 */
public class PidUtils {

	private static Integer cachedPid = null;

	public static Integer getPid() {

		if (cachedPid != null) {
			return cachedPid;
		}

		String name = ManagementFactory.getRuntimeMXBean().getName();
		if (name != null && name.indexOf("@") > 0) {
			String pid = name.split("@")[0];
			cachedPid = Integer.valueOf(pid);
			return cachedPid;
		} else {
			return -1;
		}
	}


}
