package com.brioal.brioallib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Brioal on 2016/5/18.
 */
public class CirclePoint extends ImageView {
    private int mColor;
    private Paint mPaint;
    private int radicus;
    private int mWidth;
    private int mHeight;

    public CirclePoint(Context context) {
        this(context, null);
        init();
    }

    public CirclePoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
            mHeight = heightSize;
        } else {
//            mWidth = Math.min(widthSize, heightSize);
//            mWidth = Math.min(mWidth, 200);
            mWidth = 200;
            radicus = mWidth / 2;
        }
        setMeasuredDimension(mWidth, mWidth);
    }


    private void init() {
        mColor = Color.YELLOW;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, radicus - 10, mPaint);
    }
}
