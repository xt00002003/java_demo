package com.dark.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mouhaining
 * @date 2017-04-10 16:10
 * @see org.apache.commons.lang.time.DateUtils
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    public static final String HOUR_AND_MINUTES_FORMAT = "HH:mm";
    public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";
    public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_DEFAULT_FORMAT = "yyyyMMdd";
    public static final String SIMPLE_DATE_NOYEARPREFIX_FORMAT = "yyMMdd";
    public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     *  date to string
     * @param format 日期格式
     * @param date 日期
     * @return
     */
    public static String dateToString(String format,Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *  date to string
     * @param date 日期  format：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_DEFAULT_FORMAT);
        return sdf.format(date);
    }

    /**
     *  date to string
     * @param format 日期格式
     * @param date 日期
     * @return
     */
    public static Date stringToDate(String format, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    /**
     * string to Date
     * @param date 日期  format：yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATETIME_DEFAULT_FORMAT);
        return sdf.parse(date);
    }

    /**
     * getTime
     * @return time
     */
    public static long getTime(Date date) {
        return date.getTime();
    }

    /**
     * getTime for now
     * @return
     */
    public static long getNow() {
        return DateUtils.getTime(new Date());
    }
}
