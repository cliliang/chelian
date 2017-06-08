package com.ceyu.carsteward.tribe.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.tribe.router.TribeRouter;
import com.ceyu.carsteward.tribe.router.TribeUI;

/**
 * Created by chen on 15/8/27.
 */
public class TribeActionBar extends LinearLayout {
    private TextView dotView;
    private Context mContext;
    private View rootView;
    private PopupWindow popupWindow;
    private PublishClickListener listener;
    public TribeActionBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init(){
        rootView = LayoutInflater.from(mContext).inflate(R.layout.tribe_action_bar_layout, this);
        ImageView moreView = (ImageView) findViewById(R.id.tribe_action_more_image);
        moreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopup();
                TribeRouter.getInstance(mContext).showActivity(TribeUI.tribeMine);
            }
        });
        ImageView editView = (ImageView) findViewById(R.id.tribe_action_edit);
        editView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onPublishClickedListener();
                }
            }
        });
        dotView = (TextView) findViewById(R.id.tribe_action_more_dot);
    }

    public void showRedDot(boolean show){
        dotView.setVisibility(show ? VISIBLE : GONE);
    }

    public interface PublishClickListener{
        void onPublishClickedListener();
    }

    public void setOnPublishClickedListener(PublishClickListener l){
        this.listener = l;
    }

    private void showPopup(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_tribe_more_popup_layout, null);
        view.findViewById(R.id.tribe_popup_edit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        view.findViewById(R.id.tribe_popup_comment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        view.findViewById(R.id.tribe_popup_message_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow = new PopupWindow(view, (int)Utils.dip2px(mContext, 130), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.common_bottom_line_layout));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(rootView, (int) Utils.dip2px(mContext, -18), 0);

    }
}
