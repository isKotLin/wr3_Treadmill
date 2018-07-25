package com.vigorchip.treadmill.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PublicUtils {
    //获取总内存大小
    public static String getTotalMemorySize(Context context) {
        long size = 0;

        //通过读取配置文件方式获取总内大小。文件目录：/proc/meminfo
        File file = new File("/proc/meminfo");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //根据命令行可以知道，系统总内存大小位于第一行
            String totalMemarysizeStr = reader.readLine();//MemTotal:         513744 kB
            //要获取大小，对字符串截取
            int startIndex = totalMemarysizeStr.indexOf(':');
            int endIndex = totalMemarysizeStr.indexOf('k');
            //截取
            totalMemarysizeStr = totalMemarysizeStr.substring(startIndex + 1, endIndex).trim();
            //转为long类型，得到数据单位是kb
            size = Long.parseLong(totalMemarysizeStr);
            //转为以byte为单位
            size *= 1024;
        } catch (Exception e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Formatter.formatFileSize(context, size);
//        return size;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static long getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
//        return Formatter.formatFileSize(getContext(), blockSize * totalBlocks);
        return blockSize * totalBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
//        return Formatter.formatFileSize(getContext(), blockSize * availableBlocks);
    }
    /**
     * 获取本应用版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "1.0.0";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName(Context context, String getPackageName) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName, PackageManager.GET_ACTIVITIES); //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
//            ActivityInfo[] activities = packageInfo.activities;showActivities(activities);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0.0.1";
    }

    /**
     * 读取系统的属性和版本号
     *
     * @param name
     * @param defaultValue
     * @return
     * @throws IOException
     */
    public static String getProperty(final String name, final String defaultValue) throws IOException {
        Properties properties = new Properties();
        FileInputStream is = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
        properties.load(is);
        is.close();
        return properties.getProperty(name, defaultValue);
    }
}
