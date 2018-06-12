package com.lyz.backend.framework.util;


import java.net.URL;

/**
 * 判断操作系统是否为linux
 */
public class OSUtil {
	public static final String CLASS_PATH;
	public static final boolean isLinux;
	static {
		URL resource = OSUtil.class.getResource("OSUtil.class");
		String classPath = resource.getPath();
		String className = OSUtil.class.getName().replace('.', '/') + ".class";
		String classesPath = classPath.substring(0, classPath
				.indexOf(className));
		//System.out.println(resource.getPath());

		if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1
				&& classesPath.startsWith("/")) {
			classesPath = classesPath.substring(1);
			isLinux = false;
		} else {
			isLinux = true;
		}
		CLASS_PATH = classesPath;
	}

	public static void main(String arg[]) {

		System.out.println(OSUtil.isLinux);
	}
}
