package com.vigorchip.treadmill.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.adapter.HomePageAdapter;
import com.vigorchip.treadmill.base.BaseActivity;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.DialogReady;
import com.vigorchip.treadmill.dialog.DialogUserAble;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.fragment.AdjustCustomFragment;
import com.vigorchip.treadmill.fragment.AppHomePageFragment;
import com.vigorchip.treadmill.fragment.CompleteInfoFragment;
import com.vigorchip.treadmill.fragment.CustomModeFragment;
import com.vigorchip.treadmill.fragment.HRCFragment;
import com.vigorchip.treadmill.fragment.HandFragment;
import com.vigorchip.treadmill.fragment.InverseModeFragment;
import com.vigorchip.treadmill.fragment.LoginFragment;
import com.vigorchip.treadmill.fragment.OperatorFragment;
import com.vigorchip.treadmill.fragment.PersonalInfoFragment;
import com.vigorchip.treadmill.fragment.ProgramModeFragment;
import com.vigorchip.treadmill.fragment.RegisterFragment;
import com.vigorchip.treadmill.fragment.RunningFragment;
import com.vigorchip.treadmill.fragment.SceneListFragment;
import com.vigorchip.treadmill.fragment.SettingHomePageFragment;
import com.vigorchip.treadmill.fragment.SportHomePageFragment;
import com.vigorchip.treadmill.fragment.StartupFragment;
import com.vigorchip.treadmill.fragment.VideoFragment;
import com.vigorchip.treadmill.impl.MainToService;
import com.vigorchip.treadmill.impl.ReadyToMain;
import com.vigorchip.treadmill.impl.SerialToMain;
import com.vigorchip.treadmill.module.MyTime;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.serialport.newSerial;
import com.vigorchip.treadmill.service.MyService;
import com.vigorchip.treadmill.utils.LogUtils;
import com.vigorchip.treadmill.utils.NowDateTime;
import com.vigorchip.treadmill.utils.UpdataSystemUtil;
import com.vigorchip.treadmill.utils.ZoomOutPageTransformer;
import com.vigorchip.treadmill.view.HomeViewPager;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public HomeViewPager activity_vp_main;
    private final String CURRENTINEXD = "currentItem";
    private List<Fragment> fragmentList = new ArrayList<>();//首页fragment列表
    private HomePageAdapter homePageAdapter;

    private FrameLayout activity_fl_main;//运动模式
    private FragmentManager fragmentManager;
    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    public static void setMainToService(MainToService mainToServices) {
        mainToService = mainToServices;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    private int currentIndex;

    private TextView activity_tv_time_main, activity_main_mode, personal_tv_info_icon, sport_tv_record, home_tv_setting, home_tv_app, home_tv_sport;
    private LinearLayout activity_tv_home_main, activity_tv_setting_main, activity_tv_app_main, personal_ll_info,user_ll_cancellation;
    private RelativeLayout activity_main_title;//标题
    private ImageView activity_main_back, activity_iv_wifi_icon;//返回图标   wifi图标
    private WifiManager wifiManager;
    private WifiInfo currentWifiInfo;
    private DialogReady ready;
    private MyTime myTime;
    private int video_index;
    private boolean isNet = false;//是否联网
    private int netType = 1;
    //public static String getDATAPATH() {return DATAPATH;}
    //private static String DATAPATH = "/mydata/save.txt";
    @Override
    protected void onResume() {
        super.onResume();
        if (SportData.isRunning()){
            if (activity_vp_main.getVisibility()==View.GONE&&currentIndex==7){
                RunningFragment.setIsShow(true);
            }
        }
        DialogReady.setReadyToMain(new ReadyToMain() {//模式开始
            @Override
            public void showSport(int mode) {
                myTime = new MyTime();
                switch (mode) {
                    case SportData.INVERSE_SETUP_STATUS://倒数
                        SportData.setStatus(SportData.INVERSE_RUN_STATUS);
                        setMode(getString(R.string.inverted_mode));
                        myTime.inverseStart();
                        break;
                    case SportData.SCENE_SETUP_STATUS://实景
                        SportData.setStatus(SportData.SCENE_RUN_STATUS);
                        setMode(getString(R.string.scene_mode));
                        myTime.timeStart();
                        break;
                    case SportData.PROGRAM_SETUP_STATUS://程序
                        SportData.setStatus(SportData.PROGRAM_RUN_STATUS);
                        setMode(getString(R.string.program_mode));
                        myTime.proaramStart();
                        break;
                    case SportData.USER_SETUP_STATUS://自定义
                        SportData.setStatus(SportData.USER_RUN_STATUS);
                        setMode(getString(R.string.custom_mode));
                        myTime.userStart();
                        break;
                    case SportData.HRC_SETUP_STATUS://心率
                        SportData.setStatus(SportData.HRC_RUN_STATUS);
                        setMode(getString(R.string.heart_rate_mode));
                        myTime.HRCStart();
                        break;
                    default: //手动
                        SportData.setStatus(SportData.NORMAL_RUN_STATUS);
                        setMode(getString(R.string.manual_mode));
                        myTime.timeStart();
                        break;
                }
                if (SportData.getStatus() != SportData.SCENE_RUN_STATUS) {
//                    if (currentIndex == 7)
//                        showSportMode();
//                    else
//                    RunningFragment.setIsShow(true);
                    setCurrentIndex(7);
                } else {
                    setCurrentIndex(8);
                    mainToService.frameUpss();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {//加载布局
        try {
            registerHomeKeyReceiver(this);
        } catch (Exception e) {
            LogUtils.log("Home键注册错误");
        }
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {//初始化View
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        activity_vp_main = findViewById(R.id.activity_vp_main);
        activity_tv_app_main = findViewById(R.id.activity_tv_app_main);
        activity_tv_home_main = findViewById(R.id.activity_tv_home_main);
        activity_tv_setting_main = findViewById(R.id.activity_tv_setting_main);
        activity_tv_time_main = findViewById(R.id.activity_tv_time_main);
        activity_main_title = findViewById(R.id.activity_main_title);
        activity_fl_main = findViewById(R.id.activity_fl_main);
        activity_main_back = findViewById(R.id.activity_main_back);
        activity_main_mode = findViewById(R.id.activity_main_mode);
        activity_iv_wifi_icon = findViewById(R.id.activity_iv_wifi_icon);
        personal_ll_info = findViewById(R.id.personal_ll_info);
        user_ll_cancellation = findViewById(R.id.user_ll_cancellation);
        personal_tv_info_icon = findViewById(R.id.personal_tv_info_icon);
        sport_tv_record = findViewById(R.id.sport_tv_record);
        home_tv_sport = findViewById(R.id.home_tv_sport);
        home_tv_app = findViewById(R.id.home_tv_app);
        home_tv_setting = findViewById(R.id.home_tv_setting);
        home_tv_sport.setTextColor(Color.GREEN);
        home_tv_app.setTextColor(Color.WHITE);
        home_tv_setting.setTextColor(Color.WHITE);
    }

    @Override
    protected void initData() {//初始化数据
        homeViewpager();
        startService(new Intent(this, MyService.class));
        activity_tv_home_main.setSelected(true);
        activity_tv_home_main.setOnClickListener(this);
        activity_tv_app_main.setOnClickListener(this);
        activity_tv_setting_main.setOnClickListener(this);
        activity_main_back.setOnClickListener(this);
        sport_tv_record.setOnClickListener(this);
        personal_tv_info_icon.setOnClickListener(this);
        user_ll_cancellation.setOnClickListener(this);
        activity_vp_main.addOnPageChangeListener(this);
        sportModeFragment();
        handler.post(runnable);
    }

    private void homeViewpager() {//加载首页viewpager
        fragmentList.add(new SportHomePageFragment());
        fragmentList.add(new AppHomePageFragment());
        fragmentList.add(new SettingHomePageFragment());
        homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), fragmentList);
        activity_vp_main.setPageTransformer(true, new ZoomOutPageTransformer());
        activity_vp_main.setAdapter(homePageAdapter);
    }

    private void sportModeFragment() {//加载运动模式
        fragmentManager = getSupportFragmentManager();
        fragments.add(new ProgramModeFragment());//0 程序模式
        fragments.add(new CustomModeFragment());//1 自定义模式
        fragments.add(new InverseModeFragment());//2 倒数模式
        fragments.add(new HRCFragment());//3 心率模式
        fragments.add(new HandFragment());//4 运动记录
        fragments.add(new SceneListFragment());//5 实景列表模式
        fragments.add(new OperatorFragment());//6 操作指南
        fragments.add(new RunningFragment());//7 跑步中
        fragments.add(new VideoFragment());//8 实景模式
        fragments.add(new AdjustCustomFragment());//9 自定义调整
        fragments.add(new CompleteInfoFragment());//10 完善信息
        fragments.add(new LoginFragment());//11 登录
        fragments.add(new PersonalInfoFragment());//12 个人信息
        fragments.add(new RegisterFragment());//13 注册
        fragments.add(new StartupFragment());//14 选择登录注册游客
        if (MyApplication.getInstance().isFirst()) {
            setCurrentIndex(7);
            if (TextUtils.isEmpty(MyApplication.getInstance().getSP().getString("abini", null)))//是否保存了用户
                setCurrentIndex(14);
            else
                setCurrentIndex(11);
            MyApplication.getInstance().setIsFirst(false);
        }
        if (MyApplication.getInstance().isChangLanguage()) {
            setCurrentIndex(7);
            cuss();
        }
        newSerial.setSerialToMain(new SerialToMain() {
            @Override
            public void send() {
                startRun();
            }
        });
    }

    private int oldIndex;//保存切换前页卡
    private boolean isRegister = false;

    public void setIsRegister(boolean isRegisters) {
        isRegister = isRegisters;
    }

    public void setCurrentIndex(int index) {//设置fragment页数
        currentIndex = index;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragments.get(currentIndex).isAdded() && null == getSupportFragmentManager().findFragmentByTag("TAG" + currentIndex)) {  //如果之前没有添加过
            if (oldIndex == 7
//                    ||oldIndex==8
                    ) {
                transaction.hide(currentFragment);
            } else
                transaction.detach(currentFragment);
            transaction.add(R.id.activity_fl_main, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的frgment时绑定一个tag
        } else {
            if (oldIndex == 7
//                    ||oldIndex==8
                    ) {
                transaction.hide(currentFragment);
            } else
                transaction.detach(currentFragment);
        }
        currentFragment = fragments.get(currentIndex);
        if (currentIndex == 7
//                ||currentIndex==8
                )
            transaction.show(currentFragment);
        else
            transaction.attach(currentFragment);
        transaction.commitAllowingStateLoss();
        showSportMode();
        oldIndex = currentIndex;
    }

    private void showSportMode() {//隐藏ViewPager，显示FrameLayout
        showOrGone();
        activity_fl_main.setVisibility(View.VISIBLE);
        activity_vp_main.setVisibility(View.GONE);
        activity_tv_time_main.setVisibility(View.GONE);
        activity_tv_app_main.setVisibility(View.GONE);
        activity_tv_home_main.setVisibility(View.GONE);
        activity_tv_setting_main.setVisibility(View.GONE);
    }

    private void showOrGone() {
        if (currentIndex == 14 || currentIndex == 8)
            activity_main_title.setVisibility(View.GONE);
        else
            activity_main_title.setVisibility(View.VISIBLE);
        if (currentIndex == 7 || currentIndex == 10 || currentIndex == 13 || currentIndex == 11)
            activity_main_back.setVisibility(View.GONE);
        else
            activity_main_back.setVisibility(View.VISIBLE);
        if (currentIndex == 11 || currentIndex == 13 || currentIndex == 14)
            activity_iv_wifi_icon.setVisibility(View.VISIBLE);
        else
            activity_iv_wifi_icon.setVisibility(View.GONE);
        if (currentIndex == 4 || currentIndex == 12) {
            personal_ll_info.setVisibility(View.VISIBLE);
            activity_main_mode.setVisibility(View.GONE);
            if (currentIndex == 4) {
                personal_tv_info_icon.setTextSize(20);
                user_ll_cancellation.setVisibility(View.GONE);
                sport_tv_record.setTextSize(30);
            } else if (currentIndex == 12) {
                personal_tv_info_icon.setTextSize(30);
                sport_tv_record.setTextSize(20);
                user_ll_cancellation.setVisibility(View.VISIBLE);
            }
        } else {
            user_ll_cancellation.setVisibility(View.GONE);
            personal_ll_info.setVisibility(View.GONE);
            activity_main_mode.setVisibility(View.VISIBLE);
        }
    }

    public void showHomePager() {//隐藏FrameLayout，显示ViewPager
        activity_vp_main.setVisibility(View.VISIBLE);
        activity_tv_time_main.setVisibility(View.VISIBLE);
        activity_iv_wifi_icon.setVisibility(View.VISIBLE);
        activity_tv_app_main.setVisibility(View.VISIBLE);
        activity_tv_home_main.setVisibility(View.VISIBLE);
        activity_tv_setting_main.setVisibility(View.VISIBLE);
        activity_main_title.setVisibility(View.GONE);
        activity_fl_main.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {//滑动时
    }

    @Override
    public void onPageSelected(int position) {//滑动结束 homePageAdapter.notifyDataSetChanged();
        switch (position) {
            case 0:
                activity_tv_home_main.setSelected(true);
                activity_tv_app_main.setSelected(false);
                activity_tv_setting_main.setSelected(false);
                home_tv_sport.setTextColor(Color.GREEN);
                home_tv_app.setTextColor(Color.WHITE);
                home_tv_setting.setTextColor(Color.WHITE);
                break;
            case 1:
                activity_tv_home_main.setSelected(false);
                activity_tv_app_main.setSelected(true);
                activity_tv_setting_main.setSelected(false);
                home_tv_sport.setTextColor(Color.WHITE);
                home_tv_app.setTextColor(Color.GREEN);
                home_tv_setting.setTextColor(Color.WHITE);
                break;
            case 2:
                activity_tv_home_main.setSelected(false);
                activity_tv_app_main.setSelected(false);
                activity_tv_setting_main.setSelected(true);
                home_tv_sport.setTextColor(Color.WHITE);
                home_tv_app.setTextColor(Color.WHITE);
                home_tv_setting.setTextColor(Color.GREEN);
                break;
        }
        if (Silently.getCheck() == 2)
            update();
    }

    @Override
    public void onPageScrollStateChanged(int state) {//改变
    }

    private void update() {
        Silently.setCheck(1);
        if (MyApplication.getInstance().getDoorStatus() == 0) {
            DialogUserAble userAble = new DialogUserAble(MainActivity.this);
            userAble.creatDialog();
        }
        if (Silently.getDownSystemUrl() != null) {
            downSystemLoad();
        } else if (Silently.getDownloadUrl() != null) {
            downLoad();
        }
    }

    public void downSystemLoad() {
        dialogs = new AlertDialog.Builder(MainActivity.this).create();
        dialogs.setCancelable(false);
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.update);
        update_pb = (ProgressBar) dialogs.findViewById(R.id.update_pb);
        replaced = (Button) dialogs.findViewById(R.id.replaced);
        next = (Button) dialogs.findViewById(R.id.next);
        update_tv = (TextView) dialogs.findViewById(R.id.update_tv);
        update_tv.setText(R.string.system_update);
        close = (Button) dialogs.findViewById(R.id.close);
        close.setOnClickListener(this);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogs.isShowing())
                    dialogs.dismiss();
                if (!TextUtils.isEmpty(Silently.getDownloadUrl())) {
                    downLoad();
                }
            }
        });
        replaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaced.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                update_tv.setText(R.string.recovy);
                update_pb.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                Okhttp.downloadFile(Silently.getDownSystemUrl(), "update.zip", new Okhttp.FileCallBac() {
                    @Override
                    public void onNoNetwork(String state) {
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(final File response, int id) {
                        if (dialogs != null && dialogs.isShowing()) {
                            UpdataSystemUtil.updataSystem(MainActivity.this, response);
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        update_pb.setProgress((int) (progress * 100));
                    }
                });
            }
        });
    }

    ProgressBar update_pb;
    Button replaced, next, close;
    public AlertDialog dialogs;
    private TextView update_tv;

    public void downLoad() {
        dialogs = new AlertDialog.Builder(MainActivity.this).create();
        dialogs.setCancelable(false);
        dialogs.show();
        dialogs.getWindow().setContentView(R.layout.update);
        update_pb = (ProgressBar) dialogs.findViewById(R.id.update_pb);
        replaced = (Button) dialogs.findViewById(R.id.replaced);
        next = (Button) dialogs.findViewById(R.id.next);
        update_tv = (TextView) dialogs.findViewById(R.id.update_tv);
        update_tv.setText(R.string.app_update);
        close = (Button) dialogs.findViewById(R.id.close);
        close.setOnClickListener(this);
        next.setOnClickListener(this);
        replaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaced.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                update_tv.setText(R.string.recovy);
                update_pb.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                Okhttp.downloadFile(Silently.getDownloadUrl(), "SmartRun.apk", new Okhttp.FileCallBac() {
                    @Override
                    public void onNoNetwork(String state) {
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(final File response, int id) {
                        if (dialogs != null && dialogs.isShowing()) {
//                            sendBroadcast(new Intent("android.intent.action.rc.userinstall.enable"));
//                            installApk(MainActivity.this, response);
//                            dialogs.dismiss();
//                            sendBroadcast(new Intent("android.intent.action.rc.userinstall.disable"));
//                            Logutil.e(response.getAbsolutePath());
                            DataOutputStream os = null;
                            Process process = null;
                            try {
                                process = Runtime.getRuntime().exec("su");
                                os = new DataOutputStream(process.getOutputStream());
                                os.writeBytes("mount -o remount /system" + "\n");
                                os.writeBytes("cp " + response.getAbsolutePath() + " /system/priv-app/SmartRun.apk" + "\n");
                                os.writeBytes("chmod 0644 /system/priv-app/SmartRun.apk" + "\n");
                                os.writeBytes("rm " + response.getAbsolutePath() + "\n");
                                os.writeBytes("reboot" + "\n");
                                os.flush();
                                process.waitFor();
                            } catch (Exception e) {
//                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        update_pb.setProgress((int) (progress * 100));
                    }
                });
            }
        });
    }

    public static void installApk(Activity activity, File apkfile) {// 安装apk
        if (!apkfile.exists()) {
            return;
        }
        LogUtils.log("installApk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            LogUtils.log("installApk2");
            //判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(activity,
                    "com.vigorchip.treadmill.wr2",
                    apkfile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            LogUtils.log("installApk3");
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }

        activity.startActivity(intent);
        activity.finish();
        LogUtils.log("installApk4");
    }

    public void startRun() {
        ready = new DialogReady();
        ready.readyDialog(this, SportData.getStatus());
        SportHomePageFragment.stop();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNet) {
                if (netType < 15) {
                    if (netType == 2) {
                        Silently.getDoorStatus();
                    } else if (netType == 7) {
                        Silently.systemUpdate();
                    } else if (netType == 12) {
                        Silently.appUpdate(MainActivity.this);
                    }
//                    else if (netType == 17) {
//                    }
                    netType++;
                }
            }
            handler.sendEmptyMessage(0);
            handler.postDelayed(this, 1000);
        }
    };
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            activity_tv_time_main.setText(NowDateTime.getInstance().getTimes("HH:mm"));
            currentWifiInfo = wifiManager.getConnectionInfo();
            setSigns(Math.abs(currentWifiInfo.getRssi()));
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
                case R.id.user_ll_cancellation://退出
                User.setUserId("-1");
                User.setUserWeight(0);
                MyApplication.getInstance().setDianji(true);
                setCurrentIndex(14);
                break;
            case R.id.activity_tv_home_main:
                if (!SportData.isReady()) {
                    activity_vp_main.setCurrentItem(0);
                }
                break;
            case R.id.activity_tv_app_main:
                activity_vp_main.setCurrentItem(1);
                break;
            case R.id.activity_tv_setting_main:
                if (!SportData.isReady()) {
                    activity_vp_main.setCurrentItem(2);
                }
                break;
            case R.id.activity_main_back:
                backPressed();
                break;
            case R.id.personal_tv_info_icon:
                setCurrentIndex(12);
                break;
            case R.id.sport_tv_record:
                setCurrentIndex(4);
                break;
            case R.id.close:
            case R.id.next:
                if (dialogs != null && dialogs.isShowing())
                    dialogs.dismiss();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                backPressed();
