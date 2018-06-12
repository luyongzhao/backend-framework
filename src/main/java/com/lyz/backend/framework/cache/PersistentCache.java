package com.lyz.backend.framework.cache;

import redis.clients.jedis.*;
import redis.clients.util.Slowlog;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 持久化缓存，数据丢失会影响业务
 */
public interface PersistentCache {

    public static int SECOND = 1;
    public static int MINUTE = 60;
    public static int HOUR = 3600;
    public static int DAY = 3600 * 24;

    boolean lreplace(String key, String oldValue, String newValue);

    int lreplaceReturnLength(String key, String oldValue, String newValue);

    String hoset(String key, Object bean);

    String ping();

    String set(String key, String value);

    String get(String key);


    Integer getInt(String key);

    String set(byte[] key, byte[] value);

    String quit();

    Boolean exists(String key);

    byte[] get(byte[] key);

    Long del(String... keys);

    Boolean exists(byte[] key);

    String type(String key);

    String flushDB();

    Long del(byte[]... keys);

    Set<String> keys(String pattern);

    String type(byte[] key);

    String randomKey();

    Set<byte[]> keys(byte[] pattern);

    String rename(String oldkey, String newkey);

    Long renamenx(String oldkey, String newkey);

    byte[] randomBinaryKey();

    String rename(byte[] oldkey, byte[] newkey);

    Long expire(String key, int seconds);

    Long renamenx(byte[] oldkey, byte[] newkey);

    Long expireAt(String key, long unixTime);

    Long dbSize();

    Long ttl(String key);

    Long expire(byte[] key, int seconds);

    String select(int index);

    Long move(String key, int dbIndex);

    Long expireAt(byte[] key, long unixTime);

    String flushAll();

    Long ttl(byte[] key);

    String getSet(String key, String value);

    List<String> mget(String... keys);

    List<Integer> mgetInt(String... keys);

    Long move(byte[] key, int dbIndex);

    Long setnx(String key, String value);

    byte[] getSet(byte[] key, byte[] value);

    String setex(String key, int seconds, String value);

    List<byte[]> mget(byte[]... keys);

    String mset(String... keysvalues);

    Long setnx(byte[] key, byte[] value);

    Long msetnx(String... keysvalues);

    String setex(byte[] key, int seconds, byte[] value);

    Long decrBy(String key, long integer);

    String mset(byte[]... keysvalues);

    Long decr(String key);

    Long incrBy(String key, long integer);

    Long msetnx(byte[]... keysvalues);

    Long incr(String key);

    Long decrBy(byte[] key, long integer);

    Long append(String key, String value);

    Long decr(byte[] key);

    String substr(String key, int start, int end);

    Long incrBy(byte[] key, long integer);

    Long hset(String key, String field, String value);

    Long incr(byte[] key);

    Long append(byte[] key, byte[] value);

    String hget(String key, String field);

    byte[] substr(byte[] key, int start, int end);

    Long hsetnx(String key, String field, String value);

    Long hset(byte[] key, byte[] field, byte[] value);

    String hmset(String key, Map<String, String> hash);

    byte[] hget(byte[] key, byte[] field);

    List<String> hmget(String key, String... fields);

    Long hsetnx(byte[] key, byte[] field, byte[] value);

    Long hincrBy(String key, String field, long value);

    String hmset(byte[] key, Map<byte[], byte[]> hash);

    Boolean hexists(String key, String field);

    List<byte[]> hmget(byte[] key, byte[]... fields);

    Long hdel(String key, String... fields);

    Long hincrBy(byte[] key, byte[] field, long value);

    Long hlen(String key);

    Boolean hexists(byte[] key, byte[] field);

    Set<String> hkeys(String key);

    Long hdel(byte[] key, byte[]... fields);

    List<String> hvals(String key);

    Long hlen(byte[] key);

    Map<String, String> hgetAll(String key);

    <T> T hgetAll(String key, Class<T> cls);

    <T> T hgetAll(String key, Class<T> cls, String... fileds);

    Set<byte[]> hkeys(byte[] key);

    Long rpush(String key, String... strings);

    List<byte[]> hvals(byte[] key);

    Long lpush(String key, String... strings);

    Map<byte[], byte[]> hgetAll(byte[] key);

    Long llen(String key);

    List<String> lrange(String key);

    List<String> lrange(String key, long start, long end);

    List<Integer> lrangeInteger(String key);

    List<Integer> lrangeInteger(String key, long start, long end);

    List<Integer> lrangeInteger(String key, long start);

    String ltrim(String key, long start, long end);

    Long rpush(byte[] key, byte[]... strings);

