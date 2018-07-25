package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.impl.RunningToService;
import com.vigorchip.treadmill.impl.TimeToRunning;
import com.vigorchip.treadmill.module.MyTime;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.view.RunningView;

/**
 * 运动时界面
 */
public class RunningFragment extends BaseFragment implements TimeToRunning, View.OnClickListener, View.OnTouchListener {
    private View view;
    private TextView running_tv_sloped, running_tv_heat, running_tv_mileage, running_tv_heart, running_tv_time, running_tv_speed, running_tv_stop, running_tv_pause, running_tv_media, running_tv_scene;
    private ImageView running_iv_sloped_plus, running_iv_speed_plus, running_iv_sloped_reduce, running_iv_speed_reduce, running_iv_pause;
    private RunningView running_rv_time, running_rv_speed;
    private LinearLayout sloped_ll_frame, sloped_ll_icon, running_ll_pause;

    public static void setIsShow(boolean isShows) {
        isShow = isShows;
    }

    private static boolean isShow;//是否显示跑步中界面

    public static boolean isShow() {
        return isShow;
    }

    private static RunningToService runningToService;

    public static void setRunningToService(RunningToService runningToServices) {
        runningToService = runningToServices;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_running, container, false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {//隐藏
            isShow = false;
            if (runningToService != null)
                runningToService.frameUp();
        } else {//显示
            if (runningToService != null)
                runningToService.frameDown();
            isShow = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        running_tv_sloped = view.findViewById(R.id.running_tv_sloped);
        running_tv_heat = view.findViewById(R.id.running_tv_heat);
        running_tv_mileage = view.findViewById(R.id.running_tv_mileage);
        running_tv_heart = view.findViewById(R.id.running_tv_heart);
        running_tv_time = view.findViewById(R.id.running_tv_time);
        running_tv_speed = view.findViewById(R.id.running_tv_speed);
        running_tv_stop = view.findViewById(R.id.running_tv_stop);
        running_tv_pause = view.findViewById(R.id.running_tv_pause);
        running_iv_pause = view.findViewById(R.id.running_iv_pause);
        running_ll_pause = view.findViewById(R.id.running_ll_pause);
        running_tv_scene = view.findViewById(R.id.running_tv_scene);
        running_tv_media = view.findViewById(R.id.running_tv_media);
        sloped_ll_frame = view.findViewById(R.id.sloped_ll_frame);
        sloped_ll_icon = view.findViewById(R.id.sloped_ll_icon);
        running_iv_sloped_plus = view.findViewById(R.id.running_iv_sloped_plus);
        running_iv_speed_plus = view.findViewById(R.id.running_iv_speed_plus);
        running_iv_sloped_reduce = view.findViewById(R.id.running_iv_sloped_reduce);
        running_iv_speed_reduce = view.findViewById(R.id.running_iv_speed_reduce);
        running_rv_time = view.findViewById(R.id.running_rv_time);
        running_rv_speed = view.findViewById(R.id.running_rv_speed);
        running_rv_time.setMax(3600);
        running_rv_speed.setMax(MyApplication.getInstance().MAXSPEED + 1);
        MyTime.setTimeToRunning(this);
        running_tv_scene.setOnClickListener(this);
        running_tv_media.setOnClickListener(this);
        running_tv_stop.setOnClickListener(this);
        running_ll_pause.setOnClickListener(this);
//        running_iv_sloped_plus.setOnClickListener(this);
//        running_iv_speed_plus.setOnClickListener(this);
//        running_iv_sloped_reduce.setOnClickListener(this);
//        running_iv_speed_reduce.setOnClickListener(this);


        running_iv_sloped_plus.setOnTouchListener(this);
        running_iv_speed_plus.setOnTouchListener(this);
        running_iv_sloped_reduce.setOnTouchListener(this);
        running_iv_speed_reduce.setOnTouchListener(this);

        if (MyApplication.getInstance().SLOPES == 0) {
            sloped_ll_frame.setVisibility(View.GONE);
            running_iv_sloped_plus.setVisibility(View.GONE);
            running_iv_sloped_reduce.setVisibility(View.GONE);
            sloped_ll_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public void setImage() {
        if (MyTime.getStartOrStop() == 3) {//
            running_iv_pause.setImageResource(R.mipmap.running_start);
            running_tv_pause.setText(getString(R.string.start));
            if (((MainActivity) getActivity()).getCurrentIndex() == 8) {
                VideoFragment.pausess();
            }
        } else {
            running_iv_pause.setImageResource(R.mipmap.running_pause);
            running_tv_pause.setText(getString(R.string.pause));
            if (((MainActivity) getActivity()).getCurrentIndex() == 8) {
                VideoFragment.startsss();
            }
        }
    }

    @Override
    public void setData() {
        running_tv_sloped.setText(MyTime.getmSloped() >= 10 ? MyTime.getmSloped() + "" : "0" + MyTime.getmSloped());

        running_tv_heat.setText(MyTime.getmHeat());

        running_tv_heart.setText(MyTime.getmHeart() >= 100 ? MyTime.getmHeart() + "" : MyTime.getmHeart() >= 10 ? "0" + MyTime.getmHeart() : "00" + MyTime.getmHeart());

        running_tv_mileage.setText(MyTime.getmMileage() + "");

        running_tv_time.setText(MyTime.positiveTiming());
        running_rv_time.setDate(MyTime.getTimeShow());

        running_tv_speed.setText((double) MyTime.getmSpeed() / 10 + "");
        running_rv_speed.setDate(MyTime.getmSpeed());
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        try {
////            if(getActivity() != null && getActivity().isFinishing());
//            LogUtils.log("onDetach:"+(getActivity() != null && getActivity().isFinishing())+"      "+(getActivity() != null)+"    "+(getActivity().isFinishing()));
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager ");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void setSpeed(int speed) {
        running_tv_speed.setText((double) speed / 10 + "");
        running_rv_speed.setDate(speed);
    }

    @Override
    public void setStopping() {
//        if (((MainActivity) getActivity()).getCurrentIndex() == 8){
//            ((MainActivity) getActivity()).backPressed();
//        }
        if (getActivity() != null && ((MainActivity) getActivity()).getCurrentIndex() == 7) {
            ((MainActivity) getActivity()).setMode(getString(R.string.stopping));
        }
    }

    @Override
    public void setSloped(int sloped) {
        running_tv_sloped.setText(sloped >= 10 ? sloped + "" : "0" + sloped);
    }

    @Override
    public void setHeart(int heart) {
        running_tv_heart.setText(heart >= 100 ? heart + "" : heart >= 10 ? "0" + heart : "00" + heart);
    }

    @Override
    public void setResult() {
        ((MainActivity) getActivity()).activity_vp_main.setCurrentItem(0);
        ((MainActivity) getActivity()).cuss();
        SportHomePageFragment.start();
        running_iv_pause.setImageResource(R.mipmap.running_pause);
        running_tv_pause.setText(getString(R.string.pause));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.running_tv_stop:
                MyTime.timeStop();
                break;
            case R.id.running_ll_pause:
                MyTime.timePause();
                break;
            case R.id.running_tv_media:
                ((MainActivity) getActivity()).activity_vp_main.setCurrentItem(1);
                ((MainActivity) getActivity()).showHomePager();
                if (runningToService != null)
                    runningToService.frameUp();
                isShow = false;
                break;
            case R.id.running_tv_scene:
                if (SportData.getStatus() != SportData.END_STATUS) {
                    ((MainActivity) getActivity()).setCurrentIndex(5);
                    ((MainActivity) getActivity()).setMode(getString(R.string.scene));
                }
                break;
        }
    }

    private void click(View view) {
        switch (view.getId()) {
            case R.id.running_iv_sloped_plus:
                MyTime.setmSloped(MyTime.getmSloped() + 1);
                break;
            case R.id.running_iv_speed_plus:
                MyTime.setmSpeed(MyTime.getmSpeed() + 1);
                break;
            case R.id.running_iv_sloped_reduce:
                MyTime.setmSloped(MyTime.getmSloped() - 1);
                break;
            case R.id.running_iv_speed_reduce:
                MyTime.setmSpeed(MyTime.getmSpeed() - 1);
                break;
        }
    }

    private boolean isChang;
    long lastTime;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isChang = false;
                lastTime = System.currentTimeMillis();
                click(view);
                break;//TODO 长按
            case MotionEvent.ACTION_MOVE:
                if (System.currentTimeMillis() - lastTime > 1000)
                    isChang = true;
                if (isChang && motionEvent.getY() > 0 && motionEvent.getY() <= view.getHeight() && motionEvent.getX() > 0 && motionEvent.getX() <= view.getWidth()) {
                    if (System.currentTimeMillis() - lastTime > 100) {
                        click(view);
                        lastTime = System.currentTimeMillis();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}