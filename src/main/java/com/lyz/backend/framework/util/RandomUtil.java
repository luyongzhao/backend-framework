package com.lyz.backend.framework.util;

import java.util.Random;

/**
 * 随机字符串
 *
 * @author luyongzhao
 */
public class RandomUtil {
    private static final String[] chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "0"};

    private static Random random = new Random();

    public static final String randomNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static final String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }

        return sb.toString();
    }

    /**
     * 概率随机 是或者否
     *
     * @param baseNum   基数
     * @param probitNum 基数基础之上的概率
     * @return 返回true的概率为probitNum/baseNum
     */
    public static boolean randomBoolean(int baseNum, int probitNum) {

        if (baseNum <= 0 || probitNum < 0) {
            return false;
        }

        if (probitNum == 0) {
            return false;
        }

        if (probitNum >= baseNum) {
            return true;
        }

        return random(baseNum) <= probitNum;

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.randomBoolean(10, 8));
        }
    }

    /**
     * 获取区间内的随机整数
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public static Integer randomIntegerNumber(int iStart, int iEnd) {
        int start = 0;
        int end = 0;
        if (iStart > iEnd) {
            start = iEnd;
            end = iStart;
        } else {
            start = iStart;
            end = iEnd;
        }

        return (int) (start + Math.random() * ((end - start + 1)));
    }

    /**
     * 获取区间内的随机整数
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public static long randomIntegerNumber(long iStart, long iEnd) {
        long start = 0;
        long end = 0;
        if (iStart > iEnd) {
            start = iEnd;
            end = iStart;
        } else {
            start = iStart;
            end = iEnd;
        }

        return (long) (start + Math.random() * ((end - start + 1)));
    }

    public static int random(int size) {
        return random.nextInt(size);
    }

}
