package com.ox;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by
 * User: LiMing
 * Date: 2015/8/11
 */
public class DateUtil {
    public static void main(String[] args) throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = df.parse("2004-03-03 13:31:40");

        Date d2 = df.parse("2004-03-03 14:30:24");

        System.out.println(dateDiff(d1, d2, Calendar.MINUTE));

    }

    /**
     * 计算年的差值
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long dateDiffYear(Date d1, Date d2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        int i = calendar.get(Calendar.YEAR);
        calendar.setTime(d2);
        int j = calendar.get(Calendar.YEAR);

        return i - j;
    }

    /**
     * 计算月的差值
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long dateDiffMonth(Date d1, Date d2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);
        calendar.setTime(d2);
        int y2 = calendar.get(Calendar.YEAR);
        int m2 = calendar.get(Calendar.MONTH);

        return (y1 - y2) * 12 + m1 - m2;
    }

    /**
     * 获取一个值
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int get(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * 计算查值
     *
     * @param d1
     * @param d2
     * @param field
     * @return
     */
    public static long dateDiff(Date d1, Date d2, int field) {
        long j = 1;
        switch (field) {
            case Calendar.YEAR:
                return dateDiffYear(d1, d2);
            case Calendar.MONTH:
                return dateDiffMonth(d1, d2);
            case Calendar.DATE:
                j = 1000 * 60 * 60 * 24;
                break;
            case Calendar.HOUR:
                j = 1000 * 60 * 60;
                break;
            case Calendar.MINUTE:
                j = 1000 * 60;
                break;
            case Calendar.SECOND:
                j = 1000;
                break;

            default:
                return 0;
        }
        long diff = d1.getTime() - d2.getTime();
        return diff / j;
    }

    /**
     * Date转String
     *
     * @param date
     *            默认当前时间
     * @param format
     *            默认format
     * @return
     */
    public static String dateConvertString(Date date, String... format) {
        String ss = "yyyy-MM-dd HH:mm:ss";
        Date dd = new Date();
        if (format.length > 0)
            ss = format[0];
        if (null != date)
            dd = date;
        return new SimpleDateFormat(ss).format(dd);
    }

    public static String getGMTString(Date d) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);// ();
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(d);
    }

    /**
     * 日期加减
     *
     * @param d
     * @param field
     *            类型
     * @param amount
     *            数量
     * @return
     */
    public static Date add(Date d, int field, int amount) {
        if (d == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 添加秒
     *
     * @param d
     * @param amount
     * @return
     */
    public static Date addSecond(Date d, int amount) {
        return add(d, Calendar.SECOND, amount);
    }

    /**
     * 添加分
     *
     * @param d
     * @param amount
     * @return
     */
    public static Date addMinute(Date d, int amount) {
        return add(d, Calendar.MINUTE, amount);
    }

    /**
     * 添加天
     *
     * @param d
     * @param amount
     * @return
     */
    public static Date addDay(Date d, int amount) {
        return add(d, Calendar.DATE, amount);
    }

    /**
     * 指定的日期加天数
     *
     * @param date
     * @param amount
     * @return
     */
    public static String addDateToString(String date, int amount) {
        if (date == null || "".equals(date.trim())) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.DATE, amount);
        return getDateToString(calendar.getTime());
    }

    /**
     * 指定的日期加天数
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addDate(String date, int amount) {
        if (date == null || "".equals(date.trim())) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.add(Calendar.DATE, amount);
        return calendar.getTime();
    }

    /**
     * 得到本月的第一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthFirstDay() throws ParseException {
        int mondayPlus;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getDate(cal.getTime());

    }

    /**
     * 得到本周的第一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getWeekFirstDay() throws ParseException {
        int mondayPlus;
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            mondayPlus = 0;
        } else {
            mondayPlus = 1 - dayOfWeek;
        }
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        return getDate(currentDate.getTime());
    }

    /**
     * 得到日期的年月日
     *
     * @return
     */
    public static Date getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 格式化时间
     *
     * @return
     */
    public static Date getDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 取得date的时间
     *
     * @return
     */
    public static Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 取得date的时间
     *
     * @return
     */
    public static Date getDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将日期转换成字符窜
     *
     * @return
     */
    public static String getDateToString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 将日期转换成字符窜
     *
     * @return
     */
    public static String getDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将日期转换成字符窜
     *
     * @return
     */
    public static String getTimeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将Long类型的日期转换成Date
     *
     * @return
     */
    public static Date parseDate(Long time) {
        Date date = new Date();
        date.setTime(time);
        return date;
    }

    /**
     * 将字符窜日期转换成日期
     *
     * @return
     * @throws ParseException
     */
    public static Date getTime(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(date);
    }

    /**
     * 天数
     *
     * @return
     */
    public static long getDay() {
        return getDate(new Date()).getTime() / (1000 * 3600 * 24);
    }
}
