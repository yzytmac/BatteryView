package com.example.yzy.webviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by YangZhenYu on 2016/9/6.
 */
public class BatteryView extends View {

    private Paint mTextPait;
    private Paint mBatteryPait;
    private Paint mPowerPaint;
    private float mBatteryStroke = 4.0f;
    private float raidus = 2.0f;

    /**
     * 电池参数
     *
     * @param context
     */
    private float mBatteryHeight = 30.0f;// 电池高度
    private float mBatteryWidth = 60.0f;// 电池的宽度
    private float mCapWidth;//电池盖的宽度
    private float mCapHeight;//电池盖的高度
    private float mPowerHeight;
    private float mPowerWidth;
    private float mPower = 0.5f;//默认电量

    /**
     * 矩形
     */
    private RectF mBatteryRectF;//电池
    private RectF mCapRectF;//电池盖
    private RectF mPowerRectF;//电量

    /**
     * 设置电量
     *
     * @param pPower
     */
    public void setPower(float pPower) {
        mPower = pPower;
        if (pPower < 0) {
            mPower = 0f;
        }
        if (pPower > 1f) {
            mPower = 1f;
        }
        invalidate();
    }

    /**
     * 设置电池宽高
     *
     * @param pBatteryWidth
     * @param pBatteryHeight
     */
    public void setWidthHeight(float pBatteryWidth, float pBatteryHeight) {
        mBatteryWidth = pBatteryWidth;
        mBatteryHeight = pBatteryHeight;
    }


    public BatteryView(Context context, float pBatteryWidth, float pBatteryHeight) {
        super(context);
        mBatteryWidth = pBatteryWidth;
        mBatteryHeight = pBatteryHeight;
        initSize();
        initView(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSize();
        initView(context);
    }

    private void initView(Context context) {
        mTextPait = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPait.setTextSize(sp2px(context, 8));
        mTextPait.setColor(0xff303030);
        mTextPait.setAntiAlias(true);

        /**
         * 设置电池画笔
         */
        mBatteryPait = new Paint();
        mBatteryPait.setColor(0xff29b473);
        mBatteryPait.setStrokeWidth(mBatteryStroke);
        mBatteryPait.setStyle(Paint.Style.STROKE);
        mBatteryPait.setAntiAlias(true);
        /**
         * 电量画笔
         */
        mPowerPaint = new Paint();
        mPowerPaint.setStyle(Paint.Style.FILL);
        mPowerPaint.setStrokeWidth(mBatteryStroke);
        mPowerPaint.setAntiAlias(true);
        /**
         * 设置电池矩形
         * 偏移一个弧度单位，防止线粗细不一
         */
        mBatteryRectF = new RectF(raidus, raidus, mBatteryWidth + raidus, mBatteryHeight + raidus);

        /**
         * 设置电池盖矩形
         */
        mCapRectF = new RectF(mBatteryWidth + raidus, (mBatteryHeight - mCapHeight + raidus) / 2,
                mCapWidth + mBatteryWidth + raidus, (mBatteryHeight + mCapHeight + raidus) / 2);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            mBatteryWidth = 50;
        } else {
            mBatteryWidth = MeasureSpec.getSize(widthMeasureSpec) - raidus * 2 - mCapWidth;
        }

        if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            mBatteryHeight = 25;
        } else {
            mBatteryHeight = MeasureSpec.getSize(heightMeasureSpec) - raidus * 2;
        }
        initSize();
        initView(getContext());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initSize() {
        mCapHeight = mBatteryHeight / 2;
        mCapWidth = mBatteryWidth / 20;
        mPowerHeight = mBatteryHeight - mBatteryStroke;
        mPowerWidth = mBatteryWidth - 2 * mBatteryStroke;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPower > 0.21f) {
            //电量大于20%显示绿色
            mBatteryPait.setColor(0xff29b473);
            mPowerPaint.setColor(0xff29b473);
            mTextPait.setColor(0xff303030);
        } else if (mPower < 0.21f) {
            //电量少于20%显示红色
            mBatteryPait.setColor(0xfff90f0f);
            mPowerPaint.setColor(0xfff90f0f);
            mTextPait.setColor(0xfff90f0f);
        }

        /**
         * 设置电量的矩形
         */
        mPowerRectF = new RectF(mBatteryStroke + raidus, mBatteryStroke + raidus, mPower * mPowerWidth + mBatteryStroke + raidus, mPowerHeight + raidus);
        canvas.drawRoundRect(mBatteryRectF, raidus, raidus, mBatteryPait);
        canvas.drawRoundRect(mCapRectF, raidus, raidus, mBatteryPait);
        canvas.drawRect(mPowerRectF, mPowerPaint);

        String text = (int) (mPower * 100) + "%";
        float stringWidth = mTextPait.measureText(text);
        float x = (getWidth() - stringWidth) / 2;
        float y = mBatteryHeight / 2 + mBatteryHeight / 4;
        canvas.drawText(text, x, y, mTextPait);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */

    public static int sp2px(Context context, float spVal) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,

                spVal, context.getResources().getDisplayMetrics());
    }
}
