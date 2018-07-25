package com.vigorchip.treadmill.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.SaveFixedDateUtils;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.utils.LanguageUtils;
import com.vigorchip.treadmill.utils.PublicUtils;
import com.vigorchip.treadmill.view.VerticalSeekBar;

import java.util.Locale;

/**
 * 设置
 */
public class SettingHomePageFragment extends BaseFragment implements View.OnClickListener {
    View view;
    LinearLayout setting_mll_wifi, setting_mll_instructions, setting_mll_version_info, setting_mll_restore, setting_mll_clean, setting_mll_language;
    VerticalSeekBar ringtone_vsb, lightness_vsb;
    ContentObserver mVoiceObserver;
    ImageView restore_iv_language;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_home_page, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myRegisterReceiver();
        isRingtone=false;
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);//获取媒体系统服务
        setting_mll_wifi = view.findViewById(R.id.setting_mll_wifi);
        setting_mll_instructions = view.findViewById(R.id.setting_mll_instructions);
        setting_mll_version_info = view.findViewById(R.id.setting_mll_version_info);
        setting_mll_restore = view.findViewById(R.id.setting_mll_restore);
        setting_mll_clean = view.findViewById(R.id.setting_mll_clean);
        setting_mll_language = view.findViewById(R.id.setting_mll_language);
        ringtone_vsb = view.findViewById(R.id.ringtone_vsb);
        lightness_vsb = view.findViewById(R.id.lightness_vsb);
        restore_iv_language = view.findViewById(R.id.restore_iv_language);
        setting_mll_wifi.setOnClickListener(this);
        setting_mll_instructions.setOnClickListener(this);
        setting_mll_version_info.setOnClickListener(this);
        setting_mll_restore.setOnClickListener(this);
        setting_mll_clean.setOnClickListener(this);
        setting_mll_language.setOnClickListener(this);
        lightness();
        ringtone(audioManager);
        if (getContext().getResources().getConfiguration().locale.getLanguage().equals("en")) {
            MyApplication.getInstance().setIsChangLanguage(true);
            restore_iv_language.setImageResource(R.mipmap.english);
        } else if (getContext().getResources().getConfiguration().locale.getLanguage().equals("zh")) {
            MyApplication.getInstance().setIsChangLanguage(true);
            restore_iv_language.setImageResource(R.mipmap.chinese);
        }
        isRingtone=true;
    }

    private void lightness() {
        lightness_vsb.setProgress(getScreenBrightness());
        setScreenMode(0);
        lightness_vsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                saveScreenBrightness(i);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int getScreenBrightness() {//获得当前屏幕亮度值  0--255
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    private void setScreenMode(int paramInt) {//设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
        try {
            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void saveScreenBrightness(int paramInt) {//设置当前屏幕亮度值  0--255
        try {
            Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
private boolean isRingtone;
    private void ringtone(AudioManager audioManager) {//音量
        ringtone_vsb.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//获取当前的媒体音量
        ringtone_vsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (isRingtone){
                    AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);//系统音量和媒体音量同时更新
                    //audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, arg1, AudioManager.STREAM_VOICE_CALL);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, AudioManager.STREAM_MUSIC);//  3 代表  AudioManager.STREAM_MUSIC
            }}
        });
        mVoiceObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                ringtone_vsb.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));//或者你也可以用媒体音量来监听改变，效果都是一样的。 ringtone_vsb.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        };
    }

    private MyVolumeReceiver mVolumeReceiver;

    private void myRegisterReceiver() {//注册当音量发生变化时接收的广播
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getContext().registerReceiver(mVolumeReceiver, filter);
    }

    private void myUnRegisterReceiver() {//解除注册
        if (mVolumeReceiver != null)
            getContext().unregisterReceiver(mVolumeReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        myUnRegisterReceiver();
    }

    private class MyVolumeReceiver extends BroadcastReceiver {//处理音量变化时的界面显示

        @Override
        public void onReceive(Context context, Intent intent) {//如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                ringtone_vsb.setProgress(currVolume);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_mll_wifi://wifi
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.setting_mll_instructions://操作指南
                ((MainActivity) getActivity()).setCurrentIndex(6);
                ((MainActivity) getActivity()).setMode(getString(R.string.instructions));
                break;
            case R.id.setting_mll_version_info://版本信息
                showVersionDialog();
                break;
            case R.id.setting_mll_restore://一键还原
                if (!SportData.isReady())
                    showRestoreDialog();
                break;
            case R.id.setting_mll_clean://一键清理
                try {
                    startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.qt.cleanmaster"));
                } catch (Exception e) {
                    showToast(getString(R.string.no_install));//e.printStackTrace();
                }
                break;
            case R.id.setting_mll_language://更换语言
                if (getContext().getResources().getConfiguration().locale.getLanguage().equals("en")) {
                    LanguageUtils.updateLanguage(Locale.SIMPLIFIED_CHINESE);
                } else if (getContext().getResources().getConfiguration().locale.getLanguage().equals("zh")) {
                    LanguageUtils.updateLanguage(Locale.ENGLISH);
                }
                break;
        }
    }

    private static AlertDialog version;

    private void showVersionDialog() {
        if (version != null && version.isShowing())
            return;
        version = new AlertDialog.Builder(getContext(),R.style.NoTitle).create();
        version.show();
        version.getWindow().setContentView(R.layout.dialog_version);
//        WindowManager.LayoutParams lp = version.getWindow().getAttributes();
//        lp.width = DensityUtils.px2dip(getContext(), 450);//定义宽度  
//        lp.height = DensityUtils.px2dip(getContext(), 400);//定义高度  
//        version.getWindow().setAttributes(lp);
        ((TextView) version.findViewById(R.id.dialog_tv_version)).setText(getString(R.string.version_number) + " " + PublicUtils.getVersionName(getContext()));
        ((TextView) version.findViewById(R.id.dialog_tv_music_version)).setText(getString(R.string.music_version_number) + " " + PublicUtils.getVersionName(getContext(), "com.vigorchip.WrMusic.wr2"));
        ((TextView) version.findViewById(R.id.dialog_tv_video_version)).setText(getString(R.string.video_version_number) + " " + PublicUtils.getVersionName(getContext(), "com.vigorchip.WrVideo.wr2"));
        ((TextView) version.findViewById(R.id.dialog_tv_date)).setText(getString(R.string.date) + " " + SaveFixedDateUtils.VERSION_TIME);
        ((TextView) version.findViewById(R.id.dialog_tv_type)).setText(getString(R.string.machine_type) + " " + SaveFixedDateUtils.TYPE);//version.findViewById(R.id.version_iv_close).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) {versi();}});
    }

    public static void versi() {
        if (version != null && version.isShowing())
            version.dismiss();
        if (restore != null && restore.isShowing())
            restore.dismiss();
    }

    private static AlertDialog restore;

    private void showRestoreDialog() {//Dialog restore=new AlertDialog.Builder(getContext()).create(); restore.show(); restore.getWindow().setContentView(R.layout.dialog_restore);
        if (restore != null && restore.isShowing())
            return;
        restore = new AlertDialog.Builder(getContext(),R.style.NoTitle).create();
        restore.show();
        restore.getWindow().setContentView(R.layout.dialog_restore);

//        WindowManager.LayoutParams lp = restore.getWindow().getAttributes();
//        lp.width = DensityUtils.px2dip(getContext(), 350);//定义宽度  
//        lp.height = DensityUtils.px2dip(getContext(), 250);//定义高度  
//        restore.getWindow().setAttributes(lp);
        restore.findViewById(R.id.restore_tv_affirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread("Reboot") {
                    @Override
                    public void run() {
                        try {
                            RecoverySystem.rebootWipeUserData(getContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        restore.findViewById(R.id.restore_tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versi();
            }
        });
    }
}