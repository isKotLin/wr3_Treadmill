package com.vigorchip.treadmill.serialport;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.DialogError;
import com.vigorchip.treadmill.impl.SerialToErrors;
import com.vigorchip.treadmill.impl.SerialToMain;
import com.vigorchip.treadmill.module.MyTime;
import com.vigorchip.treadmill.module.SportData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by wr-app1 on 2018/2/26.
 */

public class newSerial {
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;

    //获取软件需要发送给下控实时的状态、速度、坡度的信息 需要赋值
    private int getStatus ;
    private double getSpeed ;
    private int getSlopes ;
    private long mySteps;//获取实时步数
    private boolean isFlag = true;//判断100s发送的读写流标志位

    private AudioManager audioManager;  // 系统声音管理类
    private boolean year_flag = true;
    private static SerialToMain serialToMains;
    private static SerialToErrors serialToError;

    public static void setSerialToMain(SerialToMain serialToMain) {
        serialToMains = serialToMain;
    }

    public static void setSerialToErrors(SerialToErrors serialToErrors) {
        serialToError = serialToErrors;
    }

    public void CreateSerial() {
        try {
            serialPort = new SerialPort(new File("/dev/ttyS2"), 9600);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            handler.postDelayed(runnable1, 100);
            audioManager = (AudioManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.AUDIO_SERVICE); // 实例化系统声音管理类
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            if (isFlag) {
                write();
                isFlag = false;
            } else {
                getMsg(read());
                isFlag = true;
            }
            handler.postDelayed(runnable1, 60);
        }
    };

