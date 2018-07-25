package com.vigorchip.treadmill.base;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vigorchip.treadmill.broadcasereceiver.NetWorkChangeBroadcastReceiver;
import com.vigorchip.treadmill.module.SaveFixedDateUtils;
import com.vigorchip.treadmill.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static SharedPreferences sp;
    private static Context context;
    private static boolean dianji;//是否进入主界面
    private static boolean isFirst;//是否第一次进入
    private static boolean isChangLanguage;//语言是否改变
    public static int MAXSPEED = 168;//最大速度
    public static int MINSPEED = 6;//最小速度
    public static int SLOPES = 15;//最大扬升
    public static  String APP_TYPE = "T421";//app升级type
    private static String custom = "yijikang";//客户
    public static int sWidthPix;//屏幕宽
    public static int sHeightPix;//屏幕高
    public static int doorStatus;//后门
    public static MyApplication getInstance() {
        return myApplication;
    }
    public static Context getAppContext() {
        return MyApplication.getInstance().context;
    }
    public static SharedPreferences getSP() {
        return sp;
    }
    public static void setIsFirst(boolean isFirsts) {
        isFirst = isFirsts;
    }
    public static int getDoorStatus() {//0启动后门，1关闭后门
        doorStatus = MyApplication.getInstance().sp.getInt("DOORSTATUS", 1);
        return doorStatus;
    }
    public static void setDoorStatus(int doorStatusss) {
        doorStatus = doorStatusss;
        MyApplication.getInstance().sp.edit().putInt("DOORSTATUS", doorStatusss).commit();
    }
    public static String getCustom() {
        return custom;
    }
    public static boolean isFirst() {
        return isFirst;
    }
    public static boolean isDianji() {
        return dianji;
    }
    public static void setDianji(boolean dianjis) {
        dianji = dianjis;
    }
    public static boolean isChangLanguage() {
        return isChangLanguage;
    }
    public static void setIsChangLanguage(boolean isChangLanguages) {
        isChangLanguage = isChangLanguages;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        isFirst = true;
        dianji = true;
        isChangLanguage=false;
        context = getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sWidthPix = context.getResources().getDisplayMetrics().widthPixels;
        sHeightPix = context.getResources().getDisplayMetrics().heightPixels;
        try {
            NetWorkChangeBroadcastReceiver.setCurrentTimeMillis(sp.getLong("date", new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(new StringBuffer(SaveFixedDateUtils.INIT_TIME).toString()).getTime()));
            new NetWorkChangeBroadcastReceiver().startCalibrateTime();
            NetWorkChangeBroadcastReceiver.setIsGood(false);
        } catch (ParseException e) {// e.printStackTrace();
            LogUtils.log("初始化时间错误");
        }
        readSystemCon();
    }

    private void readSystemCon() {//读取配置文件
        if (new File("/system/treadmill/smart_run.dat").exists()) {
            if (GetBuildProproperties("Speed-max", "/system/treadmill/smart_run.dat") != null)
                MAXSPEED = Integer.parseInt(GetBuildProproperties("Speed-max", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("Speed-min", "/system/treadmill/smart_run.dat") != null)
                MINSPEED = Integer.parseInt(GetBuildProproperties("Speed-min", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("Incline-max", "/system/treadmill/smart_run.dat") != null)
                SLOPES = Integer.parseInt(GetBuildProproperties("Incline-max", "/system/treadmill/smart_run.dat"));
            if (GetBuildProproperties("Custom", "/system/treadmill/smart_run.dat") != null)
                custom = GetBuildProproperties("Custom", "/system/treadmill/smart_run.dat");
            if (GetBuildProproperties("app_type", "/system/treadmill/smart_run.dat") != null)
                APP_TYPE = GetBuildProproperties("app_type", "/system/treadmill/smart_run.dat");
        }
    }

    public static String GetBuildProproperties(String PropertiesName, String path) {// 获取build.prop中的指定属性
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(new File(path)));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strTemp;
            while ((strTemp = br.readLine()) != null) {// 如果文件没有读完则继续
                if (strTemp.indexOf(PropertiesName) != -1)
                    return strTemp.substring(strTemp.indexOf("=") + 1);
            }
            br.close();
            is.close();
            return null;
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            else
                e.printStackTrace();
            return null;
        }
    }
}