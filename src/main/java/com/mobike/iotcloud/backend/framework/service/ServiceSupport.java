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
	 * @param idx 列表偏移结束位置，从1开始
	 * @param pageSize
	 * @return 如果有数据就会返回zset中对应区间内的id列表
	 */
	public RestPageMore pageMoreForPersistentCacheZSetDesc(PersistentCache persistentCache, String persistentCacheKey, Integer idx, Integer pageSize)
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
			Integer from = idx - pageSize + 1;
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

	public RestPageLimit pageLimitFromPersistentCacheZsetDesc(PersistentCache persistentCache, String key, Long pageNo, Integer pageSize)
	{
		return pageLimitFromPersistentCacheZsetDesc(persistentCache, key, pageNo, pageSize, false);
	}

	/**
	 * 分页算法归并,从队尾拿去
	 * 
	 * @param persistentCache
	 * @param key
	 * @param pageNo 页码，从1开始
	 * @param pageSize 每一页显示的数量
	 * @param needTotalCount 是否需要显示总数量
	 * @return
	 */
	public RestPageLimit pageLimitFromPersistentCacheZsetDesc(PersistentCache persistentCache, String key, Long pageNo, Integer pageSize,
															  boolean needTotalCount)
	{
		if (pageNo == null || pageNo.intValue() < 0)
		{
			pageNo = 1L;
		}

		long totalRow = persistentCache.zcard(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
		limit.pageTotal(pageTotal);
		limit.page(pageNo);

		long start = (pageNo - 1) * pageSize;
		long end = pageNo * pageSize - 1;

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
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public RestPageLimit pageLimitFromPersistentCacheZset(PersistentCache persistentCache, String key, Long pageNo, Integer pageSize,
			boolean needTotalCount)
	{
		if (pageNo == null || pageNo.intValue() < 0)
		{
			pageNo = 1L;
		}

		long totalRow = persistentCache.zcard(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
		limit.pageTotal(pageTotal);
		limit.page(pageNo);

		long start = (pageNo - 1) * pageSize;
		long end = pageNo * pageSize - 1;

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
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public RestPageLimit pageLimitFromPersistentCacheQueue(PersistentCache persistentCache, String key, Long pageNo, Integer pageSize)
	{
		if (pageNo == null || pageNo.intValue() < 0)
		{
			pageNo = 1L;
		}

		long totalRow = persistentCache.llen(key);

		RestPageLimit limit = new RestPageLimit();
		long pageTotal = totalRow % pageSize == 0 ? totalRow / pageSize : totalRow / pageSize + 1;
		limit.pageTotal(pageTotal);
		limit.page(pageNo);

		long start = (pageNo - 1) * pageSize;
		long end = pageNo * pageSize - 1;

		limit.put("totalCount", totalRow);

		List<String> ids = persistentCache.lrange(key, start, end);
		limit.list(ids);
		return limit;
	}
}
