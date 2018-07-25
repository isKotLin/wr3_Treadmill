package com.vigorchip.treadmill.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.vigorchip.treadmill.base.MyApplication;

public class TwoLineCharView extends View {
    Paint paint;//画笔
    int width;//控件宽度
    int height;//控件高度
    int[] speedData;
    int[] slopedData;
    int speedMax, speedMin, slopedMax, slopedMin;

    public TwoLineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //用来对位图进行滤波处理  
        paint.setFilterBitmap(true);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthSize;
        height = heightSize;
        setMeasuredDimension(widthSize, heightSize);
    }

    public void updata(int[] speedDatas, int[] slopedDatas) {
        speedData = speedDatas;
        slopedData = slopedDatas;
        speedMax = 0;
        speedMin = speedData[0];
        slopedMax = 0;
        slopedMin = slopedData[0];
        for (int i = 0; i < speedData.length; i++) {
            if (speedData[i] > speedMax)
                speedMax = speedData[i];
            if (speedData[i] < speedMin)
                speedMin = speedData[i];

            if (slopedData[i] > slopedMax)
                slopedMax = slopedData[i];
            if (slopedData[i] < slopedMin)
                slopedMin = slopedData[i];
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {//画控件
        super.onDraw(canvas);
        float wid;
        float hei;
        if (speedData != null && speedData.length > 0) {
            paint.setColor(Color.WHITE);
            wid = width / (speedData.length - 2);
            hei = height / ((speedMax - speedMin) / 10 + 2);
            for (int i = 0; i < speedData.length - 1; i++) {
                canvas.drawLine(wid * i, height - hei * ((speedData[i] - speedMin) / 10 + 1), wid * (i + 1), height - hei * ((speedData[i + 1] - speedMin) / 10 + 1), paint);
            }
        }
        if (MyApplication.getInstance().SLOPES != 0 && slopedData != null && slopedData.length > 0) {
            paint.setColor(Color.BLUE);
            wid = width / (slopedData.length - 2);
            hei = height / (slopedMax - slopedMin+2);
            for (int i = 0; i < slopedData.length - 1; i++) {
                canvas.drawLine(wid * i, height - hei * (slopedData[i] - slopedMin), wid * (i + 1), height - hei * (slopedData[i + 1] - slopedMin), paint);
            }
        }
    }
}