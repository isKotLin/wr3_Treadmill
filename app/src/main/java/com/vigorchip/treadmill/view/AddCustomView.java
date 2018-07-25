package com.vigorchip.treadmill.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.utils.DensityUtils;

/**
 * 添加自定义模式
 */
public class AddCustomView extends View {
    private Paint mPaint;
    private int height, width;
    private int everX
//            , everY
            ;
    private int[] dataRec;//数据
    private OnChartClickListener listener;
    private boolean isClick;
    private int index;
    private int pitch;
    float top = DensityUtils.px2dip(getContext(), 20);//控制上下距离

    private Bitmap bitmapframe,btnBitmap;

    public AddCustomView(Context context) {
        super(context);
        init();
    }

    public AddCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dataRec = new int[16];
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTextSize(20);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        bitmapframe = BitmapFactory.decodeResource(getResources(), R.mipmap.custom_number_frame, options);
        btnBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dian, options);
    }

    public void upDataRec(int[] data, boolean type) {
        for (int i = 0; i < data.length; i++) {
            dataRec[i] = data[i];
        }
        if (type)
            pitch = MyApplication.getInstance().MAXSPEED;
        else
            pitch = MyApplication.getInstance().SLOPES;
        this.invalidate();//更新视图
    }

    int rac = DensityUtils.px2dip(getContext(), 100);//控制高度

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight() - rac;
        if (dataRec != null) {
            everX = width / dataRec.length;
//            everY = height / pitch;
            for (int i = 0; i < dataRec.length; i++) {
                mPaint.setARGB(100, 10, 10, 10);
//                canvas.drawRect(everX / 3 + everX * i, top, 2 * everX / 3 + everX * i, pitch * height / pitch + top, mPaint);
                canvas.drawLine(everX / 2 + everX * i, top, everX / 2 + everX * i, pitch * height / pitch + top, mPaint);
                mPaint.setColor(getResources().getColor(R.color.heart_text));
//                canvas.drawRect(everX / 3 + everX * i, (pitch - dataRec[i]) * height / pitch+top, 2 * everX / 3 + everX * i, pitch * height / pitch+top, mPaint);
                canvas.drawLine(everX / 2 + everX * i, (pitch - dataRec[i]) * height / pitch + top, everX / 2 + everX * i, pitch * height / pitch + top, mPaint);
                canvas.drawBitmap(bitmapframe, everX * i + DensityUtils.px2dip(getContext(), 3), height + rac / 2, mPaint);
                if (dataRec[i]==0)
                canvas.drawBitmap(btnBitmap, everX / 2 + everX * i- btnBitmap.getWidth()/2, (pitch - dataRec[i]) * height / pitch+btnBitmap.getHeight(), mPaint);
                else
                canvas.drawBitmap(btnBitmap, everX / 2 + everX * i- btnBitmap.getWidth()/2, (pitch - dataRec[i]) * height / pitch+ DensityUtils.px2dip(getContext(), 3), mPaint);
                mPaint.setColor(Color.WHITE);
                canvas.drawText((pitch == MyApplication.getInstance().MAXSPEED ? (double) dataRec[i] / 10 : dataRec[i]) + "", everX / 2 + everX * i - DensityUtils.px2dip(getContext(), 20), height + rac / 2 + top, mPaint);
            }
        }
    }

    float showTop;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float thisX = event.getX();
        float thisY = event.getY();
        float bottom = pitch * height / pitch + top;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; dataRec != null && i < dataRec.length; i++) {
                    float leftx = everX * i;
                    float rightx = everX * (i + 1);
                    if (leftx < thisX && thisX < rightx && thisY >= top && thisY <= bottom) {
                        if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值
                            showTop = pitch - (thisY - top) * pitch / bottom;
                            if (i <= 15 && i >= 0) {
                                isClick = true;
                                listener.onClick(i, showTop);
                            }
                            index = i;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isClick) {
                    if (thisY >= 0 && thisY <= bottom + top) {
                        if (listener != null) { //判断是否设置监听,将点击的第几条柱子,点击柱子顶部的坐值
                            showTop = pitch - (thisY - top) * pitch / bottom;
                            if (index <= 15 && index >= 0)
                                listener.onClick(index, showTop);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isClick = false;
                index = -1;
                break;
        }
        return true;
    }

    public interface OnChartClickListener {//柱子点击时的监听接口

        void onClick(int num, float y);
    }

    public void setOnChartClickListener(OnChartClickListener listeners) {//设置柱子点击监听的方法
        listener = listeners;
    }
}