    String lindex(String key, long index);

    Long lpush(byte[] key, byte[]... strings);

    String lset(String key, long index, String value);

    Long llen(byte[] key);

    List<byte[]> lrange(byte[] key, int start, int end);

    Long lrem(String key, long count, String value);

    String ltrim(byte[] key, int start, int end);

    String lpop(String key);

    public List<String> lpopBatch(String key, int max);

    String rpop(String key);

    public List<String> rpopBatch(String key, int max);

    byte[] lindex(byte[] key, int index);

    String rpoplpush(String srckey, String dstkey);

    String lset(byte[] key, int index, byte[] value);

    Long sadd(String key, String... members);

    Long lrem(byte[] key, int count, byte[] value);

    Set<String> smembers(String key);

    byte[] lpop(byte[] key);

    Long srem(String key, String... members);

    byte[] rpop(byte[] key);

    byte[] rpoplpush(byte[] srckey, byte[] dstkey);

    String spop(String key);

    Long smove(String srckey, String dstkey, String member);

    Long sadd(byte[] key, byte[]... members);

    Set<byte[]> smembers(byte[] key);

    Long scard(String key);

    Boolean sismember(String key, String member);

    Long srem(byte[] key, byte[]... member);

    Set<String> sinter(String... keys);

    byte[] spop(byte[] key);

    Long smove(byte[] srckey, byte[] dstkey, byte[] member);

    Long sinterstore(String dstkey, String... keys);

    Long scard(byte[] key);

    Set<String> sunion(String... keys);

    Boolean sismember(byte[] key, byte[] member);

    Long sunionstore(String dstkey, String... keys);

    Set<byte[]> sinter(byte[]... keys);

    Set<String> sdiff(String... keys);

    Long sinterstore(byte[] dstkey, byte[]... keys);

    Long sdiffstore(String dstkey, String... keys);

    Set<byte[]> sunion(byte[]... keys);

    String srandmember(String key);

    Long zadd(String key, double score, String member);

    Long sunionstore(byte[] dstkey, byte[]... keys);

    Long zadd(String key, Map<String, Double> scoreMembers);

    Set<byte[]> sdiff(byte[]... keys);

    Set<String> zrange(String key, long start, long end);

    Long sdiffstore(byte[] dstkey, byte[]... keys);

    byte[] srandmember(byte[] key);

    Long zrem(String key, String... members);

    Long zadd(byte[] key, double score, byte[] member);

    Double zincrby(String key, double score, String member);

    Long zadd(byte[] key, Map<byte[], Double> scoreMembers);

    Long zrank(String key, String member);

    Set<byte[]> zrange(byte[] key, int start, int end);

    Long zrevrank(String key, String member);

    Set<String> zrevrange(String key, long start, long end);

    Long zrem(byte[] key, byte[]... members);

    Double zincrby(byte[] key, double score, byte[] member);

    Set<Tuple> zrangeWithScores(String key, long start, long end);

    Long zrank(byte[] key, byte[] member);

    Set<Tuple> zrevrangeWithScores(String key, long start, long end);

    Long zrevrank(byte[] key, byte[] member);

    Long zcard(String key);

    Set<byte[]> zrevrange(byte[] key, int start, int end);

    Double zscore(String key, String member);

    Set<Tuple> zrangeWithScores(byte[] key, int start, int end);

    String watch(String... keys);

