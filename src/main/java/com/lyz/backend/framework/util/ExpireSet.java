package com.lyz.backend.framework.util;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 会值过期的Set
 *
 * @param <K>
 * @param <V>
 * @author luyongzhao
 */
public class ExpireSet<K> extends HashSet<K> {

    /**
     *
     */
    private static final long serialVersionUID = 3883547126660410769L;

    private Queue<KeyValue<K>> queue = new ConcurrentLinkedQueue<KeyValue<K>>();

    private int period = 60;
    private Set<K> set = this;

    public ExpireSet() {
    }

    /**
     * @param period 过期时间(秒)
     */
    public ExpireSet(int period) {
        this.period = period;


        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyValue<K> kv = null;
                long currentTime = System.currentTimeMillis();
                while (true) {
                    try {
                        kv = queue.peek();
                        if (kv != null && kv.value < currentTime) {
                            set.remove(kv.key);
                            queue.poll();
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 1000 * 10, 1000 * period);

    }

    @Override
    public boolean add(K e) {
        queue.offer(new KeyValue<K>(e, System.currentTimeMillis() + 1000 * period));
        return super.add(e);
    }

    protected class KeyValue<KK> {
        KK key;
        long value;

        public KeyValue(KK key, long value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) throws Exception {
        ExpireSet<String> set = new ExpireSet<>();
        for (int i = 0; i < 10; i++) {
            set.add(String.valueOf(i));
        }
        Thread.sleep(100000);
    }
}
