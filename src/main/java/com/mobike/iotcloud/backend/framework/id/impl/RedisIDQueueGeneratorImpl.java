package com.mobike.iotcloud.backend.framework.id.impl;

import com.mobike.iotcloud.backend.framework.id.AbstractRedisIDGenerator;
import redis.clients.jedis.PipelineBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要预先生成好一组不重复的id， 然后客户端每次pop出来使用<br />
 * 主要应用在非自增规则的生成上, 例如用户编号<br />
 * TODO 需要人工监控确保key对应的redis中一直有数据，否则会造成整个系统错误
 *
 * @author luyongzhao
 */
public class RedisIDQueueGeneratorImpl extends AbstractRedisIDGenerator {
    private List<String> queue;

    private int nextIndex;
    private int maxIdx;

    public void init() {
        super.init();

        queue = new ArrayList<String>(cacheSize.intValue());
        for (int i = 0; i < cacheSize; i++) {
            queue.add("");
        }
        maxIdx = nextIndex = cacheSize - 1;
    }

    @Override
    public synchronized String nextStringID() {
        if (nextIndex >= maxIdx) {
            List<Object> caches = persistentCache.pipelined(new PipelineBlock() {
                @Override
                public void execute() {
                    for (int i = 0; i < cacheSize; i++) {
                        rpop(key);
                    }
                }
            });

            for (int i = 0; i < cacheSize; i++) {
                queue.set(i, (String) caches.get(i));
            }

            nextIndex = 0;
        } else {
            nextIndex++;
        }

        String prefix = prefix();
        if (prefix == null) {
            return queue.get(nextIndex);
        } else {
            return prefix + queue.get(nextIndex);
        }
    }


    @Override
    public Long nextLongID() {
        return Long.valueOf(nextStringID());
    }
}
