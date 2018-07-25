package com.vigorchip.treadmill.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.impl.ReadyToMain;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.service.MyService;
import com.vigorchip.treadmill.utils.NowDateTime;

/**
 * 准备跑步
 */
public class DialogReady implements Animation.AnimationListener {
    AlertDialog dialog;
    ImageView ready_iv;
    Context mContext;
    Animation animation;
    int executeCount;//动画执行了次数
    int Mmode;
    private static ReadyToMain readyToMain;
    public static void setReadyToMain(ReadyToMain readyToMains) {
        readyToMain = readyToMains;
    }
    public void readyDialog(Context context, int mode) {
        SportData.setStatus(SportData.FIVE_SECOND_STATUS);
        MyService.setStartTime(NowDateTime.getInstance().getTimes("yyyyMMdd"));
        mContext = context;
        Mmode = mode;
        dialog = new AlertDialog.Builder(context, R.style.Transparent).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_ready);
        dialog.setCancelable(false);
        ready_iv = (ImageView) dialog.findViewById(R.id.ready_iv);
        executeCount = 1;
        ready_iv.setImageResource(R.mipmap.ready3);
        animSet();
    }
    private void animSet() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.out_to_into);
        animation.setAnimationListener(this);
        ready_iv.setAnimation(animation);
    }
    private void animSets() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.into_to_out);
        animation.setAnimationListener(this);
        ready_iv.setAnimation(animation);
    }
    @Override
    public void onAnimationStart(Animation animation) {ready_iv.setVisibility(View.VISIBLE);
    }
    @Override
    public void onAnimationEnd(Animation animation) {
        ready_iv.setVisibility(View.GONE);
        if (executeCount % 2 == 0) {//从外而进
            animSet();
        } else {//从里而出
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            animSets();
        }if (executeCount == 2)
            ready_iv.setImageResource(R.mipmap.ready2);
        else if (executeCount == 4)
            ready_iv.setImageResource(R.mipmap.ready1);
        else if (executeCount == 6)//if (mContext.getResources().getConfiguration().locale.getLanguage().equals("zh")) ready_iv.setImageResource(R.mipmap.ready_start); else
            ready_iv.setImageResource(R.mipmap.ready0);
        else if (executeCount == 8) {
            readyToMain.showSport(Mmode);
            dialog.dismiss();
            return;
        }
        executeCount++;
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}