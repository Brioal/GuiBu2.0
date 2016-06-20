package com.brioal.brioallib.view.bottomtablayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.brioal.brioallib.R;


/**
 * 点击波纹效果
 * Created by Brioal on 2016/5/9.
 */
public class RippleButton extends TextView {

    private Paint mPaint; //绘制波纹效果的画笔
    private int mStepSize;// 波纹增加的步长
    private int mStartSize; //波纹起始地大小
    private int mEndSize; //波纹的最大值
    private int mRadius; //当前的波纹半径
    private int mCenterX; //波纹的中心点
    private int mCenterY;
    private boolean isAnimation; //是否在动画中

    private int mColor = R.color.colorHalfTrans;

    public void setmColor(int mColor) {
        this.mColor = mColor;
        if (mPaint != null) {
            mPaint.setColor(mColor);
        }
    }

    public void cancel() {
        isAnimation = false;
    }

    public interface onBeforeClickListener {
        void onBeforeClicked(View view);
    }

    private onBeforeClickListener listener;

    public void setListener(onBeforeClickListener listener) {
        this.listener = listener;
    }

    public RippleButton(Context context) {
        this(context, null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(mColor));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mEndSize = Math.max(width, height) / 2; //获取半径最大值
        mCenterX = width / 2;
        mCenterY = height / 2;
        mStepSize = (mEndSize - mStartSize) / 20; //获取步进值
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null) {
                listener.onBeforeClicked(this);
            }
            mRadius = mStartSize;
            isAnimation = true;
            postInvalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isAnimation) { // 没有点击 ,执行默认绘制
            super.onDraw(canvas);
            return;
        }
        if (isAnimation && mRadius > mEndSize) { //绘制快要结束 , 响应点击事件
            isAnimation = false;
            mRadius = mStartSize;
            performClick(); //响应点击事件
            super.onDraw(canvas);
            return;
        }
        mRadius += mStepSize; //逐渐增加
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        super.onDraw(canvas);
        postInvalidate(); //再次绘制
    }
}
