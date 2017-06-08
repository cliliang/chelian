package com.ceyu.carsteward.common.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.ceyu.carsteward.R;

public class RoundCornerImageView extends ImageView {

    private Path path = new Path();
    private RectF drawRect = new RectF();
    private int radius = 6;
    private int borderWith = 1;
    private int borderColor = Color.BLACK;
    private Paint paint = new Paint();
    private boolean isRound = false;

    public RoundCornerImageView(Context context) {
        super(context);
        paint.setAntiAlias(true);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        borderColor = a.getColor(R.styleable.RoundCornerImageView_borderColor, getResources().getColor(R.color.text_hint));
        borderWith = a.getDimensionPixelSize(R.styleable.RoundCornerImageView_borderWidth, 1);

        paint.setAntiAlias(true);
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWith);
        paint.setStyle(Paint.Style.STROKE);

        setImageResource(R.mipmap.default_img);
        setScaleType(ScaleType.CENTER_CROP);
    }

    public int getRadius() {
        return radius;
    }

    public void setRound(boolean isRound) {
        this.isRound = isRound;
        invalidate();
    }

    public int getBorderWith() {

        return borderWith;
    }

    public void setBorderWith(int borderWith) {
        paint.setStrokeWidth(borderWith);
        this.borderWith = borderWith;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        paint.setColor(borderColor);
        this.borderColor = borderColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawColor(getResources().getColor(R.color.white));
        drawRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom());
        path.reset();

        path.addRoundRect(drawRect, radius, radius, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        try {
            canvas.clipPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDraw(canvas);
        if (borderWith != 0) {
            canvas.drawRoundRect(drawRect, radius, radius, paint);
        }
        canvas.restore();
    }

}