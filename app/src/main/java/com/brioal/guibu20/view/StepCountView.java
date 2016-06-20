package com.brioal.guibu20.view;

/**
 * Created by Brioal on 2016/6/16.
 */

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by _SOLID
 * Date:2016/4/10
 * Time:10:35
 * <p/>
 * update:加入动画，动画由简书网友xsfelvis提供
 */
public class StepCountView extends View {

    private static String TAG = "QQHealthView";

    private int mWidth;//自定义View宽
    private int mHeight;//自定义View高
    private int mBackgroundCorner;//背景四角的弧度
    private int mArcCenterX;
    private int mArcCenterY;
    private RectF mArcRect;
    private Paint mBackgroundPaint;
    private Paint mArcPaint;//最上面弧线的画笔
    private Paint mTextPaint;
    private Paint mDashLinePaint;//虚线的画笔
    private Paint mBarPaint;//竖条的画笔
    private float mRatio;
    private Context mContext;
    private int mDefaultThemeColor;//主题色
    private int mDefaultUpBackgroundColor;//上层默认的背景色
    private int mThemeColor;
    private int mUpBackgroundColor;
    private float mArcWidth;
    private float mBarWidth;
    private int mMaxStep;
    private int mAverageStep;
    private int mTotalSteps;
    private int step = 25;
    private float percent = 0.5f;
    private Paint mAvatarPaint;
    public StepCountView(Context context) {
        this(context, null);
    }

    public StepCountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private int stepCount ; //当前总步数
    private String mCurrentTime ; //当前时间


