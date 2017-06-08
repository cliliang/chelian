package com.ceyu.carsteward.common.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ceyu.carsteward.R;

/**
 * SlideSwitch 仿iphone滑动开关组件，仿百度魔图滑动开关组件 组件分为三种状态：打开、关闭、正在滑动<br/>
 * 用到三张图， 使用方法：
 * <p/>
 * <pre>
 * SlideSwitch slideSwitch = new SlideSwitch(this);
 * slideSwitch.setOnSwitchChangedListener(onSwitchChangedListener);
 * linearLayout.addView(slideSwitch);
 * </pre>
 * <p/>
 * 注：也可以加载在xml里面使用
 */
@SuppressLint("DrawAllocation")
public class SlideSwitch extends View {
    public static final String TAG = "SlideSwitch";
    private static final int SWITCH_OFF = 0;// 关闭状态
    /**
     * 开关的状态（默认为关闭）
     */
    private int mSwitchStatus = SWITCH_OFF;
    private static final int SWITCH_ON = 1;// 打开状态
    private static final int SWITCH_SCROLING = 2;// 滚动状态
    /**
     * 开关所需要的三张图片
     */
    Bitmap mSwitch_off, mSwitch_on, mSwitch_thumb;
    /**
     * 不动部分图片的宽度
     */
    private int mBmpWidth = 0;
    /**
     * 不动部分图片的高度
     */
    private int mBmpHeight = 0;
    /**
     * 滚动部分的图片的宽度
     */
    private int mThumbWidth = 0;
    /**
     * 自己的画笔
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 默认起始点和终止点
     */
    private int mSrcX = 0, mDstX = 0;

    private boolean clickable = true;

    private Handler mHandler;

    private OnChangedListener listener;

    public SlideSwitch(Context context) {
        this(context, null);
    }

