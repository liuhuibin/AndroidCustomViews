package com.demo.huibin.myandroidapplication.com.demo.huibin.myandroidapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.demo.huibin.myandroidapplication.R;

/**
 * 一个可以将图片显示成圆角矩形并且在下面加上描述的自定义控件
 * Created by huibin on 15/4/12.
 */
public class YYKAdView extends View {

    private Bitmap mBitmap ; //src 真正要显示的广告图片
    private Bitmap mOutBitmap ;
    private Canvas mDstCanvas ;
    private Paint mPaint ;
    private Paint mTextPaint ;
    private boolean mIsDrawAdDesc ;
    private PorterDuffXfermode mXfermode ;
    private Rect mTextBounds ;

    private int mWidth ;
    private int mHeight ;


    private Drawable mAdDrawable ; //广告图片
    private String mAdDesc ; //广告描述
    private int mTextSize ; //广告描述文字的大小
    private int mTextColor ; //广告描述文字的颜色
    private int mRadius ; //广告图片圆角的大小

    private static final int DEFAULT_TEXT_SIZE = 16 ;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF ;
    private static final int DEFAULT_RADIUS = 25 ;

    public YYKAdView(Context context) {
        this(context, null);
    }

    public YYKAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YYKAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YYKAdView) ;
        try {
            mAdDrawable = a.getDrawable(R.styleable.YYKAdView_src);
            mAdDesc = a.getString(R.styleable.YYKAdView_text);
            mTextSize = a.getDimensionPixelSize(R.styleable.YYKAdView_textSize,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics()));
            mTextColor = a.getColor(R.styleable.YYKAdView_tetxColor, DEFAULT_TEXT_COLOR);
            mRadius = a.getDimensionPixelSize(R.styleable.YYKAdView_radius,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_RADIUS,getResources().getDisplayMetrics())) ;
        } finally {
            a.recycle();
        }
        if(!TextUtils.isEmpty(mAdDesc)) mIsDrawAdDesc = true ;

        if(mIsDrawAdDesc) {
            mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG) ;
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(mTextColor);
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
        mTextBounds = new Rect() ;
    }

    /**
     * 初始化members
     */
    private void init() {


        if(mAdDrawable == null) {
            mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.default_ad),mWidth,mHeight,true) ;
        } else {
            mBitmap = Bitmap.createScaledBitmap(drawableToBitmap(mAdDrawable),mWidth,mHeight,true) ;
        }

        mOutBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888) ;
        mDstCanvas = new Canvas(mOutBitmap) ;
        //dst
        mDstCanvas.drawRoundRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),mRadius,mRadius,mPaint);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN) ;
        mPaint.setXfermode(mXfermode) ;
        //src
        mDstCanvas.drawBitmap(mBitmap,0,0,mPaint);
        mPaint.setXfermode(null) ;

    }


    /**
     * 将Drawable对象转换成Bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(@NonNull Drawable drawable) {

        if(drawable == null) return null ;

        int width = drawable.getIntrinsicWidth() ;
        int height = drawable.getIntrinsicHeight() ;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) ;
        Canvas canvas = new Canvas(bitmap) ;
        drawable.setBounds(0,0,width,height);
        drawable.draw(canvas);

        return bitmap  ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec) ;
        mHeight = MeasureSpec.getSize(heightMeasureSpec) ;

        setMeasuredDimension(mWidth,mHeight);
        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mOutBitmap,0,0,null);
        if(mIsDrawAdDesc) {
            drawAdDesc(canvas,mAdDesc) ;
        }
    }

    private void drawAdDesc(Canvas canvas, String adDesc) {

        mTextPaint.getTextBounds(adDesc,0,adDesc.length(),mTextBounds);
        canvas.drawText(adDesc,mWidth/2-mTextBounds.width()/2,mHeight-mTextBounds.height()/2,mTextPaint);
    }
}
