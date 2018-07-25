package com.vigorchip.treadmill.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.vigorchip.treadmill.utils.LogUtils;

public abstract class BaseActivity extends FragmentActivity {
    //    private Toast toast;
    public View layoutView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(layoutView);
        initView();
        initData();
//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    /**
     * 传布局id
     */
    protected abstract int getLayoutId();
    /**
     * 初始化view
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 吐司
     * @param msg
     */
//    public void showToast(String msg) {
//        if (TextUtils.isEmpty(msg)) {
//            return;
//        }
//        if (null == toast) {
//            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(msg);
//        }
//        toast.show();
//    }
//    public void showToast(int msg) {
//        if (null == toast) {
//            toast = Toast.makeText(this, getResources().getString(msg),
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(getResources().getString(msg));
//        }
//        toast.show();
//    }
    /**
     * Hide the toast, if any.
     */
//    private void hideToast() {
//        if (null != toast) {
//            toast.cancel();
//        }
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        hideToast();
//    }
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
//        return res;
//    }
}
