package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.main.router.MainUI;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;

/**
 * Created by chen on 15/6/30.
 */
public class ReservePaySuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.reserve_pay_success);
        setContentView(R.layout.reserve_pay_success_activity_layout);
        TextView containView = (TextView) findViewById(R.id.order_pay_success_content);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int type = bundle.getInt(AppConfig.orderType);
            if (type == AppConfig.tuanOrder){
                containView.setText(getResources().getString(R.string.tuan_pay_success_info));
            }
        }
        final Context mcontext = ReservePaySuccessActivity.this;
        findViewById(R.id.reserve_pay_success_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MaintainRouter.getInstance(mcontext).showActivity(MaintainUI.getOrderList);
            }
        });
        findViewById(R.id.reserve_pay_success_home).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mcontext).showActivity(ModuleNames.Main, MainUI.MainActivity);
                finish();
            }
        });
    }
}