    private void write() {
        try {
            if (outputStream != null) {
                set();
                Log.i("运行状态", String.valueOf(getStatus));
                outputStream.write(setNewbyte((byte)getStatus, (byte) ((int) (getSpeed) & 0xFF), (byte)getSlopes));
//                Log.i("方法write走了", "");
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] read() {
        try {
            if (inputStream != null) {
                int len = inputStream.available();
                byte[] buffer = new byte[len];
                if ((inputStream.read(buffer)) != -1) {
                    return buffer;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void set(){
        if (SportData.getStatus() == SportData.NORMAL_RUN_STATUS ||
                SportData.getStatus() == SportData.INVERSE_RUN_STATUS ||
                SportData.getStatus() == SportData.SCENE_RUN_STATUS ||
                SportData.getStatus() == SportData.CALORIE_RUN_STATUS ||
                SportData.getStatus() == SportData.PROGRAM_RUN_STATUS ||
                SportData.getStatus() == SportData.HRC_RUN_STATUS ||
                SportData.getStatus() == SportData.USER_RUN_STATUS) {
            getStatus = 1;
        }else if (SportData.getStatus() == SportData.ERROR_STATUS||
                SportData.getStatus() == SportData.NOTSAFE_STATUS){
            getStatus = 2;
        }else if (SportData.isSetup()){
            getStatus = 0;
        }else {
            getStatus = 3;
        }
        getSpeed = ((int) ((MyTime.getmSpeed() + 0.05) * 10)) / 10.0;
        getSlopes =  MyTime.getmSloped();
    }


    int fla = 1;
    public boolean getMsg(byte[] read) {
        if (read == null){
            return false;
        }
        byte[] mbyte = read;

        if (mbyte == null && mbyte.length == 0) {
            return false;
        }

        if(mbyte != null && mbyte.length != 0 && mbyte.length == 20
                && mbyte[0] == (byte)0x02 && mbyte[19] == (byte)0x03){
            byte[] new_mbyte = new byte[mbyte.length - 2];
            for (int i = 0; i < new_mbyte.length; i++) {
                //掐头去尾后new_mbyte为有效数据
                new_mbyte[i] = mbyte[i + 1];
            }

            if (year_flag) {
                Log.i("机器年份： ", new_mbyte[0] + "");
                year_flag = false;
            }

            if (new_mbyte[1] >= (byte)0 && new_mbyte[1] <= (byte)255) {
                int batch = getDecimalism(new_mbyte[1]);
                Log.i("机器批次： ", String.valueOf(batch));
            }

            if (calHeightBack(new_mbyte[5]) == (byte)1) {
                Log.i("当前速度范围是： ", "1-13KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte)2) {
                Log.i("当前速度范围是： ", "1-16KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte)3) {
                Log.i("当前速度范围是： ", "1-18KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte)4) {
                Log.i("当前速度范围是： ", "1-20KM");
            } else if (calHeightBack(new_mbyte[5]) == (byte)5) {
                Log.i("当前速度范围是： ", "1-22KM");
            }

            if (calLowerBack(new_mbyte[5]) == (byte)1) {
                Log.i("当前坡度范围是： ", "0-12KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte)2) {
                Log.i("当前坡度范围是： ", "0-15KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte)3) {
                Log.i("当前坡度范围是： ", "0-20KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte)4) {
                Log.i("当前坡度范围是： ", "0-35KM");
            } else if (calLowerBack(new_mbyte[5]) == (byte)5) {
                Log.i("当前坡度范围是： ", "-1-35KM");
            }

            if (calHeightBack(new_mbyte[6]) == (byte)0){
                Log.i("当前为中文","");
            }else if (calHeightBack(new_mbyte[6]) == (byte)1){
                Log.i("当前为英文","");
            }else if (calHeightBack(new_mbyte[6]) == (byte)3){
                Log.i("当前为日文","");
            }else if (calHeightBack(new_mbyte[6]) == (byte)4){
                Log.i("当前为西班牙文","");
            }else if (calHeightBack(new_mbyte[6]) == (byte)5){
                Log.i("当前为俄罗斯文","");
            }

            //将下标6转低四位再转成字节myBit数组 获取每个bit
            byte[] myBit = getBitArray(calLowerBack(new_mbyte[6]));
            if (myBit[0] == (byte)1) {
                Log.i("Bit-0为: "+ myBit[0], "====英制");
            }else if (myBit[0] == (byte)0){
                Log.i("Bit-0为: "+ myBit[0], "====公制");
            }
            if (myBit[1] == (byte)1){
                Log.i("Bit-1为: "+myBit[1], "====无HRC");
            }else if (myBit[1] == 0){
                Log.i("Bit-1为: "+myBit[1], "====有HRC");
            }
            if (myBit[2] == (byte)1){
                Log.i("Bit-2为: "+myBit[2], "====带计步");
            }else if (myBit[2] == (byte)0){
                Log.i("Bit-2为: "+myBit[2], "====无计步");
            }

//            Log.i("查看new_mbyte[7]", String.valueOf(new_mbyte[7]));
            Log.i("查看new_mbyte[7]高四位", String.valueOf(calHeightBack(new_mbyte[7])));
           if (calHeightBack(new_mbyte[7]) == (byte)0 && fla != 0) {
               if (DialogError.dialog != null) {
                   DialogError.dimss();
               }
                Log.e("跑步机运行状态： ", "无故障");
            } else if (calHeightBack(new_mbyte[7]) == (byte)1) {
               serialToError.sendErrors(17);
               if (getStatus == 1) {
                   MyTime.timePause();
               }
                Log.e("跑步机运行状态： ", "通讯故障");
            } else if (calHeightBack(new_mbyte[7]) == (byte)2) {
               serialToError.sendErrors(2);
               if (getStatus == 1) {
                   MyTime.timePause();
               }
                Log.e("跑步机运行状态： ", "电机尾插");
            } else if (calHeightBack(new_mbyte[7]) == (byte)3) {
               serialToError.sendErrors(18);
               if (getStatus == 1) {
                   MyTime.timePause();
               }
                Log.e("跑步机运行状态： ", "无速度信号");
            } else if (calHeightBack(new_mbyte[7]) == (byte)5) {
               serialToError.sendErrors(5);
               if (getStatus == 1) {
                   MyTime.timePause();
               }
                Log.e("跑步机运行状态： ", "电机过流");
            } else if (calHeightBack(new_mbyte[7]) == (byte)7) {
               serialToError.sendErrors(7);
               SportData.setStatus(SportData.NOTSAFE_STATUS);
               if (getStatus == 1) {
                   MyTime.timePause();
               }
                Log.e("跑步机运行状态： ", "安锁脱落");
            }
            fla=calHeightBack(new_mbyte[7]);
            Log.i("查看new_mbyte[7]低四位", String.valueOf(calLowerBack(new_mbyte[7])));
            if (calLowerBack(new_mbyte[7]) == (byte)0) {
                Log.e("跑步机运行状态： ", "待机状态");
            } else if (calLowerBack(new_mbyte[7]) == (byte)1) {
                Log.e("跑步机运行状态： ", "运行状态");
            } else if (calLowerBack(new_mbyte[7]) == (byte)2) {
                Log.e("跑步机运行状态： ", "故障状态");
            } else if (calLowerBack(new_mbyte[7]) == (byte)3) {
                Log.e("跑步机运行状态： ", "正在减速停机状态");
            } else if (calLowerBack(new_mbyte[7]) == (byte)4) {
                Log.e("跑步机运行状态： ", "休眠状态");
            }

            if (new_mbyte[8] == (byte)1) {
                if (getStatus != 1 && MyTime.getmSpeed() == 0
                        && SportData.getStatus() != SportData.App
                        && SportData.getStatus() != SportData.AppLogin
                        && SportData.getStatus() != SportData.AppRegist
                        && SportData.getStatus() != SportData.DialogResult) {
                    serialToMains.send();
                }
                Log.i("键值： ", "1启动");
            } else if (new_mbyte[8] == (byte)2) {
                if (getStatus == 1) {
                    MyTime.timeStop();
//                    Log.i("---------------总时间停止了-----------","5");
                }
                Log.i("键值： ", "2停止");
            } else if (new_mbyte[8] == (byte)3) {
                Log.i("键值： ", "3确认");
            } else if (new_mbyte[8] == (byte)4) {
                Log.i("键值： ", "4风扇");
            } else if (new_mbyte[8] == (byte)5) {
                MyTime.setmSloped(MyTime.getmSloped() + 1);
                Log.i("键值： ", "5扬升加1");
            } else if (new_mbyte[8] == (byte)6) {
                MyTime.setmSloped(MyTime.getmSloped() - 1);
                Log.i("键值： ", "6扬升减1");
            } else if (new_mbyte[8] == (byte)7) {
                MyTime.setmSpeed(MyTime.getmSpeed() + 1);
                Log.i("键值：  "+new_mbyte[8], " 速度加"+getSpeed);
            } else if (new_mbyte[8] == (byte)8) {
                MyTime.setmSpeed(MyTime.getmSpeed() - 1);
                Log.i("键值： ", "8    速度减"+getSpeed);
            } else if (new_mbyte[8] == (byte)9) {
                muteRelieve();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i("键值： ", "9    音量+"+"    当前音量： "+currentVolume);
            } else if (new_mbyte[8] == (byte)10) {
                muteRelieve();
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                Log.i("键值： ", "10    音量-"+"    当前音量： "+currentVolume);
            } else if (new_mbyte[8] == (byte)11) {
                muteRelieve();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC,true);
                Log.i("键值： ", "11静音");
            } else if (new_mbyte[8] == (byte)12) {
                Log.i("键值： ", "12关机");
            } else if (new_mbyte[8] == (byte)13) {
                Log.i("键值： ", "13待机休眠");
            } else if (new_mbyte[8] == (byte)14) {
                Log.i("键值： ", "14程式");
            } else if (new_mbyte[8] == (byte)15) {
                Log.i("键值： ", "15模式");
            } else if (new_mbyte[8] == (byte)16) {
                Log.i("键值： ", "16测脂");
            }

            if (new_mbyte[9] >= (byte)0 && new_mbyte[9] <= (byte)255) {
                //获取速度控制数值
                int speedCount = getDecimalism(new_mbyte[9]);
                Log.i("获取速度控制数值： ", String.valueOf(speedCount));
            }

            if (new_mbyte[10] >= (byte)0 && new_mbyte[10] <= (byte)255) {
                //获取坡度控制数值
                int gradeCount = getDecimalism(new_mbyte[10]);
                Log.i("获取坡度控制数值： ", String.valueOf(gradeCount));
            }

            if (new_mbyte[11] >= (byte)40 && new_mbyte[11] <= (byte)199) {
                //获取心率值
                int heart = getDecimalism(new_mbyte[11]);
                Log.i("获取心率值： ", String.valueOf(heart));
            }

            //计算步数的变量mySteps
            if (new_mbyte[12] == (byte)1) {
                mySteps = mySteps + 1;
                Log.i("当前步数为： ", String.valueOf(mySteps));
            }

            if (mbyte[18] == XorCheckOut(new_mbyte,new_mbyte.length)){
                Log.v("校验成功","(*^▽^*)");
                return true;
            }
            return true;
        }
//        Log.v("校验失败","￣へ￣");
        return false;
    }
    public void muteRelieve() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        audioManager.setStreamMute(AudioManager.STREAM_DTMF, false);
    }



//    private void start() {
//        if (!SportData.isRunning() && SportData.getStatus() != SportData.FIVE_SECOND_STATUS)
//            switch (SportData.getStatus()) {
//                case SportData.NORMAL_STATUS:
//                    readyToMain.showSport(7);
//                    break;
//                case SportData.CALORIE_SETUP_STATUS:
//                    readyToMain.showSport(3);
//                    break;
//                case SportData.PROGRAM_SETUP_STATUS:
//                    readyToMain.showSport(4);
//                    break;
//                case SportData.HRC_SETUP_STATUS:
//                    readyToMain.showSport(6);
//                    break;
//                case SportData.USER_SETUP_STATUS:
//                    readyToMain.showSport(5);
//                    break;
//            }
//    }

    //计算高4位
    public static byte calHeightBack(byte mbyteCount) {
        byte calHeight = (byte) ((mbyteCount >> 4) & 0xF);
        return calHeight;
    }

    //计算低4位
    public static byte calLowerBack(byte mbyteCount) {
        byte calLower = (byte) (mbyteCount & 0x0F);
        return calLower;
    }

    //计算校验码，所有的有效字节进行异或
    public byte XorCheckOut(byte[] mbyte, int length) {
        byte XorCount = 0;
        for (int i = 0; i < length; i++) {
            XorCount = (byte) (XorCount ^ mbyte[i]);
        }
        return XorCount;
    }

    //将byte字符转换成String字符串
    public static String bytesToHex(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        // 之所以用byte和0xff相与，是因为int是32位，与0xff相与后就舍弃前面的24位，只保留后8位
        String str = Integer.toHexString(src & 0xff);
        if (str.length() < 2) { //不足两位要补0
            stringBuilder.append(0);
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    //拼接一帧完整数据，用于TFT发送给下控
    public byte[] setNewbyte(byte status, byte speed, byte grade) {
        byte[] mbyte = new byte[]{status, speed, grade, 0x00, 0x00, 0x00, 0x00};//拼接有效数据
        byte[] newbyte = new byte[]{0x0B, status, speed, grade, 0x00, 0x00, 0x00, 0x00, XorCheckOut(mbyte, mbyte.length), 0x0D};
        return newbyte;
    }

    //将十六进制转成二进制方式的数组
    public static byte[] getBitArray(byte HexCount) {
        byte[] array = new byte[4];
        for (int i = 3; i >= 0; i--) {
            array[i] = (byte)(HexCount & 1);
            HexCount = (byte) (HexCount >> 1);
        }
        return array;
    }

    //将十六进制转成十进制
    public static int getDecimalism(byte HexCount){
        String hex = String.valueOf(HexCount);
        Integer DEC = Integer.parseInt(hex,16);
        return DEC;
    }

}