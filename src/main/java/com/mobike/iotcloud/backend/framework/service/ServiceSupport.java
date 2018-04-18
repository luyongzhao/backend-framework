package com.mobike.iotcloud.backend.framework.service;


import com.mobike.iotcloud.backend.framework.cache.PersistentCache;
import com.mobike.iotcloud.backend.framework.controller.bean.RestPageLimit;
import com.mobike.iotcloud.backend.framework.controller.bean.RestPageMore;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class ServiceSupport
{
	/**
	 * 重启或异常宕机将会失效的异步任务，本进程内存内异步队列执行, 仅用于日志等非重要任务
	 * 
	 * @param task
	 */
	public static void asyncUnstableTask(Runnable task)
	{
		if (asyncUnstableExecutorService == null)
		{// 要发消息必然要先启动消息消费者
			startAsyncBatchConsumerThread();
		}

		asyncUnstableExecutorService.execute(task);
	}

	// 这里使用facade保证所有facade对应一个队列
	private static ExecutorService asyncUnstableExecutorService = null;

	private static synchronized void startAsyncBatchConsumerThread()
	{
		if (asyncUnstableExecutorService == null)
		{
			asyncUnstableExecutorService = Executors.newCachedThreadPool(new ThreadFactory()
			{
				@Override
				public Thread newThread(Runnable r)
				{
					Thread thread = new Thread(r);
					thread.setDaemon(true);
					return thread;
				}
			});
		}
	}

	/**
	 * persistentCache ZSet下标分页逻辑, 倒序排列
	 * 
	 * @param persistentCache
	 * @param persistentCacheKey
	 * @param idx
	 * @param pageRowDisplay
	 * @return 如果有数据就会返回zset中对应区间内的id列表
	 */
	public RestPageMore pageMoreForpersistentCacheZSetDesc(PersistentCache persistentCache, String persistentCacheKey, Integer idx, Integer pageRowDisplay)
	{
		if (idx == null || idx.intValue() < 0)
		{
			idx = 0;
		}

		Long countSize = persistentCache.zcard(persistentCacheKey);
		Integer count = countSize.intValue();

		RestPageMore page = new RestPageMore();
		if (idx > count)
		{// 请求的下标比实际最大数量要大，返回服务端下标， 客户端以服务端下标为准
			page.more(false);
			page.idx(count);
			return page;
		}

		if (count == 0)
		{// 没有记录
			page.more(false);
			page.idx(0);
		} else
		{
			boolean isFirstRequest = idx.intValue() == 0;

			if (isFirstRequest)
			{
				idx = count - 1;
			} else
			{
				idx = idx - 1;
			}

			// 注意: 1、下标是0本位的， 2、查询方法zrange.... 是双向包含区间， 例如3~10,
			// 会返回包含3和10以及中间的所有下标数据
			// 4 ---> 3, to: 3, from: 3-4 = -1
			Integer from = idx - pageRowDisplay + 1;
			Integer to = idx;

			if (from < 0)
			{// 下一次没有idx了
				from = 0;
			}
			page.more(from > 0);
			page.idx(from);

			Collection<String> ids = persistentCache.zrange(persistentCacheKey, from, to);
			if (ids != null && ids.size() > 0)
			{
				List<String> list = new ArrayList<String>(ids);
				Collections.reverse(list);
				page.list(list);
			}
		}

		return page;
	}

	public RestPageLimit pageLimitFrompersistentCacheZsetDesc(PersistentCache persistentCache, String key, Long page, Integer pageDisplay)
	{
		return pageLimitFrompersistentCacheZsetDesc(persistentCache, key, page, pageDisplay, false);
	}

	/**
	 * 分页算法归并,从队尾拿去
	 * 
	 * @param persistentCache
	 * @param key
	 * @param page
	 * @param pageDisplay
	 * @return
	 */
	public RestPageLimit pageLimitFrompersistentCacheZsetDesc(PersistentCache persistentCache, String key, Long page, Integer pageDisplay,
															  boolean needTotalCount)
	{
		if (page == null || page.intValue() < 0)
		{
			page = 1L;
		}

		long totalRow = persistentCache.zcard(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageDisplay == 0 ? totalRow / pageDisplay : totalRow / pageDisplay + 1;
		limit.pageTotal(pageTotal);
		limit.page(page);

		long start = (page - 1) * pageDisplay;
		long end = page * pageDisplay - 1;

		if (needTotalCount)
		{
			limit.put("totalCount", totalRow);
		}

		Set<String> ids = persistentCache.zrevrange(key, start, end);

		limit.list(new ArrayList<String>(ids));

		return limit;
	}
	
	/**
	 * 分页算法归并,从队尾拿去
	 * 
	 * @param persistentCache
	 * @param key
	 * @param page
	 * @param pageDisplay
	 * @return
	 */
	public RestPageLimit pageLimitFrompersistentCacheZset(PersistentCache persistentCache, String key, Long page, Integer pageDisplay,
			boolean needTotalCount)
	{
		if (page == null || page.intValue() < 0)
		{
			page = 1L;
		}

		long totalRow = persistentCache.zcard(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageDisplay == 0 ? totalRow / pageDisplay : totalRow / pageDisplay + 1;
		limit.pageTotal(pageTotal);
		limit.page(page);

		long start = (page - 1) * pageDisplay;
		long end = page * pageDisplay - 1;

		if (needTotalCount)
		{
			limit.put("totalCount", totalRow);
		}

		Set<String> ids = persistentCache.zrange(key, start, end);

		limit.list(new ArrayList<String>(ids));

		return limit;
	}

	/**
	 * 从persistentCache List中获取分页对象， 分页规则为从左至右
	 * 
	 * @param persistentCache
	 * @param key
	 * @param page
	 * @param pageDisplay
	 * @return
	 */
	public RestPageLimit pageLimitFrompersistentCacheQueue(PersistentCache persistentCache, String key, Long page, Integer pageDisplay)
	{
		if (page == null || page.intValue() < 0)
		{
			page = 1L;
		}

		long totalRow = persistentCache.llen(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageDisplay == 0 ? totalRow / pageDisplay : totalRow / pageDisplay + 1;
		limit.pageTotal(pageTotal);
		limit.page(page);

		long start = (page - 1) * pageDisplay;
		long end = page * pageDisplay - 1;

		limit.put("totalCount", totalRow);

		List<String> ids = persistentCache.lrange(key, start, end);
		limit.list(ids);
		return limit;
	}
}
