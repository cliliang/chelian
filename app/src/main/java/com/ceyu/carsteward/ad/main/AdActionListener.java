package com.ceyu.carsteward.ad.main;

import android.view.View;

/**
 * Created by Administrator on 2015/7/2.
 */
public class AdActionListener implements View.OnClickListener{

    private String action;
    private OnAdClickListener listener;

    public AdActionListener(String action, OnAdClickListener listener){
        this.action = action;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(this.listener != null) this.listener.onAdClick(this.action);
    }

    public interface OnAdClickListener{
        void onAdClick(String action);
    }
}
