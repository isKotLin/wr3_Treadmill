package com.vigorchip.treadmill.utils;

import com.vigorchip.treadmill.broadcasereceiver.NetWorkChangeBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 显示时间日期
 */
public class NowDateTime {
    private NowDateTime() {
    }

    private static NowDateTime instance = new NowDateTime();

    public static NowDateTime getInstance() {
        return instance;
    }

    static int year;
    static int month;
    static int day;
    static int hour;
    static int minute;
    static Calendar calendar;

    public static String getDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        return year + "/" + (month < 10 ? "0" + month : month) + "/" + (day < 10 ? "0" + day : day);
    }

    public static String getTimes(String dateFormat) {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
//        SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间  这个决定了系统时间你只能获取到24小时制的。想要获取12小时制你就得先获取24小时制然后进行处理就好了！
        if (NetWorkChangeBroadcastReceiver.isGood())
            return df.format(new Date());
        else
            return "";
//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        hour = calendar.get(Calendar.HOUR);
//        minute = calendar.get(Calendar.MINUTE);
//        if (year == 1970)
//            return "";
//        else
//            return (hour == 0 ? 12 : hour >= 10 ? hour : "0" + hour) + ":" + (minute >= 10 ? minute : "0" + minute);
//return hour > 12 ? (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) : hour + 12 + ":" + (minute < 10 ? "0" + minute : minute);
    }
}