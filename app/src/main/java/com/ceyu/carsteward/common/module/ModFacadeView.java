package com.ceyu.carsteward.common.module;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;

public class ModFacadeView extends LinearLayout {
    private LinearLayout contentView;

    public ModFacadeView(Context context) {
        super(context);
        init(context);
    }

    public ModFacadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.common_mod_facade_view, this, true);
        contentView = (LinearLayout) findViewById(R.id.facade_view_contents);
    }

    public void onResume() {
    }

    public void onDestroy(){

    }

    public void clearViews() {
        if (contentView != null) {
            contentView.removeAllViews();
        }
    }

    public void insertView(View view, LayoutParams lp) {
        if (view.getParent()!=null)
            ((ViewGroup) view.getParent()).removeView(view);
        if (lp != null) {
            contentView.addView(view, lp);
        } else {
            contentView.addView(view);
        }
    }
}
