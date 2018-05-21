package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期助手类
 *
 * @author luyongzhao
 */
public class DateUtils {
    /**
     * 获取今天0点
     *
     * @return
     */
    public static Date getToday() {
        Calendar c = Calendar.getInstance();
        cleanTimes(c);

        return c.getTime();
    }

    public static Date getNextMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONDAY, 1);
        return c.getTime();
    }

    public static Date getNextYear() {
        return getNextYear(new Date(), 1);
    }

    public static Date getNextYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }

    /**
     * 获取当前时段的小时数
     *
     * @param date
     * @return
     */
    public static int getDateHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getNextMonth(Date t) {
        Calendar c = Calendar.getInstance();
        c.setTime(t);
        c.add(Calendar.MONDAY, 1);
        return c.getTime();
    }

    /**
     * 获取该日该时段，如果24日零点则返回 25日 00:00:00
     *
     * @param t
     * @return
     */
    public static Date getDateClock(Date t, int clock) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(t);
        cal.set(Calendar.HOUR_OF_DAY, clock);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static Integer getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static Integer getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static Integer getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 解析日期
     *
     * @param ymd yyyy-MM-dd
     * @return
     */
    public static Date parseY_M_D(String ymd) {
        try {
            return sdfY_M_D.parse(ymd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse(String format, String date) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析日期
     *
     * @param ymd 格式为yyyyMMdd
     * @return
     */
    public static Date parseYMD(String ymd) {
        try {
            return sdfYMD.parse(ymd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期:yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String formartYMD(Date date) {
        return sdfYMD.format(date);
    }

    public static String formartYMDH(Date time) {
        return sdfYMDH.format(time);
    }

    public static String formartYMD_CN(Date date) {
        return sdfY_M_D_CN.format(date);
    }

    public static String formartYM(Date date) {
        return sdfYM.format(date);
    }

    private static String formartYMcn(Date date) {
        return sdfYM_CN.format(date);
    }

    /**
     * 格式化日期：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formartY_M_D(Date date) {
        return sdfY_M_D.format(date);
    }

    public static String formartY_M(Date date) {
        return sdfY_M.format(date);
    }

    public static Calendar firstWeekOfYear(Integer year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, 1);
        c.set(Calendar.DAY_OF_WEEK, 1);

        if (c.get(Calendar.YEAR) < year) {// 计算一年的第一个星期一
            c.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return c;
    }

    public static void main(String[] args) {
        System.out.println(format(new Date(1525849930659L), "yyyyMMdd0HHmmss S"));
    }

    public static Calendar weekOfYear(Integer year, Integer week) {
        Calendar c = DateUtils.firstWeekOfYear(year);
        c.add(Calendar.WEEK_OF_YEAR, week - 1);
        return c;
    }

    public static String formartHH_mm_ss(Date msgTime) {
        return sdfHH_mm_ss.format(msgTime);
    }

    public static String formartHH_mm(Date msgTime) {
        return sdfHH_mm.format(msgTime);
    }

    public static String formartHH_mm(int hour, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, 0);
        return sdfHH_mm.format(c.getTime());
    }

    public static String formartHHmmss(Date msgTime) {
        return sdfHHmmss.format(msgTime);
    }

    public static String formartYMDHms(Date date) {
        return sdfYMDHms.format(date);
    }

    public static Date parseYMDHMS(String time) {
        try {
            return sdfYMDHms.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseY_M_D_H_M_S(String time) {
        try {
            return sdfY_M_D_HH_mm_SS.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formartDD(Date date) {
        return sdfDD.format(date);
    }

    public static String formartMM(Date date) {
        return sdfMM.format(date);
    }

    public static String formartYYYY(Date date) {
        return sdfYYYY.format(date);
    }

    private static final SimpleDateFormat sdfDD = new SimpleDateFormat("dd");
    private static final SimpleDateFormat sdfMM = new SimpleDateFormat("MM");
    private static final SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy");

    private static final SimpleDateFormat sdfYMDHms = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdfYMDHm = new SimpleDateFormat("yyyyMMddHHmm");
    private static final SimpleDateFormat sdfYMDH = new SimpleDateFormat("yyyyMMddHH");
    private static final SimpleDateFormat sdfY_M_D_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat sdfY_M_D_HH_mm_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat sdfY_M_D = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfY_M = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat sdfY_M_D_CN = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdfYM = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdfYM_CN = new SimpleDateFormat("yyyy年MM月");
    private static final SimpleDateFormat sdfHH_mm_ss = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat sdfHH_mm = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat sdfHHmmss = new SimpleDateFormat("HHmmss");

    private static final SimpleDateFormat sdfMDcn = new SimpleDateFormat("M月d日");
    private static final SimpleDateFormat sdfMMDDcn = new SimpleDateFormat("MM月dd日");

    public static Date getTomorrow() {
        Calendar c = Calendar.getInstance();
        cleanTimes(c);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static String formartMDcn(Date time) {
        return sdfMDcn.format(time);
    }

    public static String formartMMDDcn(Date time) {
        return sdfMMDDcn.format(time);
    }

    public static boolean isSameDay(Date today, Date date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(today);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);

        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
                && (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }

    public static Date modifyDay(Date enterDay, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(enterDay);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    public static String formatDayUsability(Date pubDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(pubDate);

        Calendar today = Calendar.getInstance();

        int day = today.get(Calendar.DAY_OF_YEAR) - c.get(Calendar.DAY_OF_YEAR);

        switch (day) {
            case 0:
                return "今天";
            case 1:
                return "昨天";
            case 2:
                return "前天";
            case -1:
                return "明天";
            case -2:
                return "后天";
            default:
                return formartYMD_CN(pubDate);
        }
    }

    public static String formatWeekCN(Date planDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(planDate);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "";
    }


    public static String formatWeekNum(Date planDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(planDate);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return "0";
            case 2:
                return "1";
            case 3:
                return "2";
            case 4:
                return "3";
            case 5:
                return "4";
            case 6:
                return "5";
            case 7:
                return "6";
        }
        return null;
    }


    public static String formatMonthCN(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar today = Calendar.getInstance();

        int day = today.get(Calendar.MONTH) - c.get(Calendar.MONTH);

        switch (day) {
            case 0:
                return "本月";
            case 1:
                return "上月";
            default:
                return formartYMcn(date);
        }
    }

    public static String formatTimeUsability(Date sendTime) {
        long t = (System.currentTimeMillis() - sendTime.getTime()) / 1000;
        if (t < 60) {
            return "刚刚";
        } else if (t < 3600) {
            return sdfHH_mm_ss.format(sendTime);
        } else {
            return sdfY_M_D_HH_mm.format(sendTime);
        }
    }

    public static Date getYearBefore(int i) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -i);
        c.set(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        cleanTimes(c);
        return c.getTime();
    }

    public static Date getDayBefore(int i) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -i);
        cleanTimes(c);
        return c.getTime();
    }

    public static Date getDayBefore(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, -i);
        return c.getTime();
    }

    /**
     * 获取date之后i天的日期
     *
     * @param date
     * @param i
     * @return
     */
    public static Date getDayAfter(Date date, int i) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, +i);
        return c.getTime();
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    public static Date cleanTimes(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return cleanTimes(c).getTime();
    }

    public static Calendar cleanTimes(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static String calculatorAge(Date birthday) {

        Calendar c = Calendar.getInstance();
        c.setTime(birthday);

        int monthNum = 0;

        Date now = new Date();

        while (c.getTime().compareTo(now) < 0) {
            monthNum++;
            c.add(Calendar.MONTH, 1);
        }

        return monthNum / 12 + "岁" + monthNum % 12 + "个月";
    }

    public static String formartYMDHm(Date date) {
        return sdfYMDHm.format(date);
    }

    /**
     * 验证日期的正确格式 标准格式为 2014-11-21
     *
     * @return
     */
    public static boolean dateFormat(String date) {
        boolean b = false;
        if (StringUtils.isBlank(date)) {
            return b;
        }
        String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        boolean dateFlag = m.matches();
        if (!dateFlag) {
            b = false;
        } else {
            b = true;
        }
        return b;
    }

    public static final int[] constellationArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public static final String[] constellation = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座"};

    public static final int[] constellationEdgeDay = {20, 21, 21, 22, 22, 23, 23, 24, 24, 23, 22, 21};

    /**
     * 根据日期获取星座
     *
     * @param time
     * @return
     */
    public static Integer date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    public static Integer date2Constellation(Date date) {
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    /**
     * 获取具体星座
     *
     * @param index
     * @return
     */
    public static String getConstellation(Integer index) {
        if (index > 0 && index < 13) {
            return constellation[index - 1];
        } else {
            return null;
        }
    }

    public static Date getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        cleanTimes(c);
        return c.getTime();
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    public static String format(long time, String format) {

        String date = null;

        if (time < 0 || format == null) {
            return null;
        }

        try {
            date = new SimpleDateFormat(format).format(new Date(time));
        } catch (Exception e) {
            return null;
        }

        return date;
    }

    /**
     * 判断当前日期是否在当日日期之前，yyyy_MM_dd
     *
     * @param day
     * @return
     */
    public static boolean isBefore(String dayStr) {
        boolean before = false;
        Date today = new Date();
        Date targetDay = parseY_M_D(dayStr);

        try {
            int flag = today.compareTo(targetDay);
            if (flag >= 0) {// 当天及当天之后，<0就是在日期之前
                before = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return before;
    }

    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }


    /**
     * 返回英文格式的日期 // May 1st
     *
     * @param date
     * @return
     */
    public static String stringEnglishDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        String dayStr = sdf.format(date);
        int length = dayStr.indexOf(" ");
        String endFix = "null";
        if (dayStr.length() - length - 1 == 2) {
            endFix = "th";
        } else {
            if (dayStr.endsWith("1")) {
                endFix = "st";
            } else if (dayStr.endsWith("2")) {
                endFix = "nd";
            } else if (dayStr.endsWith("3")) {
                endFix = "rd";
            } else {
                endFix = "th";
            }
        }
        return dayStr + endFix;
    }

    /**
     * 微博的时间显示
     *
     * @param createAt
     * @return
     */
    public static String getInterval(Date createAt) {
        // 定义最终返回的结果字符串。
        String interval = null;

        long millisecond = new Date().getTime() - createAt.getTime();

        long second = millisecond / 1000;

        if (second <= 0) {
            second = 0;
        }
        //--------------微博标准
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = second + "秒以前";
        } else if (second >= 30 && second < 60) {
            interval = "半分钟前";
        } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (second >= 60 * 60 && second < 60 * 60 * 24) {//大于1小时 小于24小时
            long hour = (second / 60) / 60;
//			if (hour <= 3) {  
            interval = hour + "小时前";
//			} else {
//				interval = "今天" + getFormatTime(createAt, "HH:mm");
//			}
        } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {//大于1D 小于2D
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {//大于2D小时 小于 7天
            long day = ((second / 60) / 60) / 24;
            interval = day + "天前";
        } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) {//大于7天小于365天
            interval = getFormatTime(createAt, "MM-dd HH:mm");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        } else {
            interval = "0";
        }
        return interval;
    }

    /**
     * 获取date所在星期的星期一的日期
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

}
