package com.lyz.backend.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

public class Configs
{
	private static Properties properties = new Properties();
	static
	{
		reloadConfigs();
	}

	private static boolean debug;

	public static boolean isDebug()
	{
		return debug;
	}

	/**
	 * 刷新配置文件
	 * 
	 * @return
	 */
	public static Properties reloadConfigs()
	{
		Properties properties = new Properties();
		InputStream in = Configs.class.getResourceAsStream("/generator.properties");
		try
		{
			properties.load(in);
			Configs.properties = properties;
			debug = getBool("DEBUG");
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			org.apache.commons.io.IOUtils.closeQuietly(in);
		}

		return properties;
	}

	public static String get(String name)
	{
		return properties.getProperty(name);
	}

	/**
	 * 获取一个配置列表，按逗号分割进行划分，并返回成set类型
	 * 
	 * @param name
	 * @return 如果未配置或配置为空，则返回一个空的list
	 */
	public static List<String> getAsList(String name)
	{
		List<String> set = new ArrayList<String>();

		String value = properties.getProperty(name);
		if (StringUtils.isNotBlank(value))
		{
			String[] vs = value.split(",");

			for (String v : vs)
			{
				if (StringUtils.isNotBlank(v.trim()))
					
					set.add(v.trim());
			}
		}
		return set;
	}

	public static Set<String> getAsSet(String name)
	{
		Set<String> set = new HashSet<String>();

		String value = properties.getProperty(name);
		if (StringUtils.isNotBlank(value))
		{
			String[] vs = value.split(",");

			for (String v : vs)
			{
				if (StringUtils.isNotBlank(v.trim()))
					set.add(v.trim());
			}
		}
		return set;
	}

	public static Integer getInt(String name)
	{
		return getInt(name, 0);
	}

	public static Integer getInt(String name, int def)
	{
		return NumberUtils.toInt(properties.getProperty(name), def);
	}

	public static long getLong(String name)
	{
		return Long.valueOf(properties.getProperty(name));
	}

	public static boolean getBool(String name)
	{
		String p = get(name);
		return p != null && (p.equals("1") || p.toLowerCase().equals("true"));
	}

	public String getText(String name, Object... args)
	{
		String k = get(name);
		if (k != null)
		{
			k = MessageFormat.format(k, args);
			return k;
		} else
		{
			return null;
		}
	}

	public static Properties getProperties()
	{
		return properties;
	}
}
