package com.vigorchip.treadmill.module;

/**
 * 运动数据
 */
public class SportData {

    private static int status;//状态
    public static final int NORMAL_STATUS = 0;//普通状态
    public static final int NOTSAFE_STATUS = 1; //安全锁脱落
    public static final int INVERSE_SETUP_STATUS = 2;//倒数模式设置
    public static final int SCENE_SETUP_STATUS = 3;//实景模式设置
    public static final int CALORIE_SETUP_STATUS = 4;//卡路里模式设置
    public static final int PROGRAM_SETUP_STATUS = 5;//程式模式设置
    public static final int FIVE_SECOND_STATUS = 6;//开始准备
    public static final int NORMAL_RUN_STATUS = 7;//手动跑步
    public static final int INVERSE_RUN_STATUS = 8;//倒数跑步
    public static final int SCENE_RUN_STATUS = 9;//实景跑步
    public static final int CALORIE_RUN_STATUS = 10;//卡路里倒数跑步
    public static final int PROGRAM_RUN_STATUS = 11;//程式跑步
    public static final int END_STATUS = 12;//运动结束
    public static final int ERROR_STATUS = 14;//错误状态
    public static final int HRC_SETUP_STATUS = 16;//HRC 模式设置
    public static final int HRC_RUN_STATUS = 17;//HRC 跑步
    public static final int USER_SETUP_STATUS = 18;//自定义模式设置
    public static final int USER_RUN_STATUS = 19;//自定义跑步
//    public static final int FAT_RUN_STATUS = 21;//减脂模式跑步
//    public static final int RECOVERY_RUN_STATUS = 23;//康复模式跑步


    public static final int App = 40;//开启界面
    public static final int AppLogin = 41;//登录界面
    public static final int AppRegist = 42;//注册界面
    public static final int AppVisitor = 43;//访客
    public static final int DialogResult = 44;//运动结果
    public static final int NoAppVisitor = 45;//不是访客
    public static final int SETUP_STATUS = 46;//待机


    public static int getStatus() {
        return status;
    }

    public static void setStatus(int statu) {
        status = statu;
    }

    public static boolean isRunning() {
        if (getStatus() == NORMAL_RUN_STATUS ||
                getStatus() == INVERSE_RUN_STATUS ||
                getStatus() == SCENE_RUN_STATUS ||
                getStatus() == CALORIE_RUN_STATUS ||
                getStatus() == PROGRAM_RUN_STATUS ||
                getStatus() == HRC_RUN_STATUS ||
                getStatus() == USER_RUN_STATUS)
            return true;//跑步中
        else
            return false;
    }

    public static boolean isSetup() {
        if (getStatus() == INVERSE_SETUP_STATUS ||
                getStatus() == SCENE_SETUP_STATUS ||
                getStatus() == CALORIE_SETUP_STATUS ||
                getStatus() == PROGRAM_SETUP_STATUS ||
                getStatus() == HRC_SETUP_STATUS ||
                getStatus() == USER_SETUP_STATUS)
            return true;//跑步中
        else
            return false;
    }

    public static boolean isAppStart() {
        if (getStatus() == App || getStatus() == AppLogin ||getStatus() == AppRegist) {
            return true;//app开始界面
        }
        return false;
    }

    public static boolean isReady() {//跑步三状态
        if (isRunning() ||
                getStatus() == FIVE_SECOND_STATUS ||
                getStatus() == END_STATUS)
            return true;
        else
            return false;
    }
}
