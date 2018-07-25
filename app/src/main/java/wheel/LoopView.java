package wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Weidongjian on 2015/8/18.
 */
public class LoopView extends TextView {

    public enum ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DAGGLE
    }

    Context context;

    Handler handler;
    private GestureDetector gestureDetector;
    OnItemSelectedListener onItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    Paint paintOuterText;
    Paint paintCenterText;
    Paint paintIndicator;

    List<String> items;

    int textSize;
    int maxTextWidth;
    int maxTextHeight;

    int colorGray;
    int colorBlack;
    int colorLightGray;

    float lineSpacingMultiplier;// 条目间距倍数
    boolean isLoop;//是否循环

    int firstLineY;// 第一条线Y坐标值
    int secondLineY;// 第二条线Y坐标值

    int totalScrollY;
    int initPosition;
    private int selectedItem;//选中的下标
    int preCurrentIndex;
    int change;

    int itemsVisible; // 显示几个条目

    int measuredHeight;//view高度
    int measuredWidth;//view宽度
    int paddingLeft = 0;
    int paddingRight = 0;

    int halfCircumference;// 半圆周长

    int radius;// 半径

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;
    int centerLinedia = 20;//修改线

    public LoopView(Context context) {
        super(context);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context);
    }

    private void initLoopView(Context context) {
        this.context = context;
        handler = new MessageHandler(this);
        gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        gestureDetector.setIsLongpressEnabled(false);

        lineSpacingMultiplier = 1.3F;
        isLoop = true;
        itemsVisible = 7;
        textSize = 0;
        colorGray = 0xffafafaf;
        colorBlack = 0xff313131;
        colorLightGray = 0xffc5c5c5;

        totalScrollY = 0;
        initPosition = -1;

        initPaints();

        setTextSizes(60F);
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(colorGray);
        paintOuterText.setAntiAlias(true);
//        paintOuterText.setTypeface(TreadApplication.getInstance().font);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(Color.RED);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(1.05F);
//        paintCenterText.setTypeface(TreadApplication.getInstance().font);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(colorLightGray);
        paintIndicator.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void remeasure() {
        if (items == null) {
            return;
        }
        measureTextWidthHeight();

        halfCircumference = (int) (maxTextHeight * lineSpacingMultiplier * (itemsVisible - 1));
        measuredHeight = (int) ((halfCircumference * 2) / Math.PI);
        radius = (int) (halfCircumference / Math.PI);
        int extraRightWidth = (int) (maxTextWidth * 0.05) + 1;
        if (paddingRight <= extraRightWidth) {
            paddingRight = extraRightWidth;
        }
        measuredWidth = 2 * maxTextWidth + paddingLeft + paddingRight;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < items.size(); i++) {
            String s1 = items.get(i);
            paintCenterText.getTextBounds(s1, 0, s1.length(), rect);
            int textWidth = rect.width();
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth;
            }
            paintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect); //星期
            int textHeight = rect.height();
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight;
            }
        }

    }

//    private void smoothScroll() {
//        int offset = (int) ((float) totalScrollY % (lineSpacingMultiplier * (float) maxTextHeight));
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new SmoothScrollTimerTask(this, offset, timer), 0L, 10L);
//    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

//    void smoothScroll() {
//        int offset = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
//        cancelFuture();
//        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, offset), 0, 10, TimeUnit.MILLISECONDS);
//    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        int velocityFling = 10;// 修改这个值可以改变滑行速度
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public final void setNotLoop() {
        isLoop = false;
    }

    public final void setTextSizes(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
//            if (size == 60)
//                textSize = getContext().getResources().getDimensionPixelOffset(R.dimen.my66dp);
//            else
//                textSize = getContext().getResources().getDimensionPixelOffset(R.dimen.my80dp);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        this.initPosition = initPosition;
        handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<String> items) {
        this.items = items;
        remeasure();
        invalidate();
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingRight = right;
    }

    public final int getSelectedItem() {
        return selectedItem;
    }
//
//    protected final void scrollBy(float velocityY) {
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
//    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }

        String as[] = new String[itemsVisible];
        change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
        preCurrentIndex = initPosition + change % items.size();
//        Log.i("test", (new StringBuilder("scrollY1=")).append(totalScrollY).toString());
//        Log.i("test", (new StringBuilder("change=")).append(change).toString());
//        Log.i("test", (new StringBuilder("lineSpacingMultiplier * maxTextHeight=")).append(lineSpacingMultiplier * maxTextHeight).toString());
//        Log.i("test", (new StringBuilder("preCurrentIndex=")).append(preCurrentIndex).toString());
        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        // 设置as数组中每个元素的值
        int k1 = 0;
        while (k1 < itemsVisible) {
            int l1 = preCurrentIndex - (itemsVisible / 2 - k1);
            if (isLoop) {
                if (l1 < 0) {
                    l1 = l1 + items.size();
                }
                if (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                as[k1] = items.get(l1);
            } else if (l1 < 0) {
                as[k1] = "";
            } else if (l1 > items.size() - 1) {
                as[k1] = "";
            } else {
                as[k1] = items.get(l1);
            }
            k1++;
        }
        int m1 = paddingLeft + measuredWidth / 4;
//        canvas.drawLine(0.0F, firstLineY + centerLinedia, measuredWidth + centerLinedia, firstLineY + centerLinedia, paintIndicator);
//        canvas.drawLine(0.0F, secondLineY, measuredWidth, secondLineY, paintIndicator);
        int j1 = 0;
        while (j1 < itemsVisible) {
            canvas.save();
            // L(弧长)=α（弧度）* r(半径) （弧度制）
            // 求弧度--> (L * π ) / (π * r)   (弧长X派/半圆周长)
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * j1 - j2) * Math.PI) / halfCircumference;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY + centerLinedia - translateY);
                    canvas.drawText(as[j1], m1, maxTextHeight, paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY + centerLinedia - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(as[j1], m1, maxTextHeight, paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(as[j1], m1, maxTextHeight, paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
// paintCenterText.getTextBounds(as[j1], 0, 1, rect);//  strwid = rect.width();//   paintCenterText.getTextBounds(MaxNum, 0, 1, Maxrect);//  maxWid=Maxrect.width();

                    canvas.drawText(as[j1], m1//  + maxWid - strwid
                            , maxTextHeight, paintCenterText);
//  Log.e("TAG", "--------------------strwid" + strwid);//  Log.e("TAG","--------------------maxwid"+maxWid);
                    selectedItem = items.indexOf(as[j1]);
                } else {
                    // 其他条目
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
//   paintOuterText.getTextBounds(as[j1], 0, 1, rect);// strwid = rect.width();//paintOuterText.getTextBounds(MaxNum, 0, 1, Maxrect);//  maxWid=Maxrect.width();
                    canvas.drawText(as[j1], m1//+maxWid-strwid
                            , maxTextHeight, paintOuterText);
                }
                canvas.restore();
            }
            j1++;
        }
    }
//public void setMax(String max){
//    MaxNum =max;
//}
//    String MaxNum;
//    Paint paintHead;
//    Paint pFont = new Paint();
//    Rect rect = new Rect();
//    Rect Maxrect = new Rect();
//int strwid;
//    int maxWid;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        remeasure();
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = gestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();

                totalScrollY = (int) (totalScrollY + dy);

                // 边界处理。
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);// 处理拖拽事件
                    } else {
                        smoothScroll(ACTION.CLICK);// 处理条目点击事件
                    }
                }
                break;
        }
        invalidate();
        return true;
    }
}