    List<String> sort(String key);

    Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end);

    List<String> sort(String key, SortingParams sortingParameters);

    Long zcard(byte[] key);

    List<String> blpop(int timeout, String... keys);

    Double zscore(byte[] key, byte[] member);

    List<Object> multi(TransactionBlock jedisTransaction);

    Long sort(String key, SortingParams sortingParameters, String dstkey);

    Long sort(String key, String dstkey);

    List<String> brpop(int timeout, String... keys);

    String watch(byte[]... keys);

    String unwatch();

    String auth(String password);

    List<byte[]> sort(byte[] key);

    List<byte[]> sort(byte[] key, SortingParams sortingParameters);

    List<byte[]> blpop(int timeout, byte[]... keys);

    Long publish(String channel, String message);

    void psubscribe(JedisPubSub jedisPubSub, String... patterns);

    Long zcount(String key, double min, double max);

    Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey);

    Long zcount(String key, String min, String max);

    Long sort(byte[] key, byte[] dstkey);

    Set<String> zrangeByScore(String key, double min, double max);

    List<byte[]> brpop(int timeout, byte[]... keys);

    Set<String> zrangeByScore(String key, String min, String max);

    Set<String> zrangeByScore(String key, double min, double max, int offset,
                              int count);

    Set<String> zrangeByScore(String key, String min, String max, int offset,
                              int count);

    public <T> List<T> pipelined(PipelineBlock jedisPipeline);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max,
                                       int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max,
                                       int offset, int count);

    Long zcount(byte[] key, double min, double max);

    Long zcount(byte[] key, byte[] min, byte[] max);

    Set<byte[]> zrangeByScore(byte[] key, double min, double max);

    Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max);

    Set<String> zrevrangeByScore(String key, double max, double min);

    Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset,
                              int count);

    Set<String> zrevrangeByScore(String key, String max, String min);

    Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset,
                              int count);

    Set<String> zrevrangeByScore(String key, double max, double min, int offset,
                                 int count);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,
                                          int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max,
                                       int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
                                       int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min,
                                          int offset, int count);

    Set<String> zrevrangeByScore(String key, String max, String min, int offset,
                                 int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);

    Set<byte[]> zrevrangeByScore(byte[] key, double max, double min);

    Long zremrangeByRank(String key, long start, long end);

    Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min);

    Long zremrangeByScore(String key, double start, double end);

    Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset,
                                 int count);

    Long zremrangeByScore(String key, String start, String end);

    Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset,
                                 int count);

    Long zunionstore(String dstkey, String... sets);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min);

    Long zunionstore(String dstkey, ZParams params, String... sets);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min,
                                          int offset, int count);

    Long zinterstore(String dstkey, String... sets);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min);

    Long zinterstore(String dstkey, ZParams params, String... sets);

    Long strlen(String key);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
                                          int offset, int count);

    Long lpushx(String key, String string);

    Long persist(String key);

    Long zremrangeByRank(byte[] key, int start, int end);

    Long rpushx(String key, String string);

    Long zremrangeByScore(byte[] key, double start, double end);

    String echo(String string);

    Long zremrangeByScore(byte[] key, byte[] start, byte[] end);

    Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value);

    Long zunionstore(byte[] dstkey, byte[]... sets);

    String brpoplpush(String source, String destination, int timeout);

    Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets);

    Boolean setbit(String key, long offset, boolean value);

    Long zinterstore(byte[] dstkey, byte[]... sets);

    Boolean getbit(String key, long offset);

    Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets);

    Long setrange(String key, long offset, String value);

    String save();

    String getrange(String key, long startOffset, long endOffset);

    String bgsave();

    List<String> configGet(String pattern);

    String bgrewriteaof();

    String configSet(String parameter, String value);

    Long lastsave();

    String shutdown();

    Object eval(String script, int keyCount, String... params);

    String info();

    String slaveof(String host, int port);

    Object eval(String script, List<String> keys, List<String> args);

    String slaveofNoOne();

    Object eval(String script);

    List<byte[]> configGet(byte[] pattern);

    Object evalsha(String script);

    String configResetStat();

    byte[] configSet(byte[] parameter, byte[] value);

    boolean isConnected();

    Long strlen(byte[] key);

    Object evalsha(String sha1, List<String> keys, List<String> args);

    Long lpushx(byte[] key, byte[] string);

    Object evalsha(String sha1, int keyCount, String... params);

    Long persist(byte[] key);

    Long rpushx(byte[] key, byte[] string);

    Boolean scriptExists(String sha1);

    byte[] echo(byte[] string);

    List<Boolean> scriptExists(String... sha1);

    Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value);

    String debug(DebugParams params);

    String scriptLoad(String script);

    Client getClient();

    List<Slowlog> slowlogGet();

    byte[] brpoplpush(byte[] source, byte[] destination, int timeout);

    List<Slowlog> slowlogGet(long entries);

    Long objectRefcount(String string);

    Boolean setbit(byte[] key, long offset, byte[] value);

    String objectEncoding(String string);

    Boolean getbit(byte[] key, long offset);

    Long objectIdletime(String string);

    Long setrange(byte[] key, long offset, byte[] value);

    byte[] getrange(byte[] key, long startOffset, long endOffset);

    Long publish(byte[] channel, byte[] message);

    void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns);

    Long getDB();

    Object eval(byte[] script, List<byte[]> keys, List<byte[]> args);

    Object eval(byte[] script, byte[] keyCount, byte[][] params);

    String scriptFlush();

    List<Long> scriptExists(byte[]... sha1);

    byte[] scriptLoad(byte[] script);

    String scriptKill();

    String slowlogReset();

    long slowlogLen();

    List<byte[]> slowlogGetBinary();

    List<byte[]> slowlogGetBinary(long entries);

    Long objectRefcount(byte[] key);

    byte[] objectEncoding(byte[] key);

    Long objectIdletime(byte[] key);
}
