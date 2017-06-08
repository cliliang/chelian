package com.ceyu.carsteward.user.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.Config;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.db.CheDBM;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.BaiduLBS;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.main.router.MainUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.router.UserUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chen on 15/6/2.
 */
public class UserLoginActivity extends BaseActivity {

    private EditText numberView, codeView;
    private Button loginButton;
    private TextView codeButton;
    private Context mContext;
    private CheckBox checkBox;
    private Handler mHandler = new Handler();
    private int maxNumber = 60;
    private BaiduLBS mLBS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity_layout);
        setTitle(R.string.user_login);
        mContext = UserLoginActivity.this;
        numberView = (EditText) findViewById(R.id.user_login_phone_number_id);
        codeView = (EditText) findViewById(R.id.user_login_check_code_id);
        codeButton = (TextView) findViewById(R.id.user_login_get_code_button);
        loginButton = (Button) findViewById(R.id.user_login_button_id);
        loginButton.setClickable(false);
        checkBox = (CheckBox) findViewById(R.id.login_check_box);

        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = numberView.getText().toString().trim();
                if (!StringUtils.isEmpty(number)) {
                    sendPhoneCode();
                    codeButton.setEnabled(false);
                    mHandler.post(codeRunnable);
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    String code = codeView.getText().toString().trim();
                    if (!StringUtils.isEmpty(code)) {
                        userLogin();
                    }
                } else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.user_login_read_detail));
                }

            }
        });
        findViewById(R.id.user_use_app_deal).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.User, UserUI.bangService);
            }
        });
        mLBS = BaiduLBS.getInstanse(this);
    }

    Runnable codeRunnable = new Runnable() {
        @Override
        public void run() {
            maxNumber--;
            if (maxNumber > 0){
                codeButton.setText(String.format(Locale.US, getResources().getString(R.string.user_number_second), maxNumber));
                mHandler.postDelayed(codeRunnable, 1000);
            }else {
                maxNumber = 60;
                codeButton.setEnabled(true);
                codeButton.setText(getResources().getString(R.string.user_get_phone_code));
                mHandler.removeCallbacks(codeRunnable);
            }
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(codeRunnable);
        super.onDestroy();
    }

    private void sendPhoneCode() {
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        String number = numberView.getText().toString().trim();
        map.put("mobile", number);
        long current = System.currentTimeMillis() / 1000;
        map.put("code1", String.valueOf(current));
        String spellString = number + String.valueOf(current * 2 - 1);
        map.put("code2", StringUtils.MD5(spellString));
        CheJSONObjectRequest sendRequest = new CheJSONObjectRequest(UserURLs.sendPhoneCode, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dismissDialog(UserLoginActivity.this);
                if (jsonObject != null) {
                    Log.i("chen", "login response:" + jsonObject.toString());
                    String state = jsonObject.optString(ResponseCode.ResponseState);
                    if (state.equals("ok")) {
                        loginButton.setClickable(true);
                        UIHelper.ToastMessage(mContext, R.string.user_send_phone_code_success);
                    } else {
                        String errorInfo = jsonObject.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(errorInfo));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(UserLoginActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(sendRequest);
        requestQueue.start();
    }

    private void userLogin() {
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", numberView.getText().toString().trim());
        map.put("code", codeView.getText().toString().trim());
        map.put("set", getLoginSet());
        CheJSONObjectRequest loginRequest = new CheJSONObjectRequest(UserURLs.userLogin, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dismissDialog(UserLoginActivity.this);
                if (jsonObject != null) {
                    String state = jsonObject.optString(ResponseCode.ResponseState);
                    if (state.equals(ResponseCode.ResponseOK)) {
                        //用于切换用户时刷新首页车辆信息
                        AppConfig appConfig = AppConfig.getInstance(mContext);
                        appConfig.setRefreshCar(true);
                        User user = User.fromJsonObject(jsonObject);
                        user.setPhoneNumber(numberView.getText().toString().trim());
                        user.setActive(true);
                        AppContext appContext = (AppContext) mContext.getApplicationContext();
                        appContext.setActiveUser(user);
                        CheDBM cheDBM = CheDBM.getInstance(mContext);
                        cheDBM.insertUser(user);
                        MainRouter.getInstance(mContext).showActivity(ModuleNames.Main, MainUI.MainActivity);
                        finish();
                    } else {
                        String info = jsonObject.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(UserLoginActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(loginRequest);
        requestQueue.start();
    }

    private String getLoginSet() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utils.getPhoneInfo(this));
        builder.append(mLBS.getOnlyLocationSet());
        return builder.toString();
    }

    private SmsReceiver smsReceiver;
    @Override
    protected void onResume() {
        super.onResume();
        //注册接收短信的广播接收器
        smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销接收短信的广播接收器
        this.unregisterReceiver(smsReceiver);
    }

    /*
    接收短信读取验证码
    短信格式： 【帮帮养车】您的验证码是：8405。如非本人操作，请忽略本短信
     */
    private class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            try {
                Object[] smsObj = (Object[]) bundle.get("pdus");
                for (Object aSmsObj : smsObj) {
                    SmsMessage msg = SmsMessage.createFromPdu((byte[]) aSmsObj);
                    String codeMsg = msg.getDisplayMessageBody();
                    //FIXME
                    //包含“帮帮养车”这四个字  //包含“验证码”这三个字
                    if (codeMsg.contains( getResources().getString(R.string.app_name)) && codeMsg.contains( getResources().getString(R.string.verification_code))) {
                        //Matcher ma = Pattern.compile("\\d{4}").matcher(msg.getDisplayMessageBody());
                        Matcher ma = Pattern.compile("(?<![0-9])([0-9]{4})(?![0-9])").matcher(msg.getDisplayMessageBody()); //前后断言，只包含四个数字
                        if (ma.find()) {
                            String verCode = ma.group(0);
                            codeView.setText(verCode);  //自动装入验证码
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                if (Config.isDevelopeMode()) e.printStackTrace();
            }
        }
    }

}
