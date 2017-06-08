package com.ceyu.carsteward.tuan.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.timepick.DateTimePickerDialog;
import com.ceyu.carsteward.maintain.bean.MaintainOrderDetail;
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.maintain.main.MaintainURLs;
import com.ceyu.carsteward.tuan.bean.TuanOrderBean;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/7/22.
 */
public class TuanOrderActivity extends BaseActivity implements View.OnClickListener{

    private TextView shopNameView, shopAddressView, endTimeView, orderContentView, serviceTimeView;
    private TextView carInfoView, licenseView, useNameView, phoneView, moneyView, buttonView, changeView, describeView;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Context mContext;
    private View mainLayout;
    private TuanOrderBean tuanOrderBean;
    private User user;
    private String orderSn;
    private int orderState; //0:未支付  1:已预约  2：已完成  8:已退款  9：已取消
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tuan_order_title);
        setContentView(R.layout.tuan_order_detail_activity_layout);
        mContext = TuanOrderActivity.this;
        msgApi.registerApp(WXUtils.APP_ID);
        user = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderSn = bundle.getString(MaintainEvent.orderSN);
            getViewData(orderSn);
        }
        mainLayout.setVisibility(View.INVISIBLE);
    }

    private void initView(){
        mainLayout = findViewById(R.id.tuan_order_main_layout);
        shopNameView = (TextView) findViewById(R.id.tuan_order_shop_name);
        shopAddressView = (TextView) findViewById(R.id.tuan_order_shop_address_view);
        endTimeView = (TextView) findViewById(R.id.tuan_order_end_time);
        orderContentView = (TextView) findViewById(R.id.tuan_order_content_detail);
        serviceTimeView = (TextView) findViewById(R.id.tuan_order_achive_shop_time);
        carInfoView = (TextView) findViewById(R.id.tuan_order_car_info);
        licenseView = (TextView) findViewById(R.id.reserve_order_license_number);
        useNameView = (TextView) findViewById(R.id.tuan_order_input_name);
        phoneView = (TextView) findViewById(R.id.tuan_order_phone_number);
        moneyView = (TextView) findViewById(R.id.tuan_order_pay_money);
        buttonView = (TextView) findViewById(R.id.tuan_order_pay_button);
        changeView = (TextView) findViewById(R.id.tuan_order_change_time);
        describeView = (TextView) findViewById(R.id.tuan_order_describe_content);
    }

    private void setViewData(TuanOrderBean orderBean){
        if (orderBean != null){
            mainLayout.setVisibility(View.VISIBLE);
            orderState = orderBean.get_orderStateCode();
            shopNameView.setText(orderBean.get_storeName());
            shopAddressView.setText(orderBean.get_storeAddress().replace("*", ""));
            endTimeView.setText(String.format(Locale.US, getResources().getString(R.string.tuan_content_end_time), orderBean.get_end_service()));
            orderContentView.setText(orderBean.get_item());
            serviceTimeView.setText(orderBean.get_storeTime());
            describeView.setText(orderBean.get_txt());
            CarBrandInfoBean infoBean = orderBean.get_car();
            if (infoBean != null){
                carInfoView.setText(infoBean.get_subBrandName() + " " + infoBean.get_modelName() + " " + infoBean.get_capacity() + " " + infoBean.get_auto());
            }
            licenseView.setText(orderBean.get_carPlate());
            useNameView.setText(orderBean.get_name());
            phoneView.setText(orderBean.get_phone());
            moneyView.setText(String.format(Locale.US, "%.0f", orderBean.get_money()));

            if (orderState == 0){
                buttonView.setText(getResources().getString(R.string.engineer_pay_now));
                buttonView.setOnClickListener(this);
                changeView.setOnClickListener(this);
            }else if (orderState == 1){
                changeView.setOnClickListener(this);
                buttonView.setText(getResources().getString(R.string.reserve_cancel_order));
                buttonView.setOnClickListener(this);
            }else {
                buttonView.setText(orderBean.get_orderState());
                buttonView.setClickable(false);
            }
        }
    }

    private void getViewData(final String order){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("sn", order);
        map.put("token", user.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(TuanURLs.getOrderDetail, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(TuanOrderActivity.this);
                TuanOrderBean orderBean = TuanOrderBean.fromJson(object);
                if (orderBean != null){
                    tuanOrderBean = orderBean;
                    setViewData(orderBean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(TuanOrderActivity.this);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tuan_order_pay_button:
                if (orderState == 0){
                    //立即支付
                    AppConfig appConfig = AppConfig.getInstance(mContext);
                    appConfig.setOrderType(AppConfig.tuanOrder);
                    appConfig.setCurrentOrder(orderSn);
                    WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), orderSn, getProgressDialog());
                    task.execute(tuanOrderBean.get_money());
                }else if (orderState == 1){
                    //已经预约
                    cancelOrder();
                }
                break;
            case R.id.tuan_order_change_time:
                showDateTimeDialog();
                break;
        }
    }

    private void showDateTimeDialog() {
        final Date date = Utils.fromYYYYMMDDHHMM(serviceTimeView.getText().toString());;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final DateTimePickerDialog dialog = new DateTimePickerDialog(mContext,getResources().getString(R.string.please_order_change_time), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0);
        dialog.setOnClickSetDateTimeListener(new DateTimePickerDialog.OnClickSetDateTimeListener() {
            @Override
            public void setDateTime(String dateTime) {
                serviceTimeView.setText(dateTime);
                modifyTime();
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void cancelOrder(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        map.put("sn", orderSn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.cancelOrder, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(TuanOrderActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    UIHelper.ToastMessage(mContext, R.string.cancel_order_success);
                    finish();
                }else {
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(TuanOrderActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    public void modifyTime(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        map.put("sn", orderSn);
        map.put("value", serviceTimeView.getText().toString());
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(TuanURLs.modifyTime, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    UIHelper.ToastMessage(mContext, R.string.reserve_modify_time_success);
                }else {
                    UIHelper.ToastMessage(mContext, R.string.reserve_modify_time_fail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();
    }
}