    public void setData(int stepCount, String mCurrentTime ) {
        this.stepCount = stepCount;
        this.mCurrentTime = mCurrentTime;
        invalidate();
    }
    private void init() {
        stepCount = 10000;
        mCurrentTime = "00:00";
        //下面这句是关闭硬件加速，防止某些4.0的设备虚线显示为实线的问题
        //可以在AndroidManifest.xml时的Application标签加上android:hardwareAccelerated=”false”,
        // 这样整件应用都关闭了硬件加速，虚线可以正常显示，但是，关闭硬件加速对性能有些影响，
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //自定义View的宽高比例
        mRatio = 450.f / 525.f;
        //初始化一些默认的参数
        mBackgroundCorner = 20;
        mDefaultThemeColor = Color.parseColor("#2EC3FD");
        mDefaultUpBackgroundColor = Color.WHITE;
        mThemeColor = mDefaultThemeColor;
        mUpBackgroundColor = mDefaultUpBackgroundColor;
        calculateSteps();
        //背景画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mThemeColor);
        //圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setColor(mThemeColor);//画笔颜色
        mArcPaint.setAntiAlias(true);//抗锯齿
        mArcPaint.setStyle(Paint.Style.STROKE);//空心
        mArcPaint.setDither(true);//防抖动
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);//在画笔的连接处是圆滑的
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);//在画笔的起始处是圆滑的
        mArcPaint.setPathEffect(new CornerPathEffect(10));//画笔效果
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        //虚线画笔
        mDashLinePaint = new Paint();
        mDashLinePaint.setAntiAlias(true);
        mDashLinePaint.setColor(Color.parseColor("#C1C1C1"));
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{8, 4}, 0));//画虚线
        //竖条画笔
        mBarPaint = new Paint();
        mBarPaint.setColor(mThemeColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        //头像画笔
        mAvatarPaint = new Paint();
        mAvatarPaint.setAntiAlias(true);

        //加入动画
        AnimatorSet animatorSet = new AnimatorSet();

        //步数的动画
        ValueAnimator stepAnimator = ValueAnimator.ofInt(0, 6);
        stepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                step = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
//        stepAnimator.setDuration(1000);
//        stepAnimator.start();

        //圆环动画
        ValueAnimator percentAnimator = ValueAnimator.ofFloat(0, 1);
        percentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        // percentAnimator.setDuration(1000);
        // percentAnimator.start();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(stepAnimator, percentAnimator);
        animatorSet.start();


    }

    public void setThemeColor(int color) {
        mThemeColor = color;
        mBackgroundPaint.setColor(mThemeColor);
        mArcPaint.setColor(mThemeColor);
        mBarPaint.setColor(mThemeColor);
        invalidate();
    }

    //将原始图片转化为圆形图片
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r;
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        return backgroundBmp;
    }

    //计算步数
    private void calculateSteps() {
        mTotalSteps = 0;
        mMaxStep = 0;
        mAverageStep = 0;
    }

    //绘制最下层背景
    private void drawBelowBackground(int left, int top, int right, int bottom, int radius, Canvas canvas, Paint paint) {
        Path path = new Path();

        path.moveTo(left, top);

        path.lineTo(right - radius, top);
        path.quadTo(right, top, right, top + radius);

        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);

        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);

        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);

        canvas.drawPath(path, paint);
    }

    //绘制上层背景
    private void drawUpBackground(int left, int top, int right, int bottom, int radius, Canvas canvas, Paint paint) {
        Path path = new Path();

        path.moveTo(left, top);

        path.lineTo(right - radius, top);
        path.quadTo(right, top, right, top + radius);

        path.lineTo(right, bottom);

        path.lineTo(left, bottom);

        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);

        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultWidth = Integer.MAX_VALUE;
        int width;
        int height;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //  int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //  int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = defaultWidth;
        }
        int defaultHeight = (int) (width * 1.f / mRatio);
        height = defaultHeight;
        setMeasuredDimension(width, height);
        Log.i(TAG, "width:" + width + "| height:" + height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mArcCenterX = (int) (mWidth / 2.f);
        mArcCenterY = (int) (160.f / 385 * mHeight);
        mArcRect = new RectF();
        mArcRect.left = mArcCenterX - 125.f / 310 * mWidth;
        mArcRect.top = mArcCenterY - 125.f / 320 * mHeight;
        mArcRect.right = mArcCenterX + 125.f / 310 * mWidth;
        mArcRect.bottom = mArcCenterY + 125.f / 385.f * mHeight;

        mArcWidth = 20.f / 310 * mWidth;
        mBarWidth = 16.f / 310 * mWidth;

        //画笔的宽度一定要在这里设置才能自适应
        mArcPaint.setStrokeWidth(mArcWidth);
        mBarPaint.setStrokeWidth(mBarWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float xPos;
        float yPos;
        //1.绘制最下层背景
        mBackgroundPaint.setColor(mThemeColor);
        //2.绘制上面的背景
        mBackgroundPaint.setColor(mUpBackgroundColor);
        drawUpBackground(0, 0, mWidth, mWidth, mBackgroundCorner, canvas, mBackgroundPaint);
        //3.绘制圆弧
        canvas.drawArc(mArcRect, 120, 300 * percent, false, mArcPaint);
        //4.绘制圆弧里面的文字
        xPos = mArcCenterX;
        yPos = (int) (mArcCenterY - 40.f / 475.f * mHeight);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(15.f / 380.f * mWidth);
        mTextPaint.setColor(Color.parseColor("#C1C1C1"));
        canvas.drawText("截至"+mCurrentTime+"分已走", xPos, yPos, mTextPaint);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(60.f /380.f * mWidth);
        mTextPaint.setColor(mThemeColor);
        canvas.drawText(stepCount + "", mArcCenterX, mArcCenterY+20, mTextPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RectF rectF = new RectF();
        rectF.top = mWidth;
        rectF.left = 380.f / 450.f * mWidth;
        rectF.right = mWidth;
        rectF.bottom = mHeight;
        if (rectF.contains(event.getX(), event.getY())) {//当前点击的坐标在右下角的范围内
            //在这里可以做点击事件的监听
            Snackbar.make(this, "Click", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

}
