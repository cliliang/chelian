package com.ceyu.carsteward.self.views;

import android.content.Context;
import android.util.AttributeSet;

import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.views.HorizontalListView;

/**
 * Created by chen on 15/9/11.
 */
public class SelfHorizontalListView extends HorizontalListView {
    private int itemWidth;
    public SelfHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (itemWidth == 0){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (width > 0){
                itemWidth = width / 2 - (int)Utils.dip2px(getContext(), 8);
            }
        }
    }

    public int getItemWidth(){
        return itemWidth;
    }
}
