package com.vigorchip.treadmill.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * 折线图
 */
public class LineCharView extends View {
    Paint paint;//画笔
    int width;//控件宽度
    int height;//控件高度
    int[] fixationData;
    int max, min;
//    PaintFlagsDrawFilter mSetfil;
    public LineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
//        mSetfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //用来对位图进行滤波处理  
        paint.setFilterBitmap(true);
        paint.setColor(Color.WHITE);
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

    public void updata(int[] data) {
        fixationData = data;
        max = 0;
        min = fixationData[0];
        for (int i = 0; i < fixationData.length; i++) {
            System.out.print(fixationData[i] + " ");
            if (fixationData[i] > max)   // 判断最大值
                max = fixationData[i];
            if (fixationData[i] < min)   // 判断最小值
                min = fixationData[i];
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {//画控件
        super.onDraw(canvas);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//        canvas.setDrawFilter(mSetfil);
         if (fixationData!=null&&fixationData.length > 0) {//数据是数组
            float wid = width / (fixationData.length - 2);//X方向的间隔距离
            float hei = height / ((max - min) / 10 + 2);//Y方向的间隔距离
            for (int i = 0; i < fixationData.length-1; i++) {
                canvas.drawLine(wid * i, height - hei * ((fixationData[i] - min) / 10 + 1), wid * (i + 1), height - hei * ((fixationData[i + 1] - min) / 10 + 1), paint);
            }
        }

    }
}
