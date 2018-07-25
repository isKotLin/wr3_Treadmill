package com.vigorchip.treadmill.broadcasereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.SaveFixedDateUtils;
import com.vigorchip.treadmill.utils.LogUtils;
import com.vigorchip.treadmill.utils.ShellUtils;
import com.vigorchip.treadmill.utils.SntpClient;

import java.io.DataOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {
    private static boolean isGood;
    private final int NTP_TIME_OUT_MILLISECOND = 30000;// private final String LOG_TAG = TimeCalibrateHelper.class.getSimpleName();
    private boolean isStopCalibrate = false;
    public static boolean isGood() {
        return isGood;
    }

    public static void setIsGood(boolean isGood) {
        NetWorkChangeBroadcastReceiver.isGood = isGood;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (int i = 0; i < networkInfos.length; i++) {
                NetworkInfo.State state = networkInfos[i].getState();
                if (NetworkInfo.State.CONNECTED == state) {
                   LogUtils.log("------------> Network is ok");
                    startCalibrateTime();
                    return;
                }
            }
        } //没有执行return,则说明当前无网络连接
        System.out.println("------------> Network is validate"); //intent.setClass(context, NetWorkErrorActivity.class); intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); context.startActivity(intent);
    }
    private String[] ntpServerHost = new String[]{//ntp服务器地址集
            "dns1.synet.edu.cn",
            "news.neu.edu.cn",
            "dns.sjtu.edu.cn",
            "dns2.synet.edu.cn",
            "ntp.glnet.edu.cn",
            "ntp-sz.chl.la",
            "ntp.gwadar.cn",
            "cn.pool.ntp.org"
    };
    public void startCalibrateTime() {//开始校准时间
        new Thread() {
            @Override
            public void run() {
                while (!isStopCalibrate) {
                    for (int i = 0; i < ntpServerHost.length; i++) {
                        long time = getTimeFromNtpServer(ntpServerHost[i]);
                        if (time != -1) {
                            int tryCount = 3;
                            while (tryCount > 0) {
                                tryCount--;
                                boolean isSetTimeSuccessful = setCurrentTimeMillis(time);
                                if (isSetTimeSuccessful) {
                                    tryCount = 0;
                                    isStopCalibrate = true; //Log.i(LOG_TAG, "set time successful");
                                    try {
                                        if (MyApplication.getInstance().getSP().getLong("date", new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(new StringBuffer(SaveFixedDateUtils.INIT_TIME).toString()).getTime()) < getTimeFromNtpServer(ntpServerHost[i])) {
                                            MyApplication.getInstance().getSP().edit().putLong("date", getTimeFromNtpServer(ntpServerHost[i]));
                                            stopCalibrateTime();
                                            isGood = true;
                                        }
                                    } catch (ParseException e) { //e.printStackTrace();
                                        LogUtils.log("初始化时间错误");
                                    }
                                } else { //  Log.i(LOG_TAG, "set time failure");
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }.start();
    }

    public void stopCalibrateTime() {//停止校准时间
        isStopCalibrate = true;
    }

    /**
     * 从ntp服务器中获取时间
     * @param ntpHost ntp服务器域名地址
     * @return 如果失败返回-1，否则返回当前的毫秒数
     */
    private long getTimeFromNtpServer(String ntpHost) {
//        Log.i(LOG_TAG, "get time from " + ntpHost);
        SntpClient client = new SntpClient();
        boolean isSuccessful = client.requestTime(ntpHost, NTP_TIME_OUT_MILLISECOND);
        if (isSuccessful) {
            return client.getNtpTime();
        }
        return -1;
    }

    /**
     * 设置当前的系统时间
     * @param time
     * @return true表示设置成功, false表示设置失败
     */
    public static boolean setCurrentTimeMillis(long time) {
        try {
            if (ShellUtils.checkRootPermission()) {
//                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
                TimeZone.setDefault(TimeZone.getDefault());
                Date current = new Date(time);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd.HHmmss");
                String datetime = df.format(current);
                Process process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
//                os.writeBytes("setprop persist.sys.timezone GMT\n");
                os.writeBytes("/system/bin/date -s " + datetime + "\n");
                os.writeBytes("clock -w\n");
                os.writeBytes("exit\n");
                os.flush();
                os.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}