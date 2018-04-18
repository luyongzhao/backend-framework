package com.mobike.iotcloud.backend.framework.util;

import java.io.Serializable;
import java.util.Collection;

public class JoinHelper {

    public static String joinToSql(Collection<?> coll) {
        StringBuilder sb = new StringBuilder();
        for (Object s : coll) {
            sb.append(",'");
            sb.append(s).append("'");
        }
        String s = sb.toString();
        return s.substring(1);
    }

    public static String joinToSql(String[] coll) {
        StringBuilder sb = new StringBuilder();
        for (Object s : coll) {
            sb.append(",'");
            sb.append(s).append("'");
        }
        String s = sb.toString();
        return s.substring(1);
    }

    public static String joinToSql(Integer[] coll) {
        StringBuilder sb = new StringBuilder();
        for (Object s : coll) {
            sb.append(s).append(",");
        }
        String s = sb.toString();
        return s.substring(0, s.length() - 2);
    }

    /**
     * 合并数组为一个字符串
     *
     * @param results
     * @param split
     * @return
     */
    public static String join(String[] results, String split) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < results.length; i++) {
            sb.append(results[i]);
            if (i != results.length - 1) {
                sb.append(split);
            }
        }

        return sb.toString();
    }

    /**
     * 合并数组为一个字符串
     *
     * @param set
     * @param split
     * @return
     */
    public static String join(Collection<? extends Serializable> set, String split) {
        if (set.size() == 0) {
            return null;
        }

        if (set.size() == 1) {
            return set.iterator().next().toString();
        }

        StringBuilder sb = new StringBuilder();

        for (Serializable str : set) {
            sb.append(split).append(str.toString());
        }

        return sb.toString().substring(split.length());
    }
}
