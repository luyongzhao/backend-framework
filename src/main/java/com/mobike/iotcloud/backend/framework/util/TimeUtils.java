package com.mobike.iotcloud.backend.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
    private static final SimpleDateFormat SDF_MD = new SimpleDateFormat("MM-dd");
    private static final SimpleDateFormat SDF_YMD = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 格式化日期
     *
     * @param ms
     * @return
     */
    public static String dateDescribe(long ms) {
        Calendar c = Calendar.getInstance();
        long t = (c.getTimeInMillis() - ms) / 1000;
        if (t < SECS.MINUS * 1) {
            return "刚刚";
        } else if (t < SECS.HOUR) {
            return t / SECS.MINUS + "分钟前";
        } else if (t < SECS.DAY) {
            return t / SECS.HOUR + "小时前";
        } else if (t < SECS.DAY * 7) {
            return t / (SECS.DAY) + "天前";
        } else {
            int year = c.get(Calendar.YEAR);
            c.setTimeInMillis(ms);
            if (year == c.get(Calendar.YEAR)) {
                return SDF_MD.format(c.getTime());
            } else {
                return SDF_YMD.format(c.getTime());
            }
        }
    }

    public static final class SECS {
        public static final long MINUS = 60;
        public static final long HOUR = MINUS * 60;
        public static final long DAY = HOUR * 24;

    }


}
