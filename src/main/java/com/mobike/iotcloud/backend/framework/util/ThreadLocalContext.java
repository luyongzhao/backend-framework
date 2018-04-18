package com.mobike.iotcloud.backend.framework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个总的threadLocal,可做缓存， 做请求单例， 省的到处写threadLocal和清理<br />
 * 只要使用了就必须清理
 * 
 * @author luyongzhao
 * 
 */
public class ThreadLocalContext
{
	private static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> cls)
	{
		return (T) get().get(cls);
	}

	public static Object get(Object key)
	{
		return get().get(key);
	}

	public static void put(Object key, Object o)
	{
		get().put(key, o);
	}

	public static void put(Object o)
	{
		if (o != null)
			get().put(o.getClass(), o);
	}

	public static void remove(Object key)
	{
		get().remove(key);
	}

	private static Map<Object, Object> get()
	{
		Map<Object, Object> map = threadLocal.get();
		if (map == null)
		{
			map = new HashMap<Object, Object>();
			threadLocal.set(map);
		}

		return map;
	}

	/**
	 * 调用完成后必须清理
	 */
	public static void clean()
	{
		threadLocal.remove();
	}
}
