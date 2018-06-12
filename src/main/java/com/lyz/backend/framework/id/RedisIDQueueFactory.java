package com.lyz.backend.framework.id;

import com.lyz.backend.framework.util.RandomUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 生成稀疏队列ID文件的main方法
 *
 * @author luyongzhao
 */
public class RedisIDQueueFactory {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        // String key = "_CORE_MEETIN_ORDER_ID_QUEUE";
        // String key = "_CORE_MEETIN_ACTIVE_ID_QUEUE";
        // String key = "_CORE_PAYCODE_ID_QUEUE";
        String key = "_CORE_ARTICLE_ID_QUEUE";
        Integer percentage = 90;
        // String outputDir = "e://test/";
        String outputDir = "/Users/kevin/Documents/workspace/Taste/doc/idGenerator";

        // for (int i = 1; i <= 1; i++)
        // {
        // Integer start = 10001 * i;
        // Integer end = 20000;
        //
        // generator(host, key, start, end, percentage, outputDir);
        // }

        // randomPayCode(host, key, 100000000000L, 999999999999L, outputDir, 20,
        // 1000000);

        generator(host, key, 800000, 900000, percentage, outputDir);

    }

    private static void generator(String host, String key, Integer start, Integer end, Integer percentage,
                                  String outputDir) throws IOException {
        File outputFile = new File(outputDir);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
        outputFile = new File(outputFile, key + "_" + start + "_" + end + ".sh");
        System.out.println(outputFile.getAbsolutePath());
        _generator(host, key, start, end, percentage, outputFile);
    }

    private static void _generator(String host, String key, Integer start, Integer end, Integer percentage,
                                   File outputFile) throws IOException {
        int count = end - start;
        List<Integer> queue = new ArrayList<Integer>(count);
        for (int i = start; i < end; i++) {
            queue.add(i);
        }

        int resultCount = count * percentage / 100;

        FileWriter fw = new FileWriter(outputFile);

        if (StringUtils.isNotBlank(host)) {
            fw.write("redis-cli -h " + host + " lpush " + key);
        } else {
            fw.write("redis-cli lpush " + key);
        }
        Random rd = new Random();
        while (resultCount > 0) {
            int index = rd.nextInt(resultCount);
            fw.write(" " + queue.remove(index));
            resultCount--;
        }
        fw.write("\n");
        fw.close();
    }

    public static void randomPayCode(String host, String key, Long start, Long end, String outputDir,
                                     int fileCount, int size) throws IOException {
        Date d = new Date();

        Map<Long, Integer> m = new HashMap<Long, Integer>();
        List<Long> longList = new ArrayList<Long>(size);

        for (int i = 0; i < 2 * size; i++) {
            long l = RandomUtil.randomIntegerNumber(100000000000L, 999999999999L);
            if (m.containsKey(l)) {
                continue;
            } else {
                m.put(l, 1);
            }
            longList.add(l);

            if (longList.size() == size) {
                break;
            }

        }

        int singleCount = size / fileCount;

        int idx = 0;
        for (int i = 0; i < fileCount; i++) {

            File outputFile = new File(outputDir);
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            outputFile = new File(outputFile, key + "_" + start + "_" + end + "_" + i + ".sh");
            System.out.println(outputFile.getAbsolutePath());
            FileWriter fw = new FileWriter(outputFile);

            if (StringUtils.isNotBlank(host)) {
                fw.write("redis-cli -h " + host + " lpush " + key);
            } else {
                fw.write("redis-cli lpush " + key);
            }
            for (int k = 0; k < singleCount; k++) {
                fw.write(" " + longList.get(idx));
                idx++;
            }

            fw.write("\n");
            fw.close();
        }

        Date d2 = new Date();
        System.out.println("finished " + (d2.getTime() - d.getTime()));
    }
}
