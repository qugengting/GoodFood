package com.common.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xuruibin on 2017/11/21
 * 日期转化工具类
 */

public class DateUtils {
    public static String FORMAT_Y = "yyyy";

    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_HM = "HH:mm";

    /**
     * 英文简写如：1-12 12:01
     */
    public static String FORMAT_MDHM = "MM-dd HH:mm";

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_YMD = "yyyy-MM-dd";

    /**
     * 英文全称  如：2010-12-01 23:15
     */
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL_SN = "yyyyMMddHHmmssS";

    public static String FORMAT_YEAR = "yyyy年";

    public static String FORMAT_NONE = "yyyy";

    public static String FORMAT_MONTH = "MM月dd日";
    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_YMD_CN = "yyyy年MM月dd日";

    /**
     * 中文简写  如：2010年12月01日  12时
     */
    public static String FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时";

    /**
     * 中文简写  如：2010年12月01日  12时12分
     */
    public static String FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";

    /**
     * 中文全称  如：2010年12月01日  23时15分06秒
     */
    public static String FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    public static String FORMAT_BEGIN_DAY = "yyyy-MM-dd 00:00:00";
    public static String FORMAT_END_DAY = "yyyy-MM-dd 23:59:59";
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long stringToLong(String strTime) {
        Date date = stringToDate(strTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date类型转成long类型
            return currentTime;
        }
    }

    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType);
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = date2Str(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static String longToString(long currentTime, String formatType) {
        Date date = null; // long类型转成Date类型
        try {
            date = longToDate(currentTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strTime = date2Str(date, formatType); // date类型转成String
        return strTime;
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    /**
     * 跟现在比较相差的分钟数
     *
     * @param date   日期字符串A
     * @param format 日期格式
     * @return 跟现在比较相差的分钟数,大于0表示A的时间比当前时间早
     */
    public static int countMins(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 60;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(stringToDate(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    public static long getNextTime(long currentTime, int unit, int num) {
        long nextTime = 0;
        long perDay = 1000 * 60 * 60 * 24;
        switch (unit) {
            //日
            case 1:
                nextTime = currentTime + perDay * num;
                break;
            //月
            case 2:
                nextTime = currentTime + perDay * 30 * num;
                break;
            //年
            case 3:
                nextTime = currentTime + perDay * 30 * 365 * num;
                break;
        }
        return nextTime;
    }

    /**
     * 判断当前日期是星期几<br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayForWeek;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        String week = "";
        switch (dayForWeek) {
            case 1:
                week = "(星期一)";
                break;
            case 2:
                week = "(星期二)";
                break;
            case 3:
                week = "(星期三)";
                break;
            case 4:
                week = "(星期四)";
                break;
            case 5:
                week = "(星期五)";
                break;
            case 6:
                week = "(星期六)";
                break;
            case 7:
                week = "(星期日)";
                break;
        }
        return week;
    }

    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startTime:String yyyy-MM-dd HH:mm:ss
     * @param endTime:String   yyyy-MM-dd HH:mm:ss
     * @return list:yyyy-MM-dd
     */
    public static List<String> getBetweenDate(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf1.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断指定时间和当前时间比较大小
     * @param compareTime HH:mm格式
     * @return 大于当前时间返回1，小于当前时间返回0
     */
    public static int timeCompare(String compareTime) {
        int i = 0;
        String str = longToString(System.currentTimeMillis(), "yyyy-MM-dd");
        str = str + " " + compareTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = dateFormat.parse(str);
            long time = System.currentTimeMillis();
            if (time <= date.getTime()) {
                //当前时间大于指定时间
                i = 1;
            } else {
                //当前时间小于指定时间
                i = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
