package com.ceyu.carsteward.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.db.CheDBM;
import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.main.router.MainUI;
import com.ceyu.carsteward.main.router.MajorRouter;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.main.UserLoginActivity;
import com.ceyu.carsteward.user.router.UserUI;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/2.
 */
public class FlashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_activity_layout);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = findViewById(R.id.flash_activity_main);
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        view.setAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void redirectTo() {
//        if (AppConfig.Show_Guard){
//            if (isNewVersion()){
//                MajorRouter.getInstance(FlashActivity.this).showActivity(MainUI.GuardActivity);
//            }else {
//
//            }
//        }
        AppContext appContext = (AppContext) FlashActivity.this.getApplicationContext();
        CheDBM dbm = CheDBM.getInstance(this);
        User activeUse = dbm.queryUser();
        if (activeUse == null){
            MainRouter.getInstance(FlashActivity.this).showActivity(ModuleNames.User, UserUI.userLogin);
        }else {
            Bundle bundle = getIntent().getExtras();
            MajorRouter.getInstance(FlashActivity.this).showActivity(MainUI.MainActivity, bundle);
            appContext.setActiveUser(activeUse);
        }
        finish();
    }

    private boolean isNewVersion() {
        boolean result = false;
        AppConfig appConfig = AppConfig.getInstance(getBaseContext());
        if (appConfig.HOME_GUIDE_VERSION > appConfig.getVersionCode()) {
            result = true;
        }
        return result;
    }
}
