package com.jumang.shortvideo.utils;

import android.text.format.Time;

import com.jumang.shortvideo.data.AppInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static boolean sameDate(Date d1, Date d2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * judge the string is date?
     *
     * @param strDate
     * @param dateFormat
     * @return
     */
    public static boolean isDate(String strDate, SimpleDateFormat dateFormat) {
        Date d = null;
        try {
            d = (Date) dateFormat.parseObject(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d == null)
            return false;

        String tmp = dateFormat.format(d);

        return tmp.equals(strDate);
    }

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     *
     * @param str    字符串
     * @param format 日期格式
     * @return 日期
     * @throws ParseException
     */
    public static Date strToDate(final String str, final String format) {
        String ft = format;

        if (null == str || "".equals(str)) {
            return null;
        }
        SimpleDateFormat sdf = null;
        // 如果没有指定字符串转换的格式，则用默认格式进行转换
        if (null == ft || "".equals(ft)) {
            sdf = DATE_FORMAT_DATE;
        }
        sdf = new SimpleDateFormat(ft);
        Date date;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date(0);
        }
        return date;
    }

    public static Date getCaleDate(Date nowDate, int Num) {
        //如果需要向后计算日期 -改为+
        return new Date(nowDate.getTime() - (long) Num * 60000);
    }

    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

    /**
     * 字符串转换成日期 如果转换格式为空，则利用默认格式进行转换操作
     *
     * @param str 字符串
     * @return 日期
     * @throws ParseException
     */
    public static Date strToDate(final String str) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = DATE_FORMAT_DATE.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换为字符串
     *
     * @param date 日期
     * @return 字符串
     */
    public static String dateToStr(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param dateString 日期字符串(yyyy-MM-dd)
     * @return 格式化后的字符串
     */
    public static String formatDateString(String dateString) {
        return formatDateString(dateString, AppInfo.DATE_FORMATE);
    }

    /**
     * 日期转换为字符串
     *
     * @param dateString 日期字符串
     * @param format     日期格式
     * @return 格式化后的字符串
     */
    public static String formatDateString(String dateString, String format) {
        return formatDateString(dateString, null, format);
    }

    /**
     * 日期转换为字符串
     *
     * @param dateString 日期字符串
     * @param format     日期格式
     * @return 格式化后的字符串
     */
    public static String formatDateString(String dateString, String originalFormat, String format) {
        if (dateString == null) {
            return null;
        }
        if (originalFormat == null) {
            originalFormat = "yyyy-MM-dd HH:mm:ss";
        }
        Date d = strToDate(dateString, originalFormat);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * 日期转成字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    private TimeUtils() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public static String getTime() {
        String date = getFormattedDate();
        return date.substring(11, date.length() - 3);

    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public static String getDate() {
        String date = getFormattedDate();
        return date.substring(0, 11);
    }

    public static Date getDateStart(Date date) {
        return strToDate(dateToStr(date, "yyyy-MM-dd") + " 00:00:00");
    }


    /**
     * start
     * 本周开始时间戳 - 以星期一为本周的第一天
     */
    public static long getWeekStartTime() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        return getDateStart(cal.getTime()).getTime();
    }

    public static Date getHourStart(Date date) {
        return strToDate(dateToStr(date, "yyyy-MM-dd HH") + ":00:00", AppInfo.DATE_TIME_FORMATE);
    }

    public static Date getDateEnd(Date date) {
        return strToDate(dateToStr(date, "yyyy-MM-dd") + " 23:59:59"
                , AppInfo.DATE_TIME_FORMATE);
    }

    public static Date getWeekEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal.getTime();
    }

    public static Date getWeekStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        // 增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return getDateStart(cal.getTime());
    }

    public static String getFormattedDate() {

        Time time = new Time();
        time.setToNow();
        DateFormat.getDateInstance();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(AppInfo.DATE_TIME_FORMATE);
        return df.format(c.getTime());
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(AppInfo.DATE_TIME_FORMATE);
        long between_days = 0;
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算时间差
     *
     * @param d1
     * @param d2
     * @return "day,hour, min,s"
     */
    public static long[] getTimeDiff(Date d1, Date d2) {
        long l = d2.getTime() - d1.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        long[] rArray = new long[]{day, hour, min, s};
        return rArray;
    }

    public static String getTimeString(String endTime, String expendTime) {
        //传入字串类型 end:2016/06/28 08:30 expend: 03:25
        long longEnd = strToDate(endTime).getTime();
        String[] expendTimes = expendTime.split(":");   //截取出小时数和分钟数
        long longExpend = Long.parseLong(expendTimes[0]) * 60 * 60 * 1000 + Long.parseLong(expendTimes[1]) * 60 * 1000;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdfTime.format(new Date(longEnd - longExpend));
    }

    public static String getTimeExpend(String startTime, Date endTime) {
        //传入字串类型 2016/06/28 08:30
        long longStart = strToDate(startTime, "yyyy-MM-dd HH:mm:ss").getTime(); //获取开始时间毫秒数
        long longEnd = endTime.getTime();  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差

        long longHours = longExpend / (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);   //根据时间差来计算分钟数
        String strHours;
        if (longHours < 10) {
            strHours = "0" + longHours;
        } else {
            strHours = longHours + "";
        }
        String strMin;
        if (longMinutes < 10) {
            strMin = "0" + longMinutes;
        } else {
            strMin = longMinutes + "";
        }

        return strHours + ":" + strMin;
    }

    public static int diffMin(Date d1, Date d2) {
        long from = d1.getTime();
        long to = d2.getTime();
        return (int) ((to - from) / (60000));
    }

    public static Date calculateSecond(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, value);//把日期往后增加一秒.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一秒的结果
        return date;
    }

    public static Date calculateDay(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, value);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return date;
    }

    public static Date calculateHour(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, value);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return date;
    }

    public static Date calculateMonth(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, value);//把日期往后增加一月.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一月的结果
        return date;
    }

    public static Date calculateYear(Date date, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, value);//把日期往后增加一年.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一年的结果
        return date;
    }

    public static int[] getTimeSpanHour(int min) {
        int[] result = new int[]{0, 0};
        if (min >= 60) {
            result[0] = min / 60;
            result[1] = min % (result[0] * 60);
        } else {
            result[1] = min;
        }
        return result;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2018-01-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String getWeek(String pTime) {
        String Week = "周";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(DATE_FORMAT_DATE.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }
        return Week;
    }
//
//    public static String getWeek(Context context, Date date) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        switch (c.get(Calendar.DAY_OF_WEEK)) {
//            case Calendar.SUNDAY:
//            default:
//                return context.getString(R.string.datescroller_sunday);
//            case Calendar.MONDAY:
//                return context.getString(R.string.datescroller_monday);
//            case Calendar.TUESDAY:
//                return context.getString(R.string.datescroller_tuseday);
//            case Calendar.WEDNESDAY:
//                return context.getString(R.string.datescroller_wednesday);
//            case Calendar.THURSDAY:
//                return context.getString(R.string.datescroller_thursday);
//            case Calendar.FRIDAY:
//                return context.getString(R.string.datescroller_friday);
//            case Calendar.SATURDAY:
//                return context.getString(R.string.datescroller_saturday);
//        }
//    }

}
