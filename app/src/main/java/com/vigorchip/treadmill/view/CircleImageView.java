package com.vigorchip.treadmill.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.vigorchip.treadmill.R;

/**
 * 圆形头像
 */
public class CircleImageView extends ImageView {
	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;
	private int mBorderWidth = 10;
	private int mBorderColor = Color.parseColor("#f2f2f2");
	private boolean useDefaultStyle = false;
	
	static {
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}
	
	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularImage);
		mBorderColor = a.getColor(R.styleable.CircularImage_border_color, mBorderColor);
		final int def = (int) (2*context.getResources().getDisplayMetrics().density + 0.5f);
		mBorderWidth = a.getDimensionPixelOffset(R.styleable.CircularImage_border_width, def);
		a.recycle();		
	}

	private void useDefaultStyle(boolean useDefaultStyle) {
		this.useDefaultStyle = useDefaultStyle;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(useDefaultStyle) {
			super.onDraw(canvas);
			return ;
		}
		final Drawable localDraw = getDrawable();
		if(localDraw == null) {
			return ;
		}
		if(localDraw instanceof NinePatchDrawable) {
			return ;
		}
		if (this.paint == null) {  
            final Paint localPaint = new Paint();  
            localPaint.setFilterBitmap(false);  
            localPaint.setAntiAlias(true);  
            localPaint.setXfermode(MASK_XFERMODE);  
            this.paint = localPaint;  
        }  
        final int width = getWidth();  
        final int height = getHeight();  
        int layer = canvas.saveLayer(0.0F, 0.0F, width, height, null, Canvas.ALL_SAVE_FLAG);
        localDraw.setBounds(0, 0, width, height);
        localDraw.draw(canvas);
        if ((this.mask == null) || (this.mask.isRecycled())) {  
            this.mask = createOvalBitmap(width, height);  
        }  
        canvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
        canvas.restoreToCount(layer);
        drawBorder(canvas, width, height); 
	}

	private void drawBorder(Canvas canvas, final int width, final int height) {
		if(mBorderWidth == 0) {
			return ;
		}
		final Paint mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);
		canvas.drawCircle(width/2, height/2, (width-mBorderWidth)/2, mBorderPaint);
		canvas = null;
	}
	
	public Bitmap createOvalBitmap(final int width, final int height) {
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;  
        Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);  
        Canvas localCanvas = new Canvas(localBitmap);  
        Paint localPaint = new Paint();  
        final int padding = (mBorderWidth - 3) > 0 ? mBorderWidth - 3 : 1;  
        RectF localRectF = new RectF(padding, padding, width - padding, height - padding);
        localCanvas.drawOval(localRectF, localPaint);  
		
		return localBitmap;
	}
	
}
