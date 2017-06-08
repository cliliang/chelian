package com.ceyu.carsteward.engineer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ceyu.carsteward.R;

public class ChoiceEngineerWindow extends PopupWindow{

    public ChoiceEngineerWindow(Activity context){
        super(context, null, R.style.SharePopupWindow);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.simple_listview_layout, null);
//        设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
//        设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置背景后点返回键可以消失
        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popupAnimation);
    }
}
