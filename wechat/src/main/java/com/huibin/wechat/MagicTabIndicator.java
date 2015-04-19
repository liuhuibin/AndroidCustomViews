package com.huibin.wechat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 微信画面底部的Tab的指示器
 * Created by huibin on 15/4/18.
 */
public class MagicTabIndicator extends View {

    private Paint mPaint ;
    private Bitmap mBitmap ;
    private Canvas mCanvas ;
    private Paint mTextPaint ;
    private Xfermode mXfermode ;

    private Rect mTextBounds ;
    private Rect mIconRect ;

    private Bitmap mIcon ;
    private String mText ;
    private int mTextSize ;
    private int mColor ;

    private float mAlpha ;

    private final float DEFAULT_TEXT_SIZE = 12 ;//默认12sp
    private final int DEFAULT_COLOR = 0xFF45C01A ;

    public static final String INSTANCE_STATE = "instance_state" ;
    public static final String INSTANCE_ALPHA = "instance_alpha" ;


    public MagicTabIndicator(Context context) {
        this(context, null);
    }

    public MagicTabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicTabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MagicTabIndicator) ;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) a.getDrawable(R.styleable.MagicTabIndicator_tabIcon);
        mIcon = bitmapDrawable.getBitmap() ;
        mText = a.getString(R.styleable.MagicTabIndicator_text) ;
        mTextSize = (int)a.getDimensionPixelSize(R.styleable.MagicTabIndicator_textSize,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,DEFAULT_TEXT_SIZE,getResources().getDisplayMetrics())) ;
        mColor = a.getColor(R.styleable.MagicTabIndicator_tabColor,DEFAULT_COLOR) ;
        a.recycle();

        init() ;

    }

    private void init() {
        mTextBounds = new Rect() ;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0Xff555555);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBounds);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mPaint.setColor(mColor);
        mPaint.setDither(true);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN) ;



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得icon的边长
        int iconSideLen = Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),//
                                  getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBounds.height()) ;
        int left = getMeasuredWidth()/2 - iconSideLen/2 ;
        int top = getMeasuredHeight()/2 - (iconSideLen+mTextBounds.height())/2 ;

        mIconRect = new Rect(left,top,left+iconSideLen,top+iconSideLen) ;


    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mIcon,null,mIconRect,null);
        int alpha = (int)Math.ceil(255*mAlpha) ;
        drawIcon(alpha);
        drawSourceText(canvas, alpha) ;
        drawTargetText(canvas, alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setAlpha(255-alpha);
        mTextPaint.setColor(Color.BLACK);
        canvas.drawText(mText,getMeasuredWidth()/2-mTextBounds.width()/2,mIconRect.bottom+mTextBounds.height(),mTextPaint);

    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setAlpha(0);
        mTextPaint.setColor(mColor);
        canvas.drawText(mText,getMeasuredWidth()/2-mTextBounds.width()/2,mIconRect.bottom+mTextBounds.height(),mTextPaint);

    }

    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle() ;
        bundle.putParcelable(INSTANCE_STATE,super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_ALPHA,mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            mAlpha = ((Bundle) state).getFloat(INSTANCE_ALPHA) ;
            super.onRestoreInstanceState(((Bundle) state).getParcelable(INSTANCE_STATE));
            return;
        }

        super.onRestoreInstanceState(state);

    }

    /**
     * 画图标
     * @param alpha
     */
    private void drawIcon(int alpha) {

        mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888) ;
        mPaint.setAlpha(alpha);
        mCanvas = new Canvas(mBitmap) ;
        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(mXfermode) ;
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIcon,null,mIconRect,mPaint);
        mPaint.setXfermode(null) ;

    }

    public void setIconAlpha(float alpha) {
        mAlpha = alpha ;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }

}