//            case KeyEvent.KEYCODE_VOLUME_DOWN://音量增大键响应撤销
//            case KeyEvent.KEYCODE_VOLUME_UP://音量减小键响应重做
                return true;
        }
        return false;
    }

    private void runningMode() {
        switch (SportData.getStatus()) {
            case SportData.INVERSE_RUN_STATUS:
                setMode(getString(R.string.inverted_mode));
                break;
            case SportData.SCENE_RUN_STATUS://实景
                setMode(getString(R.string.scene_mode));
                break;
            case SportData.PROGRAM_RUN_STATUS://程序
                setMode(getString(R.string.program_mode));
                break;
            case SportData.USER_RUN_STATUS://自定义
                setMode(getString(R.string.custom_mode));
                break;
            case SportData.HRC_RUN_STATUS://心率
                setMode(getString(R.string.heart_rate_mode));
                break;
            case SportData.NORMAL_RUN_STATUS: //手动
                setMode(getString(R.string.manual_mode));
                break;
            case SportData.END_STATUS://结束
                setMode(getString(R.string.stopping));
                break;
        }
    }

    private static MainToService mainToService;

    public void cuss() {
        if (activity_vp_main.getVisibility() == View.GONE) {
            if (currentFragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (currentIndex == 7
//                        ||currentIndex==8
                        )
                    transaction.hide(currentFragment);
                else
                    transaction.detach(currentFragment);
                transaction.commitAllowingStateLoss();
            }
            showHomePager();
        }
    }

    public void setSigns(int signs) {
        if (signs <= 25) {//由强到弱
            activity_iv_wifi_icon.setImageResource(R.mipmap.wifi4);
            isNet = true;
        } else if (signs > 25 && signs <= 50) {
            isNet = true;
            activity_iv_wifi_icon.setImageResource(R.mipmap.wifi3);
        } else if (signs > 50 && signs <= 75) {
            isNet = true;
            activity_iv_wifi_icon.setImageResource(R.mipmap.wifi2);
        } else if (signs > 75 && signs <= 100) {
            isNet = true;
            activity_iv_wifi_icon.setImageResource(R.mipmap.wifi1);
        } else {//未连接WiFi
            activity_iv_wifi_icon.setImageResource(R.mipmap.wifi0);
            isNet = false;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        activity_vp_main.setCurrentItem(savedInstanceState.getInt(CURRENTINEXD));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterHomeKeyReceiver(this);
        } catch (Exception e) {
            LogUtils.log("Home键解除注册");
        }
    }

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) throws Exception {
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) throws Exception {
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public void backPressed() {//返回键
        if (SportData.isSetup())
            SportData.setStatus(SportData.NORMAL_STATUS);
        if (SportData.isReady()) {
            if (RunningFragment.isShow()) {
            } else if (currentIndex != 7) {
                runningMode();
                setCurrentIndex(7);
            } else {
                RunningFragment.setIsShow(true);
                runningMode();
                mainToService.frameDownss();
                showSportMode();
            }
        } else if (currentIndex == 14) {

        } else if (isRegister && currentIndex == 10) {

        } else if ((currentIndex == 11 || currentIndex == 13) && activity_vp_main.getVisibility() == View.GONE) {
            setCurrentIndex(14);
        } else if (currentIndex == 9) setCurrentIndex(1);
        else cuss();
    }

    private class HomeWatcherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {//if (DialogAdjustVelocityGradient.isShowings())DialogAdjustVelocityGradient.dimss();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {// 监听Home键
                        if (MyApplication.getInstance().isDianji())
                            return;
                        SettingHomePageFragment.versi();
                        SportHomePageFragment.dismissDialog();
                        CustomModeFragment.dismisses();
                        ProgramModeFragment.dis();
                        InverseModeFragment.dises();
                        HRCFragment.dismis();
                        if (SportData.isRunning()) {
                            if (RunningFragment.isShow()) {
                            } else if (currentIndex != 7) {
                                runningMode();
                                setCurrentIndex(7);
                            } else {
                                RunningFragment.setIsShow(true);
                                runningMode();
                                mainToService.frameDownss();
                                showSportMode();
                            }
                        } else if (activity_vp_main.getVisibility() == View.VISIBLE) {
                            activity_vp_main.setCurrentItem(0);
                        } else if (activity_vp_main.getVisibility() == View.GONE) {
                            if (currentFragment != null) {
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                if (currentIndex == 7
//                                        ||currentIndex==8
                                        )
                                    transaction.hide(currentFragment);
                                else
                                    transaction.detach(currentFragment);
                                transaction.commitAllowingStateLoss();
                            }
                            showHomePager();
                        }
                    } else if (reason.equals("recentapps")) {
                        //监听多任务键
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SportData.isRunning()){
            if (activity_vp_main.getVisibility()==View.GONE&&currentIndex==7){
                RunningFragment.setIsShow(false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENTINEXD, activity_vp_main.getCurrentItem());//super.onSaveInstanceState(outState);
    }

    public int getVideo_index() {
        return video_index;
    }

    public void setMode(String mode) {//设置模式名
        activity_main_mode.setText(mode);
    }

    public void setVideo_index(int video_indexs) {
        video_index = video_indexs;
    }
}