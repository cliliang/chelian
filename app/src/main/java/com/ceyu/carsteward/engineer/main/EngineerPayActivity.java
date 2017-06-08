package com.ceyu.carsteward.engineer.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.CircleHeadImageView;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.engineer.bean.EngineerBean;
import com.ceyu.carsteward.engineer.bean.EngineerPay;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/9.
 */
public class EngineerPayActivity extends BaseActivity {

    private TextView nameView, levelView, brandView, moneyView, bangMoneyView, otherMomeyView;
    private CheckBox bangPayIcon, wechatPayIcon, alipayIcon;
    private CircleHeadImageView headImageView;
    private Button payButton;
    private int payType = -1; // 1表示三选其一  2表示必用券，二选其一
    private ScrollView mScrollView;
    private ProgressDialog progressDialog;
    private CheImageLoader imageLoader;
    private EngineerPay engineerPay;
    private EngineerBean engineerBean;
    private Context mContext;
    private String engineerToken = "";

    private User activeUser;
    private boolean bangEqualtMoney = false;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private String TAG = "chen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_pay_activity_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            engineerToken = bundle.getString(EngineerEvent.ENGINEER_TOKEN);
            if (StringUtils.isEmpty(engineerToken)) {
                finish();
            }
        } else {
            finish();
        }
        initView();
        setTitle(getResources().getString(R.string.engineer_buy_service));
        mContext = EngineerPayActivity.this;
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        progressDialog = ProgressDialog.getInstance();
        imageLoader = new CheImageLoader(requestQueue, mContext);
        msgApi.registerApp(WXUtils.APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScrollView.setVisibility(View.INVISIBLE);
        getPayInfo(engineerToken);
    }

    private void initView(){
        mScrollView = (ScrollView) findViewById(R.id.engineer_pay_scroll_id);
        headImageView = (CircleHeadImageView) findViewById(R.id.engineer_pay_head);
        nameView = (TextView) findViewById(R.id.engineer_pay_name);
        levelView = (TextView) findViewById(R.id.engineer_pay_level);
        brandView = (TextView) findViewById(R.id.engineer_pay_brand);
        moneyView = (TextView) findViewById(R.id.engineer_pay_dollar);
        bangMoneyView = (TextView) findViewById(R.id.engineer_pay_bang_money);
        otherMomeyView = (TextView) findViewById(R.id.engineer_pay_other_money);
        bangPayIcon = (CheckBox) findViewById(R.id.engineer_pay_bang_icon);
        wechatPayIcon = (CheckBox) findViewById(R.id.engineer_pay_wechat_icon);
        alipayIcon = (CheckBox) findViewById(R.id.engineer_pay_alipay_icon);
        payButton = (Button) findViewById(R.id.engineer_pay_button);
        payButton.setEnabled(false);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType == 1) {
                    if (bangPayIcon.isChecked()) {
                        if (bangEqualtMoney) {
                            payByPacket();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(EngineerEvent.ENGINEER_TOKEN, engineerToken);
                            EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerPacketPay, bundle);
                        }
                    } else if (wechatPayIcon.isChecked()) {
                        getOrderId(false);
                    } else {
                        UIHelper.ToastMessage(mContext, "alipay:" + engineerBean.get_money());
                    }
                } else if (payType == 2) {
                    float bang = engineerPay.getBangMoney();
                    float release = engineerBean.get_money() - bang;
                    if (wechatPayIcon.isChecked()) {
                        getOrderId(true);
                    } else {
                        UIHelper.ToastMessage(mContext, "alipay:" + release + "--bang:" + bang);
                    }
                }
            }
        });
    }

    private void setViewData(EngineerPay engineerPay){
        mScrollView.setVisibility(View.VISIBLE);
        engineerBean = engineerPay.getEngineerBean();
        if (engineerBean != null){
            if (!StringUtils.isEmpty(engineerBean.get_pic())){
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(headImageView, R.mipmap.icon_my, R.mipmap.icon_my);
                imageLoader.get(engineerBean.get_pic(), imageListener);
            }
            String index = engineerBean.get_name();
            if (!StringUtils.isEmpty(index)){
                nameView.setText(index);
            }
            index = engineerBean.get_level();
            if (!StringUtils.isEmpty(index)){
                levelView.setText(index);
            }
            index = engineerBean.get_model();
            if (!StringUtils.isEmpty(index)){
                brandView.setText(index);
            }
            float money = engineerBean.get_money();
            moneyView.setText(String.format(Locale.US, "%.0f", money));
            float bangMoney = engineerPay.getBangMoney();
            bangMoneyView.setText(String.format(Locale.US, getResources().getString(R.string.engineer_service_money_string), bangMoney));


            if (engineerPay.getBangMoney() > 0){
                bangPayIcon.setChecked(true);
            }else {
                bangPayIcon.setClickable(false);
            }

            if (engineerPay.getBangMoney() >= engineerBean.get_money()){
                if (engineerPay.getBangMoney() == engineerBean.get_money()){
                    bangEqualtMoney = true;
                }
                //帮帮券，微信，支付宝三选一
                payButton.setEnabled(true);
                payType = 1;
                bangPayIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            wechatPayIcon.setChecked(false);
                            alipayIcon.setChecked(false);

                            bangPayIcon.setClickable(false);
                            wechatPayIcon.setClickable(true);
                            alipayIcon.setClickable(true);
                        }
                    }
                });
                wechatPayIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            bangPayIcon.setChecked(false);
                            alipayIcon.setChecked(false);

                            wechatPayIcon.setClickable(false);
                            bangPayIcon.setClickable(true);
                            alipayIcon.setClickable(true);
                        }
                    }
                });
                alipayIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            bangPayIcon.setChecked(false);
                            wechatPayIcon.setChecked(false);

                            alipayIcon.setClickable(false);
                            bangPayIcon.setClickable(true);
                            wechatPayIcon.setClickable(true);
                        }

                    }
                });
            }else {
                //帮帮券必用，微信，支付宝二选一
                payButton.setEnabled(true);
                bangPayIcon.setClickable(false);
                wechatPayIcon.setChecked(true);
                wechatPayIcon.setClickable(false);
                payType = 2;
                wechatPayIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            alipayIcon.setChecked(false);

                            wechatPayIcon.setClickable(false);
                            alipayIcon.setClickable(true);
                        }
                    }
                });
                alipayIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            wechatPayIcon.setChecked(false);

                            alipayIcon.setClickable(false);
                            wechatPayIcon.setClickable(true);
                        }
                    }
                });
            }
        }

    }

    private void getPayInfo(String engineerToken){
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("mtoken", engineerToken);
        map.put("token", appContext.getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerPay, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                if (object.has(ResponseCode.ResponseState)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    EngineerPay pay = EngineerPay.fromJSONObject(object);
                    if (pay != null){
                        engineerPay = pay;
                        setViewData(pay);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }


    /**
     * ======================================微信支付 start====================================
     */

    private void payByPacket() {
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("mtoken", engineerToken);
        map.put("token", activeUser.getToken());
        map.put("coupons", "all");
        map.put("pay", "WeChat");
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerPayId, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                if (object != null){
                    float moneyMore = (float) object.optDouble("money");
                    if (moneyMore == 0){
                        //remember order sn
                        AppConfig config = AppConfig.getInstance(mContext);
                        String sn = object.optString("sn");
                        config.setCurrentOrder(sn);
                        Bundle bundle = new Bundle();
                        bundle.putInt(EngineerEvent.payResult, 0);
                        EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerInfo, bundle);
                        finish();
                    }else {
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.weixin_pay_fail));
                    }
                }else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.weixin_pay_fail));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getOrderId(boolean useCoupons){
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        User user = appContext.getActiveUser();
        HashMap<String,String> map = new HashMap<>();
        map.put("mtoken", engineerToken);
        map.put("token", user.getToken());
        if (useCoupons){
            map.put("coupons", "all");
        }else {
            map.put("coupons", "[]");
        }
        map.put("pay", "WeChat");
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(EngineerURLs.getEngineerPayId, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (object != null){
                    String state = object.optString(ResponseCode.ResponseState);
                    if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                        String order = object.optString("sn");
                        AppConfig config = AppConfig.getInstance(mContext);
                        config.setCurrentOrder(order);
                        config.setOrderType(AppConfig.engineerOrder);
                        float money = (float) object.optDouble("money");
                        String body = String.format(Locale.US, mContext.getResources().getString(R.string.weixin_pay_title), engineerBean.get_name());
                        WXPayTask task = new WXPayTask(mContext, msgApi, body, order, getProgressDialog());
                        task.execute(money);
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                        dismissDialog();
                    }
                }else {
                    dismissDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();
    }


    /**
     * ======================================微信支付 end====================================
     */

    private void dismissDialog(){
        if (progressDialog.isShowing() && !isFinishing()){
            progressDialog.dismiss();
        }
    }
}
