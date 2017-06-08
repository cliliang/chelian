package com.ceyu.carsteward.common.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;

import java.util.ArrayList;

public class IndicatorView extends LinearLayout {
    public static final int OFFSET_LEFT = -1;

    // private AllAppSpace mAllAppSpace;
    public static final int OFFSET_RIGHT = 1;
    public boolean isDragFlag = false;
    Drawable background = null;
    Drawable forwordground = null;
    ArrayList<IndicatorViewFrameLayout> childList = new ArrayList<IndicatorViewFrameLayout>();
    private Context mContext;
    private int ITEM_WIDTH;
    private int ITEM_HEIGHT;
    private int leftMargin;
    private int rightMargin;
    private Drawable mNormalPic = null;
    private int mCurrentNum = -1;
    private int mScreenNum = -1;
    private Animation oldChildAnimationScale = null;
    private Animation newChildAnimationScale = null;

    public IndicatorView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
    }

    public void init() {
        initResources();
        ITEM_WIDTH = (int) mNormalPic.getIntrinsicWidth();
        ITEM_HEIGHT = (int) mNormalPic.getIntrinsicHeight();
        leftMargin = (int) mContext.getResources().getDimension(
                R.dimen.indicator_view_margin);
    }

    private void recreate() {
        removeAllViews();
        childList.clear();
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lp = new LayoutParams(
                ITEM_WIDTH, ITEM_HEIGHT);
        // marginLeft=((getWidth())/2)-(((mScreenNum*ITEM_WIDTH)/2)+(((mScreenNum-1)*ITEM_WIDTH)/2));
        int y = 0;
        int t = 0;
        if (this.mScreenNum > 15) {
            lp.leftMargin = leftMargin;
            lp.rightMargin = rightMargin;
            if (mScreenNum - mCurrentNum > 3) {
                if (mCurrentNum > 3) {
                    y = mCurrentNum - 3;
                    t = mCurrentNum + 4;
                } else {
                    y = 0;
                    t = 7;
                }
            } else {
                t = mScreenNum;
                y = mScreenNum - 7;
            }
        } else {
            // lp.leftMargin =totalLength/mScreenNum;
            // lp.rightMargin = totalLength/mScreenNum;
            y = 0;
            t = mScreenNum;
        }
        lp.leftMargin = leftMargin;

        for (int i = 0; i < mScreenNum; i++) {
            IndicatorViewFrameLayout child = new IndicatorViewFrameLayout(
                    mContext);
            if (i == mCurrentNum) {
                child.getImageViewBack().setImageDrawable(getNormalDrawable());
                child.getImageViewFront()
                        .setImageDrawable(getCurrentDrawable());
            } else {
                child.getImageViewFront().setImageDrawable(getNormalDrawable());
            }
            child.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }

            });
            childList.add(child);
            if (i >= y && i < t) {
                addView(child, lp);
            }
        }
        if (mScreenNum > 7 && mScreenNum - mCurrentNum > 3) {

        }
    }

    private void updateState() {
        for (int i = 0; i < mScreenNum; i++) {
            IndicatorViewFrameLayout child = childList.get(i);
            if (i == mCurrentNum) {
                child.getImageViewBack().setImageDrawable(getNormalDrawable());
                child.getImageViewFront()
                        .setImageDrawable(getCurrentDrawable());
            } else {
                child.getImageViewFront().setImageDrawable(getNormalDrawable());
            }
        }
        if (mScreenNum > 7 && mScreenNum - mCurrentNum > 3) {

        }

    }

    private int initScreen(int mScreenNum, int mCurrentNum) {
        mCurrentNum %= mScreenNum;

        mCurrentNum = mCurrentNum < 0 ? mScreenNum - 1 : mCurrentNum;

        this.mCurrentNum = mCurrentNum;
        this.mScreenNum = mScreenNum;

        if (mScreenNum != childList.size()) {
            this.recreate();
        } else {
            this.updateState();
        }

        return mCurrentNum;
    }

    public void updateScreen(int gotoIndex, int totalNum) {
        int oldScreen = mCurrentNum < 0 ? 0 : mCurrentNum;
        gotoIndex = initScreen(totalNum, gotoIndex);

        if (childList == null || childList.size() == 0
                || gotoIndex > mScreenNum || oldScreen == mCurrentNum
                || gotoIndex > (childList.size() - 1)
                || oldScreen > (childList.size() - 1)) {
            return;
        }
        IndicatorViewFrameLayout newChild = childList.get(gotoIndex);
        if (newChild == null) {
            return;
        }
        IndicatorViewFrameLayout oldChild = childList.get(oldScreen);
        if (oldChild == null) {
            return;
        }
        initAnimation();
        newChild.getImageViewBack().setImageDrawable(getNormalDrawable());
        newChild.getImageViewFront().setImageDrawable(getCurrentDrawable());
        newChild.getImageViewFront().startAnimation(newChildAnimationScale);

        oldChild.getImageViewBack().setImageDrawable(getNormalDrawable());
        oldChild.getImageViewFront().setImageDrawable(getCurrentDrawable());
        oldChild.getImageViewFront().startAnimation(oldChildAnimationScale);

        invalidate();
        requestLayout();
    }

    public void initAnimation() {
        if (newChildAnimationScale == null) {
            newChildAnimationScale = AnimationUtils.loadAnimation(mContext,
                    R.anim.indicator_focus);
        }
        if (oldChildAnimationScale == null) {
            oldChildAnimationScale = AnimationUtils.loadAnimation(mContext,
                    R.anim.indicator_unfocus);
            oldChildAnimationScale.setFillAfter(true);
        }
    }

    private void initResources() {
        mNormalPic = getNormalDrawable();
    }

    private Drawable getNormalDrawable() {
        return getResources().getDrawable(R.mipmap.dot_unselected);
    }

    private Drawable getCurrentDrawable() {
        return getResources().getDrawable(R.mipmap.dot_selected);
    }

    public String toString() {
        return "TotalNum: " + mScreenNum + "   currenScreen: " + mCurrentNum;
    }

    @Override
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        final int count = getChildCount();

        setDrawingCacheEnabled(enabled);
        // Update the drawing caches
        buildDrawingCache(true);

        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            view.setDrawingCacheEnabled(enabled);
            // Update the drawing caches
            view.buildDrawingCache(true);
        }
    }

    @Override
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        super.setChildrenDrawnWithCacheEnabled(enabled);
    }
}
