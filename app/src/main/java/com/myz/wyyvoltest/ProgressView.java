package com.myz.wyyvoltest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author myz
 * @date 2019/2/21
 * desc:
 */
public class ProgressView extends View{

    private Context mContext;
    private Paint mPaint;
    /**
     * Progress color
     */
    private int mProgressColor = Color.BLUE;
    /**
     * Progress width
     */
    private float mWidth = 0;
    /**
     * Progress height
     */
    private float mHeight = 0;
    /**
     * 进度
     */
    private float mProgress = 0f;
    /**
     * 总时长
     */
    private long mTimes = 0L;

    private CountDownTimer timer;

    public ProgressView(Context context) {
        this(context,null,0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
        initPaint();
        initTimer();
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.ProgressView);
        mProgressColor = typedArray.getColor(R.styleable.ProgressView_ProgressColor,mProgressColor);
        mWidth = typedArray.getDimension(R.styleable.ProgressView_ProgressWidth,mWidth);
        mHeight = typedArray.getDimension(R.styleable.ProgressView_ProgressHeight,mHeight);
        mTimes = typedArray.getInteger(R.styleable.ProgressView_Times,0) * 1000;
        typedArray.recycle();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(mProgressColor);
    }

    private void initTimer(){
        timer = new MyCountDownTimer(mTimes,10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mWidth == 0){
            mWidth = getMeasuredWidth();
        }

        if(mHeight == 0){
            mHeight = getMeasuredHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.WHITE);
        canvas.drawLine(mWidth / 2,0,mWidth / 2,mHeight,mPaint);
        mPaint.setColor(mProgressColor);
        canvas.drawRect(mWidth / 2 - 3,0,mWidth / 2 + 3,mProgress / 100 * mHeight,mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(14);
        if(mProgress != 100){
            canvas.drawText(handleTime(mProgress / 100 * mTimes / 1000),mWidth / 2 - 50,mProgress / 100 * mHeight,mPaint);
        }
        canvas.drawText(handleTime(mTimes / 1000),mWidth / 2 + 15,mHeight,mPaint);
    }

    private class MyCountDownTimer extends CountDownTimer{

        private long millisInFuture;

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.millisInFuture  = millisInFuture;
        }

        @Override
        public void onTick(long l) {
            mProgress = (millisInFuture - l) * 100f / millisInFuture ;
            invalidate();
        }

        @Override
        public void onFinish() {
            mProgress = 100;
            invalidate();
        }
    }

    public void start(){
        timer.start();
    }
    public void stop(){
        timer.cancel();
    }

    public void reset(){
        mProgress = 0;
        timer.start();
    }


    private String handleTime(float times){
        int a = (int)times/60;
        int b = (int)times%60;
        String aStr;
        String bStr;
        if(a < 10){
            aStr = "0" + a;
        }else {
            aStr = "" + a;
        }

        if(b < 10){
            bStr = "0" + b;
        }else {
            bStr = "" + b;
        }

        return aStr + ":" + bStr;

    }
}
