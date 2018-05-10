package com.mobike.iotcloud.backend.framework.cache.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimedExpiredHashMap<K, V>
{
	private Map<K, CacheValue> map = new ConcurrentHashMap<>();

	private class CacheValue
	{
		long expiredTime;
		V value;

		CacheValue(V value, int exp)
		{
			super();
			this.value = value;
			this.expiredTime = System.currentTimeMillis() + exp * 1000;
		}

		/**
		 * 是否超时
		 * 
		 * @return
		 */
		public boolean expired()
		{

			return System.currentTimeMillis() > this.expiredTime;
		}
	}

	public V get(K key)
	{

		CacheValue entry = map.get(key);
		if (entry == null)
		{
			return null;
		}
		/* 超时返回null */
		if (entry.expired())
		{
			map.remove(key);
			return null;
		}

		return entry == null ? null : entry.value;
	}

	public V put(K key, V value, int exp)
	{
		CacheValue entry = new CacheValue(value, exp);
		map.put(key, entry);
		return value;
	}

	public static void main(String[] args)
	{
		TimedExpiredHashMap<String, String> map = new TimedExpiredHashMap<String, String>();

		map.put("ex", "hello", 3);

		map.put("nex", "world", 10);

		try
		{
			Thread.sleep(4000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(map.get("noExist") == null);
		System.out.println(map.get("ex") == null);
		System.out.println("world".equals(map.get("nex")));
	}

}
