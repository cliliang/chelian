package com.ceyu.carsteward.common.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


public class BaseActivity extends AppCompatActivity {

    private TextView titleView;
    private LinearLayout rightLayout;
    public RequestQueue requestQueue;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        requestQueue = Volley.newRequestQueue(this);
        ActionBar actionBar = getSupportActionBar();
        progressDialog = ProgressDialog.getInstance();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.common_actionbar_background));
        View view = getLayoutInflater().inflate(R.layout.common_actionbar_layout, null);
        titleView = (TextView) view.findViewById(R.id.action_bar_title_id);
        rightLayout = (LinearLayout) view.findViewById(R.id.action_bar_right_layout);
        ImageView backImage = (ImageView) view.findViewById(R.id.action_bar_back_id);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        actionBar.setCustomView(view, lp);

        PushAgent.getInstance(this).onAppStart();
    }

    public void setTitle(int titleId){
        titleView.setText(getResources().getString(titleId));
    }

    @Override
    protected void onPause() {
        hideSoftInput();
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        if (requestQueue != null){
            requestQueue.cancelAll(this);
        }
        super.onDestroy();
    }

    public void dismissDialog(Activity activity){
        if (progressDialog.isShowing() && !activity.isFinishing()){
            progressDialog.dismiss();
        }
    }

    public ProgressDialog getProgressDialog(){
        if (progressDialog != null){
            return progressDialog;
        }else {
            return ProgressDialog.getInstance();
        }
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void onBackClick(){
        finish();
    }

    public void setRightTitle(String title, View.OnClickListener listener){
        if (rightLayout != null){
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.common_action_bar_text, null);
            if (textView != null){
                textView.setText(title);
                textView.setOnClickListener(listener);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                rightLayout.addView(textView, lp);
            }
        }
    }

    public void hideSoftInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