    public SlideSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }

    // 初始化三幅图片，自己要改的地方
    private void setBitmap() {
        Resources res = getResources();
        mSwitch_off = BitmapFactory.decodeResource(res, R.mipmap.switch_bg_gray);
        mSwitch_on = BitmapFactory.decodeResource(res, R.mipmap.switch_bg_green);
        mSwitch_thumb = BitmapFactory.decodeResource(res, R.mipmap.switch_button);
    }

    private void init() {
        setBitmap();
        mBmpWidth = mSwitch_on.getWidth();
        mBmpHeight = mSwitch_on.getHeight();
        mThumbWidth = mSwitch_thumb.getWidth();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (listener != null) {
                    listener.onChanged(SlideSwitch.this, SlideSwitch.this.getState(), true);
                }
            }
        };
    }

    /**
     * 对组件的适配
     */
    @Override
    public void setLayoutParams(LayoutParams params) {
        if (params.height < 0 || params.width < 0) {
            params.width = mBmpWidth;
            params.height = mBmpHeight;
        } else {
            mSwitch_off = PictureZoom(mSwitch_off, params.width, params.height);
            mSwitch_on = PictureZoom(mSwitch_on, params.width, params.height);
            mSwitch_thumb = PictureZoom(mSwitch_thumb, params.height, params.height);
            mBmpWidth = mSwitch_off.getWidth();
            mBmpHeight = mSwitch_off.getHeight();
            mThumbWidth = mSwitch_thumb.getWidth();
            params.width = mBmpWidth;
            params.height = mBmpHeight;
        }
        super.setLayoutParams(params);
    }

    /**
     * 将图片进行缩放
     *
     * @param bitmap    需要缩放的图片
     * @param newWidth  想要的图片的新的宽度
     * @param newHeight 想要的图片的新的高度
     */
    private Bitmap PictureZoom(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    /**
     * 设置开关的状态
     *
     * @param on 是否打开开关 打开为true 关闭为false
     */
    public void setStatus(boolean on) {
        mSwitchStatus = (on ? SWITCH_ON : SWITCH_OFF);
        invalidate();
        if (listener != null) {
            listener.onChanged(this, getState(), false);
        }
    }

    public void setClickable(boolean able) {
        this.clickable = able;
    }

    public boolean getState() {
        return (mSwitchStatus == SWITCH_ON);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickable) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    // 如果没有发生过滑动，就意味着这是一次单击过程
                    mSwitchStatus = Math.abs(mSwitchStatus - 1);
                    int xFrom = mThumbWidth / 2, xTo = mBmpWidth - mThumbWidth / 2;
                    if (mSwitchStatus == SWITCH_OFF) {
                        xFrom = mBmpWidth - mThumbWidth / 2;
                        xTo = mThumbWidth / 2;
                    }
                    AnimationTransRunnable runnable = new AnimationTransRunnable(xFrom, xTo, 1);
                    new Thread(runnable).start();
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置显示字体大小
        mPaint.setTextSize(mThumbWidth / 3);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        if (mSwitchStatus == SWITCH_OFF) {
            drawBitmap(canvas, null, null, mSwitch_off);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.translate(mThumbWidth, 0);
        } else if (mSwitchStatus == SWITCH_ON) {
            drawBitmap(canvas, null, null, mSwitch_on);
            int count = canvas.save();
            canvas.translate(mBmpWidth - mThumbWidth, 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.WHITE);
            canvas.restoreToCount(count);
        } else {
            // 正在滑动中
            mSwitchStatus = mDstX == mBmpWidth - mThumbWidth / 2 ? SWITCH_ON : SWITCH_OFF;
            drawBitmap(canvas, new Rect(0, 0, mDstX, mBmpHeight), new Rect(0, 0, (int) mDstX, mBmpHeight), mSwitch_on);
            mPaint.setColor(Color.WHITE);
            int count = canvas.save();
            canvas.translate(mDstX, 0);
            drawBitmap(canvas, new Rect(mDstX, 0, mBmpWidth, mBmpHeight), new Rect(0, 0, mBmpWidth - mDstX, mBmpHeight), mSwitch_off);
            canvas.restoreToCount(count);
            count = canvas.save();
            canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight);
            canvas.translate(mThumbWidth, 0);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.restoreToCount(count);
            count = canvas.save();
            canvas.translate(mDstX - mThumbWidth / 2, 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            canvas.restoreToCount(count);
        }

    }

    public void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap) {
        dst = (dst == null ? new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()) : dst);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    public interface OnChangedListener {
        public void onChanged(SlideSwitch view, boolean on, boolean fromUser);
    }

    /**
     * AnimationTransRunnable 做滑动动画所使用的线程
     */
    private class AnimationTransRunnable implements Runnable {
        private int srcX, dstX;
        private int duration;

        /**
         * 滑动动画
         *
         * @param srcX     滑动起始点
         * @param dstX     滑动终止点
         * @param duration 是否采用动画，1采用，0不采用
         */
        public AnimationTransRunnable(float srcX, float dstX, final int duration) {
            this.srcX = (int) srcX;
            this.dstX = (int) dstX;
            this.duration = duration;
        }

        @Override
        public void run() {
            final int patch = (dstX > srcX ? 5 : -5);
            if (duration == 0) {
                SlideSwitch.this.mSwitchStatus = SWITCH_SCROLING;
                // postInvalidate刷新界面
                SlideSwitch.this.postInvalidate();
            } else {
                int x = srcX + patch;
                while (Math.abs(x - dstX) > 5) {
                    mDstX = x;
                    SlideSwitch.this.mSwitchStatus = SWITCH_SCROLING;
                    SlideSwitch.this.postInvalidate();
                    x += patch;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mDstX = dstX;
                SlideSwitch.this.mSwitchStatus = mDstX > mSwitch_off.getWidth() / 2 ? SWITCH_ON : SWITCH_OFF;
                SlideSwitch.this.postInvalidate();
            }
            mHandler.obtainMessage().sendToTarget();
        }
    }

}