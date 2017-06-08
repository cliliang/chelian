package com.ceyu.carsteward.user.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.user.router.UserRouter;
import com.ceyu.carsteward.user.router.UserUI;

/**
 * Created by chen on 15/6/11.
 */
public class AboutBangActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.about_bang));
        setContentView(R.layout.about_bang_activity_layout);
        final Context mContext = AboutBangActivity.this;
        findViewById(R.id.about_bang_suggest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRouter.getInstance(mContext).showActivity(UserUI.bangSuggest);
            }
        });
        findViewById(R.id.about_bang_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRouter.getInstance(mContext).showActivity(UserUI.bangConnect);
            }
        });
        findViewById(R.id.about_bang_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRouter.getInstance(mContext).showActivity(UserUI.bangService);
            }
        });
    }
}
