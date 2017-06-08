package com.ceyu.carsteward.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class IndicatorViewFrameLayout extends FrameLayout {
    private ImageView imageViewFront;

    private ImageView imageViewBack;

    public IndicatorViewFrameLayout(Context context) {
        super(context);
        imageViewFront = new ImageView(context);
        imageViewFront.setScaleType(ScaleType.CENTER);
        imageViewBack = new ImageView(context);
        imageViewBack.setScaleType(ScaleType.CENTER);
        addView(imageViewBack);
        addView(imageViewFront);
    }

    public IndicatorViewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorViewFrameLayout(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageView getImageViewFront() {
        return imageViewFront;
    }

    public ImageView getImageViewBack() {
        return imageViewBack;
    }
}
