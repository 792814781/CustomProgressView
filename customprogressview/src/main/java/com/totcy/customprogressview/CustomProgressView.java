package com.totcy.customprogressview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.math.BigDecimal;

/**
 * Description 自定义多种进度条
 * Author: tu
 * Date: 2016-10-17
 * Time: 16:01
 */
public class CustomProgressView extends View {

    private float dotX, dotY;//圆点xy
    private int viewWidth;//view的宽度
    private int viewHigth;//view的高度
    private Paint mPaint, mPaintArc;//画笔  66 206 161

    private float radius = 100f;//最大的半径
    private float ringWidth = 60f;//进度条的宽度
    private float curProgressSize = 120f;
    private float minCircleWidth = 10;//里面最小圈的宽度

    private int color1 = Color.argb(255, 66, 206, 161);//背景圆颜色 深
    private int color2 = Color.argb(255, 124, 221, 197);//背景圆颜色 深
    private int color3 = Color.argb(255, 157, 231, 212);//背景圆颜色 深
    private int colorGray = Color.argb(255, 219, 247, 239);//
    private int colorWhite = Color.argb(255, 255, 255, 255);//
    private int[] colors = new int[]{colorGray,color1};//

    private int curProgress = 0;//0 ~ 99进度 当前进度
    private RectF mRectF;

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomProgressView, defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i =   0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomProgressView_ProgressTextSize) {
                curProgressSize = a.getDimension(attr,
                        curProgressSize);

            } else if (attr == R.styleable.CustomProgressView_RingWidth) {
                ringWidth = a.getDimension(attr, ringWidth);

            } else if (attr == R.styleable.CustomProgressView_MinCircleWidth) {
                minCircleWidth = a.getDimension(attr, minCircleWidth);

            } else {
            }
        }
        a.recycle();
        init(context);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressView(Context context) {
        this(context, null);
    }

    /**
     * 初始化画笔
     */
    private void init(Context context) {

        //初始化坐标画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);//
        mPaint.setAntiAlias(true);
        mPaintArc.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        int width;

        //宽度测量
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getMeasuredWidth();
        }
        dotX = width / 2;
        viewWidth = width;
        //高度测量
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getMeasuredHeight();
        }
        viewHigth = height;
        dotY = height / 2;
        radius = dotX - (getPaddingLeft() + getPaddingRight()) / 2;
        float tempR = radius - ringWidth / 2;
        mRectF = new RectF(dotX - tempR, dotY - tempR, dotX + tempR, dotY + tempR);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //从外圈绘制开始
        mPaintArc.setShader(null);
        float startRadius = radius - ringWidth / 2;
        //先绘制外圈环背景 1
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setStrokeWidth(ringWidth);
        mPaintArc.setColor(color3);
        canvas.drawCircle(dotX, dotY, startRadius, mPaintArc);
        //2
        mPaintArc.setColor(color2);
        canvas.drawCircle(dotX, dotY, startRadius - ringWidth, mPaintArc);
        //3
        mPaintArc.setColor(color3);
        canvas.drawCircle(dotX, dotY, startRadius - 2 * ringWidth, mPaintArc);
        //3_1
        float width4 = minCircleWidth/2;
        mPaintArc.setStrokeWidth(width4);
        mPaintArc.setColor(colorGray);
        canvas.drawCircle(dotX, dotY, startRadius - 2 * ringWidth, mPaintArc);
        //4
        mPaintArc.setStrokeWidth(width4);
        mPaintArc.setColor(colorGray);
        canvas.drawCircle(dotX, dotY, radius - (3 * ringWidth + width4/2), mPaintArc);
        //5
        mPaintArc.setStrokeWidth(minCircleWidth);
        mPaintArc.setColor(color1);
        // radius - (3 * ringWidth + minCircleWidth/2 + width4)
        canvas.drawCircle(dotX, dotY, radius - (3 * ringWidth + minCircleWidth), mPaintArc);
        //绘制 2 -3 环之间的圆周点
        mPaintArc.setStrokeWidth(0);
        mPaintArc.setColor(colorWhite);
        mPaintArc.setStyle(Paint.Style.FILL);
        float radiusPoint = radius - 2 * ringWidth;
        for (int angle = -90; angle <= 270; angle += 45) {
            float xY[] = caculCoordinate(angle, radiusPoint);
            if (xY != null) {
                canvas.drawCircle(xY[0], xY[1], minCircleWidth, mPaintArc);
            }
        }
        //小圆点进度
        int angle = new BigDecimal(curProgress).multiply(new BigDecimal(360))
                .divide(new BigDecimal(99),0, BigDecimal.ROUND_HALF_UP).intValue() - 90;
        float xYProgress[] = caculCoordinate(angle, startRadius - 2 * ringWidth);
        canvas.drawCircle(xYProgress[0], xYProgress[1], minCircleWidth*2, mPaintArc);
        //中心进度
        mPaint.setTextSize(curProgressSize);
        mPaint.setColor(color1);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float curProgressY = dotY + mPaint.measureText("0")/2;
        canvas.drawText(curProgress + "", dotX - 10, curProgressY, mPaint);
        //单位
        float unitX = dotX + mPaint.measureText(curProgress+"")/2 - 5;
        mPaint.setTextSize(curProgressSize/2);
        mPaint.setColor(Color.parseColor("#c6c6c6"));
        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("分", unitX, curProgressY, mPaint);

        //绘制外圈渐变进度
        mPaintArc.setStrokeWidth(ringWidth);
        mPaintArc.setStyle(Paint.Style.STROKE);
        //mPaintArc.setColor(color1);
        SweepGradient gradient = new SweepGradient(dotX,dotY,colorGray,color1);
        //按照圆心旋转
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, dotX, dotY);
        gradient.setLocalMatrix(matrix);
        mPaintArc.setShader(gradient);
        int endAngle = new BigDecimal(curProgress).multiply(new BigDecimal(360))
                .divide(new BigDecimal(99),0, BigDecimal.ROUND_HALF_UP).intValue();
        canvas.drawArc(mRectF, -90, endAngle, false, mPaintArc);

    }

    /**
     * 根据圆心角 计算圆周上的坐标
     *
     * @param angle
     * @param radius 半径
     * @return xY[0] startX; xY[1] startY; xY[2]
     */
    private float[] caculCoordinate(int angle, float radius) {
        //angle >180     angle = angle -180
        float xY[] = new float[2];
        //角度处理
        int tempAngle = Math.abs(angle);

        if (270 >= tempAngle && tempAngle >= 180) {
            tempAngle = tempAngle - 180;
            xY[0] = dotX - getCoordinateX(tempAngle, radius);
            xY[1] = dotY - getCoordinateY(tempAngle, radius);

        } else if (180 > tempAngle && tempAngle > 90) {
            tempAngle = 180 - tempAngle;
            xY[0] = dotX - getCoordinateX(tempAngle, radius);
            xY[1] = dotY + getCoordinateY(tempAngle, radius);

        } else if (90 >= tempAngle && tempAngle >= 0) {
            xY[0] = dotX + getCoordinateX(tempAngle, radius);
            xY[1] = angle < 0 ? dotY - getCoordinateY(tempAngle, radius) : dotY + getCoordinateY(tempAngle, radius);

        }
        return xY;
    }

    /**
     * 获取圆周上y值相对值
     *
     * @param tempAngle
     * @param radius    算开始坐标是传半径，算结束坐标时传刻度线的长度
     * @return
     */
    private float getCoordinateY(int tempAngle, float radius) {

        //利用正弦函数算出y坐标
        return (float) (Math.sin(tempAngle * Math.PI / 180) * radius);
    }

    /**
     * 获取圆周上X值相对值
     *
     * @param tempAngle
     * @return
     */
    private float getCoordinateX(int tempAngle, float radius) {

        //利用余弦函数算出y坐标
        return (float) (Math.cos(tempAngle * Math.PI / 180) * radius);
    }

    public void setCurProgress(int curProgress) {
        if(curProgress > 99)
            return;
        this.curProgress = curProgress;
        postInvalidate();
    }
    public void setCurProgressWithAnim(int curProgress){
        ObjectAnimator o = ObjectAnimator.ofInt(this, "curProgress", 0, curProgress);
        o.setDuration(1500);
        o.setInterpolator(new DecelerateInterpolator());
        o.start();
    }
}
