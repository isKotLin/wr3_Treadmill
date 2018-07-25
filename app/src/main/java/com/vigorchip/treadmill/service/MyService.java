package com.vigorchip.treadmill.service;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.goole.zxing.client.android.EncodingUtils;
import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.DialogError;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.fragment.RunningFragment;
import com.vigorchip.treadmill.impl.MainToService;
import com.vigorchip.treadmill.impl.RunningToService;
import com.vigorchip.treadmill.impl.SerialToErrors;
import com.vigorchip.treadmill.impl.TimeToService;
import com.vigorchip.treadmill.module.HistoryList;
import com.vigorchip.treadmill.module.MyTime;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.okttp.UrlConstant;
import com.vigorchip.treadmill.serialport.SerialComm;
import com.vigorchip.treadmill.serialport.newSerial;
import com.vigorchip.treadmill.utils.DensityUtils;
import com.vigorchip.treadmill.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

import okhttp3.Call;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MyService extends Service implements View.OnClickListener, TimeToService, View.OnLongClickListener, RunningToService, MainToService {
    private SerialComm serialComm;
    private newSerial newSerial;
    private LinearLayout suspension_ll_stop, suspension_ll_pause, window_suspension_button, window_button, data_ll_slopes;
    private TextView suspension_tv_speed, suspension_tv_sloped, suspension_tv_time, suspension_tv_mileage, suspension_tv_heat, suspension_tv_heart;
    private static TextView speed_tv_sign, slope_tv_sign, time_tv_sign, distance_tv_sign, heat_tv_sign, hrc_tv_sign;
    private ImageView suspension_iv_home, suspension_iv_subtract, suspension_iv_plus, suspension_iv_multitasking, suspension_iv_back
//            ,su_back
            , suspension_iv_pause;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams, buttonWMParam, backParam;
    private static LinearLayout window_data_frame, suspension_ll;
    private static String startTime;
    private static Context mContext;

    DialogError dialogError;

    public static void setStartTime(String startTimes) {
        startTime = startTimes;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createDataFrame();
        createButton();
        initData();
    }

    private void initData() {
//        serialComm = new SerialComm(MyApplication.getInstance().getAppContext());

        //三为协议
        new newSerial().CreateSerial();
        MyTime.setTimeToService(this);
        mContext = getApplicationContext();
        RunningFragment.setRunningToService(this);
        MainActivity.setMainToService(this);
        newSerial.setSerialToErrors(new SerialToErrors() {
            @Override
            public void sendErrors(int error) {
                if (dialogError == null)
                    dialogError = new DialogError(mContext);
                dialogError.creatDialog(error);
            }
        });
    }

    private void createButton() {//悬浮返回键 mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        buttonWMParam = new WindowManager.LayoutParams();
        buttonWMParam.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
        buttonWMParam.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        buttonWMParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        buttonWMParam.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT; //调整悬浮窗显示位置
        buttonWMParam.width = WRAP_CONTENT;
        buttonWMParam.height = WRAP_CONTENT;
        backParam = new WindowManager.LayoutParams();
        backParam.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
        backParam.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        backParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        backParam.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT; //调整悬浮窗显示位置
        backParam.width = WRAP_CONTENT;
        backParam.height = WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        window_suspension_button = (LinearLayout) inflater.inflate(R.layout.window_suspension_button, null);
        window_button = (LinearLayout) inflater.inflate(R.layout.window_button, null);
        mWindowManager.addView(window_suspension_button, buttonWMParam);
        mWindowManager.addView(window_button, backParam);
        initButton();
        window_suspension_button.post(new Runnable() {
            @Override
            public void run() {
                viewHeight = window_suspension_button.getHeight();
                buttonWMParam.y = -viewHeight / 2;
                mWindowManager.updateViewLayout(window_suspension_button, buttonWMParam);
                restrict();
                goneHome();
            }
        });
    }

    private int isMove;//1按下 2移动 3展开
    private boolean isFacing;//按键是否要贴边
    private float oldButtonX, oldButtonY;
    private int viewHeight;

    private void initButton() {
        suspension_iv_home = window_suspension_button.findViewById(R.id.suspension_iv_home);
        suspension_iv_subtract = window_suspension_button.findViewById(R.id.suspension_iv_subtract);
        suspension_iv_plus = window_suspension_button.findViewById(R.id.suspension_iv_plus);
        suspension_iv_multitasking = window_suspension_button.findViewById(R.id.suspension_iv_multitasking);
        suspension_iv_back = window_button.findViewById(R.id.suspension_iv_back);
//        su_back = window_button.findViewById(su_back);

        suspension_iv_home.setOnClickListener(this);
        suspension_iv_subtract.setOnClickListener(this);
        suspension_iv_plus.setOnClickListener(this);
        suspension_iv_multitasking.setOnClickListener(this);
//        su_back.setOnClickListener(this);
        suspension_iv_back.setOnClickListener(this);
        suspension_iv_back.setOnLongClickListener(this);
        suspension_iv_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMove = 1;
                        isFacing = false;
                        oldButtonX = motionEvent.getRawX();
                        oldButtonY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        if (Math.abs(motionEvent.getRawX() - oldButtonX) > DensityUtils.px2dip(mContext, 10) || Math.abs(motionEvent.getRawY() - oldButtonY) > DensityUtils.px2dip(mContext, 10))
                            if (isMove == 1)
                                isMove = 2;
                        if (isMove == 2) {
                            isFacing = true;
                            buttonWMParam.x = (int) (MyApplication.getInstance().sWidthPix - motionEvent.getRawX() - view.getWidth() / 2);
                            buttonWMParam.y = (int) (motionEvent.getRawY() - MyApplication.getInstance().sHeightPix / 2 - viewHeight / 2);

                            backParam.x = (int) (MyApplication.getInstance().sWidthPix - motionEvent.getRawX() - view.getWidth() / 2);
                            backParam.y = (int) (motionEvent.getRawY() - MyApplication.getInstance().sHeightPix / 2 - suspension_iv_back.getHeight() / 2);
                            mWindowManager.updateViewLayout(window_suspension_button, buttonWMParam);
                            restrict();
                            mWindowManager.updateViewLayout(window_button, backParam);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isFacing)//TODO 要贴边
                            handler.post(facing);
                        break;
                }
                return isFacing;
            }
        });
    }

    private void restrict() {
        if (suspension_iv_home.getVisibility() == View.VISIBLE) {
            if (buttonWMParam.y <= viewHeight / 2 - MyApplication.getInstance().sHeightPix / 2 - DensityUtils.px2dip(mContext, 20)) {
                backParam.y = viewHeight - suspension_iv_back.getHeight() - MyApplication.getInstance().sHeightPix / 2 + DensityUtils.px2dip(mContext, 10);
                mWindowManager.updateViewLayout(window_button, backParam);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (isMove == 1)
            isMove = 3;
        if (isMove == 3) {
            isFacing = true;
            if (suspension_iv_home.getVisibility() == View.GONE) {
                showHome();
                restrict();
            } else if (suspension_iv_home.getVisibility() == View.VISIBLE) {
                goneHome();
            }
        }
        return false;
    }

    private void goneHome() {
        suspension_iv_home.setVisibility(View.GONE);
        suspension_iv_subtract.setVisibility(View.GONE);
        suspension_iv_plus.setVisibility(View.GONE);
        suspension_iv_multitasking.setVisibility(View.GONE);
    }

    private void showHome() {
        suspension_iv_home.setVisibility(View.VISIBLE);
        suspension_iv_subtract.setVisibility(View.VISIBLE);
        suspension_iv_plus.setVisibility(View.VISIBLE);
        suspension_iv_multitasking.setVisibility(View.VISIBLE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (isClick)
                    animDown();
            } else {
                mWindowManager.updateViewLayout(window_suspension_button, buttonWMParam);
                mWindowManager.updateViewLayout(window_button, backParam);
            }
        }
    };

    Runnable facing = new Runnable() {
        @Override
        public void run() {
            for (; buttonWMParam.x > 0 && buttonWMParam.x < MyApplication.getInstance().sWidthPix; ) {
                if (buttonWMParam.x > MyApplication.getInstance().sWidthPix / 2) {//往左边靠
                    buttonWMParam.x += 10;
                    backParam.x += 10;
                } else {//往右边靠
                    buttonWMParam.x -= 10;
                    backParam.x -= 10;
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
            handler.removeCallbacks(this);
        }
    };

    private void multitasking() {//多任务键
        try {
            Class serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService", String.class);
            IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
            Class statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[]{retbinder});
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (Exception e) { // no such mothed ??
            e.printStackTrace();
        }
    }

    private void setBackPressed(int value) {
        final int repeatCount = (KeyEvent.FLAG_VIRTUAL_HARD_KEY & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent evDown = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_DOWN, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD,
                0, KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        final KeyEvent evUp = new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                KeyEvent.ACTION_UP, value, repeatCount, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                KeyEvent.FLAG_VIRTUAL_HARD_KEY | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        Class<?> ClassInputManager;
        try {
            ClassInputManager = Class.forName("android.hardware.input.InputManager");
            Method[] methods = ClassInputManager.getMethods();
            Method methodInjectInputEvent = null;
            Method methodGetInstance = null;
            for (Method method : methods) {
                if (method.getName().contains("getInstance")) {
                    methodGetInstance = method;
                }
                if (method.getName().contains("injectInputEvent")) {
                    methodInjectInputEvent = method;
                }
            }
            Object instance = methodGetInstance.invoke(ClassInputManager, (Object[]) null); // boolean bool = InputManager.class.isInstance(instance); // methodInjectInputEvent =InputManager.getMethod("injectInputEvent",KeyEvent.class, Integer.class);
            methodInjectInputEvent.invoke(instance, evDown, 0);
            methodInjectInputEvent.invoke(instance, evUp, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDataFrame() {//悬浮数据栏
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;//设置window type
        wmParams.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.BOTTOM; //调整悬浮窗显示的停靠位置
        wmParams.width = MATCH_PARENT; //设置悬浮窗口长宽数据
        wmParams.height = WRAP_CONTENT;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        window_data_frame = (LinearLayout) inflater.inflate(R.layout.window_data_frame, null); //获取浮动窗口视图所在布局
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                return;
            } else {
                //执行6.0以上绘制代码
                mWindowManager.addView(window_data_frame, wmParams);//添加mFloatLayout
            }
        }
        initView();
    }

    float lastY;//点击是Y坐标
    boolean isClick;//是否点击数据栏

    private void initView() {
        suspension_ll = window_data_frame.findViewById(R.id.suspension_ll);
        data_ll_slopes = window_data_frame.findViewById(R.id.data_ll_slopes);

        suspension_ll_stop = window_data_frame.findViewById(R.id.suspension_ll_stop);
        suspension_ll_pause = window_data_frame.findViewById(R.id.suspension_ll_pause);
        suspension_iv_pause = window_data_frame.findViewById(R.id.suspension_iv_pause);

        suspension_tv_speed = window_data_frame.findViewById(R.id.suspension_tv_speed);
        suspension_tv_sloped = window_data_frame.findViewById(R.id.suspension_tv_sloped);
        suspension_tv_time = window_data_frame.findViewById(R.id.suspension_tv_time);
        suspension_tv_mileage = window_data_frame.findViewById(R.id.suspension_tv_mileage);
        suspension_tv_heat = window_data_frame.findViewById(R.id.suspension_tv_heat);
        suspension_tv_heart = window_data_frame.findViewById(R.id.suspension_tv_heart);

        speed_tv_sign = window_data_frame.findViewById(R.id.speed_tv_sign);
        slope_tv_sign = window_data_frame.findViewById(R.id.slope_tv_sign);
        time_tv_sign = window_data_frame.findViewById(R.id.time_tv_sign);
        distance_tv_sign = window_data_frame.findViewById(R.id.distance_tv_sign);
        heat_tv_sign = window_data_frame.findViewById(R.id.heat_tv_sign);
        hrc_tv_sign = window_data_frame.findViewById(R.id.hrc_tv_sign);

        suspension_ll_stop.setOnClickListener(this);
        suspension_ll_pause.setOnClickListener(this);
        if (MyApplication.getInstance().SLOPES == 0)
            data_ll_slopes.setVisibility(View.GONE);
        hide();
        window_data_frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastY = motionEvent.getRawY();
                        isClick = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getRawY() - lastY < 0) {
                            suspension_ll.setY(0);
                        } else if (motionEvent.getRawY() - lastY > suspension_ll.getHeight() / 2) {
                            suspension_ll.setY(suspension_ll.getHeight() / 2);
                        } else {
                            suspension_ll.setY(motionEvent.getRawY() - lastY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((motionEvent.getRawY() - lastY) > DensityUtils.px2dip(getApplicationContext(), 30)) {
                            animDown();
                        } else {
                            suspension_ll.setY(0);
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void animDown() {
        Animation animations = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.water_down);
        suspension_ll.startAnimation(animations);
        animations.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                suspension_ll.setY(0);
                window_data_frame.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void animUp() {
        v = 0;
        if (SportData.isRunning() && window_data_frame.getVisibility() == View.GONE && !RunningFragment.isShow()) {
            isClick = true;
            window_data_frame.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.water_up);
            suspension_ll.startAnimation(anim);
            upOrDown();
        }
    }

    @Override
    public void frameUp() {
        if (SportData.isReady() && window_data_frame.getVisibility() == View.GONE) {
            window_data_frame.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.water_up);
            suspension_ll.startAnimation(anim);
        }
    }

    @Override
    public void frameDown() {
        if (window_data_frame.getVisibility() == View.VISIBLE)
            animDown();
    }

    @Override
    public void frameDownss() {
        if (window_data_frame.getVisibility() == View.VISIBLE)
            animDown();
    }

    @Override
    public void frameUpss() {
        if (SportData.isReady() && window_data_frame.getVisibility() == View.GONE) {
            window_data_frame.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.water_up);
            suspension_ll.startAnimation(anim);
        }
    }

    public static void updatess() {//
        speed_tv_sign.setText(mContext.getString(R.string.speed));
        slope_tv_sign.setText(mContext.getString(R.string.sloped));
        time_tv_sign.setText(mContext.getString(R.string.time));
        distance_tv_sign.setText(mContext.getString(R.string.mileage));
        heat_tv_sign.setText(mContext.getString(R.string.heat));
        hrc_tv_sign.setText(mContext.getString(R.string.heart_rate));
    }

    public static class DownReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ((SportData.isRunning() || SportData.getStatus() == SportData.END_STATUS) && window_data_frame.getVisibility() == View.GONE && !RunningFragment.isShow()) {
                window_data_frame.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.water_up);
                suspension_ll.startAnimation(anim);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.suspension_ll_stop:
                MyTime.timeStop();
                break;
            case R.id.suspension_ll_pause:
                MyTime.timePause();
                break;
            case R.id.suspension_iv_home:
                setBackPressed(KeyEvent.KEYCODE_HOME);
                break;
            case R.id.suspension_iv_subtract:
//                setBackPressed(KeyEvent.KEYCODE_VOLUME_DOWN);
                serialComm.dowlower();
                break;
            case R.id.suspension_iv_plus:
//                setBackPressed(KeyEvent.KEYCODE_VOLUME_UP);
                serialComm.upRaise();
                break;
            case R.id.suspension_iv_multitasking:
//                setBackPressed(KeyEvent.KEYCODE_MENU);
                multitasking();
                break;
            case R.id.suspension_iv_back:
//            case su_back:
                LogUtils.log("clickBack");
                setBackPressed(KeyEvent.KEYCODE_BACK);
                break;
        }
    }

    public static void hide() {
        if (window_data_frame.getVisibility() == View.VISIBLE) {
            window_data_frame.setVisibility(View.GONE);
        }
    }

    @Override
    public void setDate() {
        suspension_tv_speed.setText((double) MyTime.getmSpeed() / 10 + "");
        suspension_tv_sloped.setText(MyTime.getmSloped() >= 10 ? MyTime.getmSloped() + "" : "0" + MyTime.getmSloped());
        suspension_tv_time.setText(MyTime.positiveTiming());
        suspension_tv_mileage.setText(MyTime.getmMileage() + "");
        suspension_tv_heat.setText(MyTime.getmHeat());
        suspension_tv_heart.setText(MyTime.getmHeart() >= 100 ? MyTime.getmHeart() + "" : MyTime.getmHeart() >= 10 ? "0" + MyTime.getmHeart() : "00" + MyTime.getmHeart());
    }

    @Override
    public void setSpeed(int speed) {
        suspension_tv_speed.setText((double) speed / 10 + "");
        animUp();
    }

    private void upOrDown() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    for (; v < 30; v++) {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    int v;

    @Override
    public void setStart() {
        suspension_ll_stop.setSelected(false);
    }

    @Override
    public void setStopping() {
        suspension_ll_stop.setSelected(true);
    }

    @Override
    public void setSloped(int sloped) {
        suspension_tv_sloped.setText(sloped >= 10 ? sloped + "" : "0" + sloped);
        animUp();
    }

    @Override
    public void setHeart(int heart) {
        suspension_tv_heart.setText(heart >= 100 ? heart + "" : heart >= 10 ? "0" + heart : "00" + heart);
    }

    @Override
    public void setImage() {
        if (MyTime.getStartOrStop() == 3) {
            suspension_iv_pause.setImageResource(R.mipmap.suspension_start);
//            suspension_ll_pause.setBackgroundResource(R.mipmap.yellow);
            suspension_ll_pause.setSelected(true);
        } else {
            suspension_iv_pause.setImageResource(R.mipmap.suspension_pause);
            suspension_ll_pause.setSelected(false);
//            suspension_ll_pause.setBackgroundResource(R.mipmap.green);
        }
    }

    @Override
    public void setResult() {
        animDown();
        final Dialog result = new Dialog(mContext, R.style.NoTitle);
        result.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        result.setCancelable(false);
        result.show();
        SportData.setStatus(SportData.DialogResult);
        result.setContentView(R.layout.dialog_result);
        ((TextView) result.findViewById(R.id.result_tv_avg_heart)).setText(MyTime.getAvgHeart() + " " + getString(R.string.bpm));
        ((TextView) result.findViewById(R.id.result_tv_heat)).setText(MyTime.getmHeat() + " " + getString(R.string.kilocalorie));
        ((TextView) result.findViewById(R.id.result_tv_time)).setText(MyTime.positiveTimess() + " " + getString(R.string.min));
        ((TextView) result.findViewById(R.id.result_tv_avg_speed)).setText(MyTime.getAvgSpeed() + " " + getString(R.string.per));
        ((TextView) result.findViewById(R.id.result_tv_pack)).setText(MyTime.getPace()// + getString(R.string.seconds)
        );
        ((TextView) result.findViewById(R.id.result_tv_mileage)).setText(MyTime.getmMileage());
        if (mContext.getResources().getConfiguration().locale.getLanguage().equals("en")) {
            ((TextView) result.findViewById(R.id.result_tv_scan)).setTextSize(DensityUtils.px2dip(mContext, 16));
            ((TextView) result.findViewById(R.id.result_tv_friend)).setTextSize(DensityUtils.px2dip(mContext, 16));//distance_tv_sign.setTextSize(DensityUtils.px2dip(mContext,20));
        } else if (mContext.getResources().getConfiguration().locale.getLanguage().equals("zh")) {
            ((TextView) result.findViewById(R.id.result_tv_scan)).setTextSize(DensityUtils.px2dip(mContext, 24));
            ((TextView) result.findViewById(R.id.result_tv_friend)).setTextSize(DensityUtils.px2dip(mContext, 24));//distance_tv_sign.setTextSize(DensityUtils.px2dip(mContext,24));
        }
        try {
            ((ImageView) result.findViewById(R.id.iv_QRCodes))
                    .setImageBitmap(EncodingUtils.createQRCode("http://39.108.225.155/vc_tp/Home/Share/share?mileage="
                            + MyTime.getmMileage() + "&time=" + MyTime.positiveTimess() + "&kaluli=" + MyTime.getmHeat()
                            + "&peisu=" + MyTime.getPace() + "&speed=" + MyTime.getAvgSpeed() + "&xinlv="
                            + MyTime.getAvgHeart(), DensityUtils.px2dip(mContext, 150)));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        result.findViewById(R.id.result_tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result != null && result.isShowing()) {
                    SportData.setStatus(SportData.SETUP_STATUS);
                    result.dismiss();
                }
            }
        });
        suspension_iv_pause.setImageResource(R.mipmap.suspension_pause);
        suspension_ll_pause.setSelected(false);
        if (!User.getUserId().equals("-1")) {
            positiveTiming = MyTime.getTimess();
            getPace = MyTime.getPace();
            getmMileage = MyTime.getmMileage();
            getmHeat = MyTime.getmHeat();
            getAvgHeart = MyTime.getAvgHeart();
            runnable.run();
        }
    }

    int positiveTiming;
    String getPace, getmMileage, getmHeat, getAvgHeart;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", User.getUserId());
            map.put("starttime", startTime);
            map.put("totletime", positiveTiming + "");
            map.put("speed", getPace);
            map.put("distance", getmMileage);
            map.put("kaluli", getmHeat);
            map.put("xinlv", getAvgHeart);
            Okhttp.get(false, UrlConstant.UPDATA_RUNRECORD, map, new Okhttp.CallBac() {
                @Override
                public void onError(Call call, Exception e, String state, int id) {
                }

                @Override
                public void onResponse(String response, int id) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.optBoolean("success")) {
                            HistoryList historyList = new HistoryList();
                            historyList.setPace(getPace);
                            historyList.setXinlv(getAvgHeart);
                            historyList.setKaluli(getmHeat);
                            historyList.setDistance(getmMileage);
                            historyList.setTotletime(positiveTiming);
                            historyList.setStartTime(startTime);
                            Silently.setHistoryList(historyList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNoNetwork(String state) {
                }
            });
        }
    };
}
