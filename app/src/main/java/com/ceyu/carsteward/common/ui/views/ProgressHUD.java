package com.ceyu.carsteward.common.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.ceyu.carsteward.R;
public class ProgressHUD extends Dialog {

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressHUD get(Context context) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressDialog);
        dialog.setContentView(R.layout.common_progress_dialog_layout);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.common_progress_id);
        imageView.setImageResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
//        Animation anim = new RotateAnimation(0, 360,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        anim.setRepeatCount(Animation.INFINITE); // 设置INFINITE，对应值-1，代表重复次数为无穷次
//        anim.setDuration(1000); // 设置该动画的持续时间，毫秒单位
//        anim.setInterpolator(new LinearInterpolator()); // 设置一个插入器，或叫补间器，用于完成从动画的一个起始到结束中间的补间部分
//        imageView.startAnimation(anim);
        return dialog;
    }
}
