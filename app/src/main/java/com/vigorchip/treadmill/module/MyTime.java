package com.vigorchip.treadmill.module;

import android.os.Handler;
import android.os.Message;

import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.fragment.CustomModeFragment;
import com.vigorchip.treadmill.fragment.HRCFragment;
import com.vigorchip.treadmill.fragment.InverseModeFragment;
import com.vigorchip.treadmill.fragment.ProgramModeFragment;
import com.vigorchip.treadmill.fragment.VideoFragment;
import com.vigorchip.treadmill.impl.TimeToRunning;
import com.vigorchip.treadmill.impl.TimeToService;

import java.text.DecimalFormat;


public class MyTime {
    private static int timeShow;//跑步时间
    private static int startOrStop;//跑步状态  1 跑步中   2 停止跑步  3 暂停跑步
    private static int mSpeed;//速度
    private static int mSloped;//扬升
    private static int oldSpeed;//速度
    private static int oldSloped;//扬升
    private static double mMileage;//里程
    private static int mHeart;//心率
    private static double mHeat;//卡路里
    private static TimeToRunning timeToRunning;//回调 RunningFragment
    private static TimeToService timeToService;//回调 MyService
    private static DecimalFormat mileageFormat, heatFormat;//格式卡路里
    private static int downTime;//倒数时间
    private static int downMileage;//倒数距离
    private static int downHeat;//倒数卡路里
    private static int targetHreat;//目标心率
    private boolean isStart;//是否开始跑步
    private static StringBuffer strTime;
    private static int avgHeart, heartTime;

    public static void setTimeToService(TimeToService timeToServices) {
        timeToService = timeToServices;
    }

    public static void setTimeToRunning(TimeToRunning timeToRunnings) {
        timeToRunning = timeToRunnings;
    }

    public void timeStart() {
        mileageFormat = new DecimalFormat("0.00");
        heatFormat = new DecimalFormat("0.0");
        strTime = new StringBuffer();
        isStart = true;
        timeShow = 0;
        show = 1;
        mMileage = 0;
        mHeat = 0;
        mHeart = 0;
        mSloped = 0;
        num = 0;
        mSpeed = MyApplication.getInstance().MINSPEED;
        startOrStop = 1;
//        timePause();
//        timeToRunning.setImage();
//        timeToService.setImage();
        timeToService.setStart();
        handler.post(runnable);
    }

    public void inverseStart() {
        timeStart();
        downTime = Integer.parseInt(InverseModeFragment.getTimeSet()) * 60;
        downMileage = Integer.parseInt(InverseModeFragment.getMileageSet());
        downHeat = Integer.parseInt(InverseModeFragment.getHeatSet());
    }

    public void HRCStart() {
        timeStart();
        downTime = Integer.parseInt(HRCFragment.getTimeSet()) * 60;
        targetHreat = Integer.parseInt(HRCFragment.getHeartSet());
    }

    private static int index; //模式
    private static int num;//正在跑那个

    public void proaramStart() {
        timeStart();
        downTime = ProgramModeFragment.getTime() * 60;
        index = ProgramModeFragment.getIndexs();
    }

