package com.common.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuruibin on 2017/11/21
 * 日期转化工具类
 */

public class DateUtils {
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    public static long stringToLong(String strTime) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date类型转成long类型
            return currentTime;
        }
    }
    public static long stringToLong(String strTime, String formatType) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
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
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
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
}
