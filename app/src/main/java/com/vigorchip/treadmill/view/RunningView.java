package com.vigorchip.treadmill.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 跑步中
 */
public class RunningView extends View {
    Paint mPaint;
    private int MAX;
    private int mDate;
//    private boolean isNegative;//是否减一度

    public RunningView(Context context) {
        super(context);
        init();
    }

    public RunningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setMax(int max) {
        MAX = max;
//        isNegative = isNegatives;
    }

    public void setDate(int date) {
        mDate = date;
        invalidate();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        //画白圆
        RectF rect = new RectF(20, 20, getWidth() - 20, getHeight() - 20);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(30);
//        if (isNegative)
//            canvas.drawArc(rect, -90, (mDate * 360 / MAX - 1) % 360, false, mPaint);
//        else
            canvas.drawArc(rect, -90, mDate * 360 / MAX % 360, false, mPaint);
//        canvas.drawArc(rect,-90,180,false,mPaint);
        //画粉圆
        mPaint.setColor(Color.MAGENTA);
        mPaint.setStrokeWidth(5);
        RectF rects = new RectF(40, 40, getWidth() - 40, getHeight() - 40);
//        if (isNegative)
//            canvas.drawArc(rects, -90, (mDate * 360 / MAX - 1) % 360, false, mPaint);
//        else
            canvas.drawArc(rects, -90, mDate * 360 / MAX % 360, false, mPaint);
//        canvas.drawArc(rects,-90,180,false,mPaint);
        //画三角
        Path path = new Path();
        /**
         * 圆点坐标：(x0,y0) 
         半径：r 
         角度：a0 
         则圆上任一点为：（x1,y1） 
         x1   =   x0   +   r   *   cos(ao   *   3.14   /180   ) 
         y1   =   y0   +   r   *   sin(ao   *   3.14   /180   ) 
         */
        mPaint.setStyle(Paint.Style.FILL);
//        if (isNegative) {
//            path.moveTo((float) (getWidth() / 2 + (getWidth() - 85) / 2 * Math.cos((mDate * 360 / MAX - 95 - 1) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 85) / 2 * Math.sin((mDate * 360 / MAX - 95 - 1) % 360 * 3.14 / 180)));
//            path.lineTo((float) (getWidth() / 2 + (getWidth() - 85) / 2 * Math.cos((mDate * 360 / MAX - 85 - 1) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 85) / 2 * Math.sin((mDate * 360 / MAX - 85 - 1) % 360 * 3.14 / 180)));
//            path.lineTo((float) (getWidth() / 2 + (getWidth() - 5) / 2 * Math.cos((mDate * 360 / MAX - 90 - 1) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 5) / 2 * Math.sin((mDate * 360 / MAX - 90 - 1) % 360 * 3.14 / 180)));
//        } else {
            path.moveTo((float) (getWidth() / 2 + (getWidth() - 85) / 2 * Math.cos((mDate * 360 / MAX - 95) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 85) / 2 * Math.sin((mDate * 360 / MAX - 95) % 360 * 3.14 / 180)));
            path.lineTo((float) (getWidth() / 2 + (getWidth() - 85) / 2 * Math.cos((mDate * 360 / MAX - 85) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 85) / 2 * Math.sin((mDate * 360 / MAX - 85) % 360 * 3.14 / 180)));
            path.lineTo((float) (getWidth() / 2 + (getWidth() - 5) / 2 * Math.cos((mDate * 360 / MAX - 90) % 360 * 3.14 / 180)), (float) (getHeight() / 2 + (getHeight() - 5) / 2 * Math.sin((mDate * 360 / MAX - 90) % 360 * 3.14 / 180)));
//        }
//        path.moveTo((float) (getWidth()/2+(getWidth()-85)/2*Math.cos(85*3.14/180)),(float)(getHeight()/2+(getHeight()-85)/2*Math.sin(85*3.14/180)));
//        path.lineTo((float) (getWidth()/2+(getWidth()-85)/2*Math.cos(95*3.14/180)),(float)(getHeight()/2+(getHeight()-85)/2*Math.sin(95*3.14/180)));
//        path.lineTo((float) (getWidth()/2+(getWidth()-5)/2*Math.cos(90*3.14/180)),(float)(getHeight()/2+(getHeight()-5)/2*Math.sin(90*3.14/180)));

        path.close();
        canvas.drawPath(path, mPaint);
    }
}