    public void userStart() {
        timeStart();
        downTime = CustomModeFragment.getTimess() * 60;
        index = CustomModeFragment.getCurrentss();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Thread.currentThread().getPriority() != 9)
                Thread.currentThread().setPriority(9);
            switch (startOrStop) {
                case 1:
                    handler.sendEmptyMessage(1);
                    handler.postDelayed(this, 1000);
                    break;
                case 2:
                    if (mSpeed <= 0 && mSloped <= 0) {
                        SportData.setStatus(SportData.NORMAL_STATUS);
                        timeToService.setResult();
                        timeToRunning.setResult();
                        handler.removeCallbacks(this);
                    } else {
                        SportData.setStatus(SportData.END_STATUS);
                        timeToService.setStopping();
                        mSpeed = (mSpeed - 1) < 0 ? 0 : (mSpeed - 1);
                        timeToRunning.setSpeed(mSpeed);
                        timeToService.setSpeed(mSpeed);
                        mSloped = (mSloped - 1) < 0 ? 0 : (mSloped - 1);
                        timeToService.setSloped(mSloped);
                        timeToRunning.setSloped(mSloped);
                        handler.postDelayed(this, 200);
                    }
                    break;
                case 3:
                    handler.sendEmptyMessage(2);
                    handler.postDelayed(this, 1000);
                    break;
            }
        }
    };

    public static void timeStop() {
        startOrStop = 2;
        timeToRunning.setStopping();
    }

    public static int getStartOrStop() {
        return startOrStop;
    }

    public static void timePause() {//跑步暂停
//        if (SportData.isRunning()) {
        if (startOrStop == 1) {
            startOrStop = 3;
            oldSpeed = mSpeed;
            oldSloped = mSloped;
            mSpeed = 0;
            mSloped = 0;
        } else if (startOrStop == 3) {
            startOrStop = 1;
            mSpeed = oldSpeed;
            mSloped = oldSloped;
        }
        timeToRunning.setImage();
        timeToService.setImage();
//        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isStart)
                        isStart = false;
                    else
                        timeShow++;
                    mMileage = mMileage + (mSpeed / 36000.0);
                    double heatSpeed = ((double) mSpeed / 10);
                    if (heatSpeed * 0.6213711 <= 3.7) {
                        mHeat += (1 + (0.768 * heatSpeed * 0.6213711) + (0.137 * heatSpeed * 0.6213711 * (mSloped + 1))) * User.getTimeUserWeight() / (2.2 * 3600);
                    } else if (heatSpeed * 0.6213711 > 3.7) {
                        mHeat += (1 + (1.532 * heatSpeed * 0.6213711) + (0.0685 * heatSpeed * 0.6213711 * (mSloped + 1))) * User.getTimeUserWeight() / (2.2 * 3600);
                    }
                    if (startOrStop == 1 && mHeart != 0) {
                        avgHeart = mHeart + avgHeart;
                        heartTime++;
                    }
                    break;
                case 2://TODO 暂停
                    break;
            }
            timeToService.setDate();
            timeToRunning.setData();
        }
    };

    private static int show;

    public static String positiveTiming() {
        strTime.setLength(0);
        if (SportData.getStatus() == SportData.INVERSE_RUN_STATUS ||
                SportData.getStatus() == SportData.PROGRAM_RUN_STATUS ||
                SportData.getStatus() == SportData.HRC_RUN_STATUS ||
                SportData.getStatus() == SportData.USER_RUN_STATUS) {
            typeBin();
            if (timeShow >= downTime) {//startOrStop = 2;
                timeStop();
            }
            strTime.append((downTime - timeShow) / 3600 > 0 ? (downTime - timeShow) / 3600 + ":" : "");//时
            strTime.append(((downTime - timeShow) % 3600 / 60 > 9 ? (downTime - timeShow) % 3600 / 60 : "0" + (downTime - timeShow) % 3600 / 60) + ":");//分
            strTime.append((downTime - timeShow) % 3600 % 60 > 9 ? (downTime - timeShow) % 3600 % 60 : "0" + (downTime - timeShow) % 3600 % 60);//秒
        } else {
            strTime.append(timeShow / 3600 > 0 ? timeShow / 3600 + ":" : "");//时
            strTime.append((timeShow % 3600 / 60 > 9 ? timeShow % 3600 / 60 : "0" + timeShow % 3600 / 60) + ":");//分
            strTime.append(timeShow % 3600 % 60 > 9 ? timeShow % 3600 % 60 : "0" + timeShow % 3600 % 60);//秒
        }
        return strTime.toString();
    }

    private static void typeBin() {
        if (SportData.getStatus() == SportData.PROGRAM_RUN_STATUS) {//程序模式
            if (timeShow == (downTime * num / SaveFixedDateUtils.PROGRAM_TABLE.get(index)[0].length)) {
                if (num != SaveFixedDateUtils.PROGRAM_TABLE.get(index)[0].length) {
                    setmSpeed(SaveFixedDateUtils.PROGRAM_TABLE.get(index)[0][num]);
                    setmSloped(SaveFixedDateUtils.PROGRAM_TABLE.get(index)[1][num]);
                    if (show != timeShow) {
                        show = timeShow;
                        num++;
                    }
                }
            }
        } else if (SportData.getStatus() == SportData.USER_RUN_STATUS) {//自定义模式
            if (timeShow == (downTime * num / Silently.CUSTOM_TABLE.get(index)[0].length)) {
                if (num != Silently.CUSTOM_TABLE.get(index)[0].length) {
                    setmSpeed(Silently.CUSTOM_TABLE.get(index)[0][num]);
                    setmSloped(Silently.CUSTOM_TABLE.get(index)[1][num]);
                    if (show != timeShow) {
                        show = timeShow;
                        num++;
                    }
                }
            }
        }
    }

    private static int nextTime;

    public static String getPace() {
        nextTime = (int) (timeShow / mMileage);
//        nextTime = (int) (3600 / avgSpeed);
        return (nextTime / 60 > 9 ? nextTime / 60 : "0" + nextTime / 60) + ":" + (nextTime % 60 > 9 ? nextTime % 60 : "0" + nextTime % 60);
//时分秒return (nextTime / 3600) + ":" + (nextTime / 60 >= 60 ? "00" : (nextTime / 60 < 10 ? "0" + nextTime / 60 : nextTime / 60)) + ":" + (nextTime % 60 < 10 ? "0" + nextTime % 60 : nextTime % 60);
    }

    public static String positiveTimess() {
        strTime.setLength(0);
        strTime.append(timeShow / 3600 > 0 ? timeShow / 3600 + ":" : "");//时
        strTime.append((timeShow % 3600 / 60 > 9 ? timeShow % 3600 / 60 : "0" + timeShow % 3600 / 60) + ":");//分
        strTime.append(timeShow % 3600 % 60 > 9 ? timeShow % 3600 % 60 : "0" + timeShow % 3600 % 60);//秒
        return strTime.toString();
    }

    public static int getTimeShow() {
        if (SportData.getStatus() == SportData.INVERSE_RUN_STATUS ||
                SportData.getStatus() == SportData.PROGRAM_RUN_STATUS ||
                SportData.getStatus() == SportData.HRC_RUN_STATUS ||
                SportData.getStatus() == SportData.USER_RUN_STATUS) {
            if (SportData.getStatus() == SportData.HRC_RUN_STATUS) {//心率模式
                if (timeShow > 60 && timeShow % 10 == 0) {
                    if (mHeart != 0) {
                        if (targetHreat > mHeart) {
                            mSpeed += 0.5;
                        }
                        if (targetHreat < mHeart) {
                            mSpeed -= 0.5;
                        }
                        setmSpeed(mSpeed);
                    }
                }
            }
            return downTime - timeShow;
        } else
            return timeShow;
    }

    public static int getTimess() {
        return timeShow;
    }

    public static String getAvgSpeed() {
        if (timeShow == 0)
            return "0";
        return heatFormat.format(mMileage * 3600 / timeShow);
    }

    public static int getmSpeed() {
        return mSpeed;
    }

    public static void setmSpeed(int mSpeeds) {
        if (SportData.isRunning() && startOrStop != 3) {
            if (mSpeeds < MyApplication.getInstance().MINSPEED)
                mSpeeds = MyApplication.getInstance().MINSPEED;
            else if (mSpeeds > MyApplication.getInstance().MAXSPEED)
                mSpeeds = MyApplication.getInstance().MAXSPEED;
            mSpeed = mSpeeds;
            VideoFragment.setMPSpeed(((getmSpeed() - MyApplication.getInstance().MINSPEED) / getPlaySpeed() / 10 + 0.5f));
            timeToService.setSpeed(mSpeed);
            timeToRunning.setSpeed(mSpeed);
        }
    }

    private static float playSpeed;//播放速度

    public static float getPlaySpeed() {
        playSpeed = (float) ((MyApplication.getInstance().MAXSPEED - MyApplication.getInstance().MINSPEED) / 12);
        return playSpeed;
    }

    public static int getmSloped() {
        return mSloped;
    }

    public static String getAvgHeart() {
        if (heartTime == 0)
            return "00";
        return heatFormat.format(avgHeart / heartTime);
    }

    public static void setmSloped(int mSlopeds) {
        if (SportData.isRunning() && startOrStop != 3) {
            if (mSlopeds < 0)
                mSlopeds = 0;
            else if (mSlopeds > MyApplication.getInstance().SLOPES)
                mSlopeds = MyApplication.getInstance().SLOPES;
            mSloped = mSlopeds;
            timeToService.setSloped(mSloped);
            timeToRunning.setSloped(mSloped);
        }
    }

    public static String getmMileage() {
        if (SportData.getStatus() == SportData.INVERSE_RUN_STATUS) {
            if (mMileage >= downMileage) {//startOrStop = 2;
                timeStop();
                mMileage = downMileage;
            }
            return heatFormat.format(downMileage - mMileage);
        } else {
            return mileageFormat.format(mMileage % 100);
        }
    }

    public static int getmHeart() {
        return mHeart;
    }

    public static void setmHeart(int mHearts) {
        mHeart = mHearts;
        timeToService.setHeart(mHeart);
        timeToRunning.setHeart(mHeart);
    }

    public static String getmHeat() {
        if (SportData.getStatus() == SportData.INVERSE_RUN_STATUS) {
            if (mHeat >= downHeat) {
//                startOrStop = 2;
                timeStop();
                mHeat = downHeat;
            }
            return heatFormat.format(downHeat - mHeat);
        } else {
            return heatFormat.format(mHeat % 1000);
        }
    }
}