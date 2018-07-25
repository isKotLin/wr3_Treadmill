package com.vigorchip.treadmill.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vigorchip.treadmill.module.SportData;

public class HomeViewPager extends ViewPager {
    private float startX;
    private float startY;

    public HomeViewPager(Context context) {
        super(context);
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (SportData.isReady())
            return false;
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (SportData.isReady())
            return false;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:// 发生down事件时,记录按下坐标
                startX = e.getX();
                startY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dX = (e.getX() - startX);
                float dY = (e.getY() - startY);
                if (Math.abs(dX) > Math.abs(dY)) {//左右滑动，自己处理
                    return true;
                } else {//上下滑动,给子控件处理
                    return false;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(e);
    }
}