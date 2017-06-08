package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.OrderServiceItemView;
import com.ceyu.carsteward.common.ui.views.timepick.DateTimePickerDialog;
import com.ceyu.carsteward.maintain.bean.MaintainContent;
import com.ceyu.carsteward.maintain.bean.MaintainDiscount;
import com.ceyu.carsteward.maintain.bean.MaintainOrderDetail;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderActivity extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    private ImageView imageView;
    private TextView carInfoView, plateView, shopNameView, shopAddressView, freeContent, doorMoney, freeLayout, freeCount, optionalTitle;
    private TextView serviceTime, takeTime, nameView, phoneView, moneyView, takeAddress, backAddress, backMoneyView;
    private LinearLayout serveLayout, doorLayout, takeLayout, mainLayout, freeContentLayout, discountLayout, discountContentLayout, optionalLayout;
    private TextView cancelButton;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private User activeUser;
    private String orderSn;
    private MaintainOrderDetail orderDetail;
    private int orderState; //0:未支付  1:已预约  2：已完成  9：已取消
    private boolean onlinePay = false; //是否是在现支付
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_order_content_activity_layout);
        setTitle(getResources().getString(R.string.reserve_order_detail_content));
        mContext = MaintainOrderActivity.this;
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        activeUser = appContext.getActiveUser();
        initView();
        msgApi.registerApp(WXUtils.APP_ID);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderSn = bundle.getString(MaintainEvent.orderSN);
            getOrderDetail(orderSn);
        }
        mainLayout.setVisibility(View.GONE);
    }

    private void initView(){
        mainLayout = (LinearLayout) findViewById(R.id.maintain_order_main_layout);
        imageView = (ImageView) findViewById(R.id.maintain_order_car_icon);
        carInfoView = (TextView) findViewById(R.id.maintain_order_car_info);
        backMoneyView = (TextView) findViewById(R.id.online_pay_back_money);
        plateView = (TextView) findViewById(R.id.maintain_order_car_plate);
        shopNameView = (TextView) findViewById(R.id.maintain_order_shop_name);
        shopAddressView = (TextView) findViewById(R.id.maintain_order_shop_address);
        serveLayout = (LinearLayout) findViewById(R.id.maintain_order_service_layout);
        optionalLayout = (LinearLayout) findViewById(R.id.maintain_order_optional_service_layout);
        optionalTitle = (TextView) findViewById(R.id.maintain_order_optional_title);
        freeContentLayout = (LinearLayout) findViewById(R.id.maintain_order_free_content_layout);
        freeContent = (TextView) findViewById(R.id.maintain_order_free_content);
        serviceTime = (TextView) findViewById(R.id.maintain_order_service_time);
        nameView = (TextView) findViewById(R.id.maintain_order_use_name);
        phoneView = (TextView) findViewById(R.id.maintain_order_use_phone);
        moneyView = (TextView) findViewById(R.id.maintain_order_money);
        doorLayout = (LinearLayout) findViewById(R.id.maintain_order_door_layout);
        takeLayout = (LinearLayout) findViewById(R.id.maintain_order_take_layout);
        takeTime = (TextView) findViewById(R.id.maintain_order_take_time);
        takeAddress = (TextView) findViewById(R.id.maintain_order_take_address);
        backAddress = (TextView) findViewById(R.id.maintain_order_back_address);
        doorMoney = (TextView) findViewById(R.id.maintain_order_door_money);
        cancelButton = (TextView) findViewById(R.id.maintain_order_cancel_button);
        freeCount = (TextView) findViewById(R.id.maintain_order_free_layout);
        discountLayout = (LinearLayout) findViewById(R.id.maintain_order_discount_layout);
        discountContentLayout = (LinearLayout) findViewById(R.id.maintain_order_discount_content_layout);

    }

    private void setViewData(MaintainOrderDetail detail){
        orderDetail = detail;
        orderState = detail.get_orderStateCode();
        if (orderState != 9 && orderState != 2){
            findViewById(R.id.maintain_order_modify_service_time).setOnClickListener(this);
            findViewById(R.id.maintain_order_take_layout).setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }
        String payClass = detail.get_payClass();
        if (StringUtils.isEmpty(payClass) || payClass.equals("offline")){
            onlinePay = false;
        }else {
            onlinePay = true;
        }

        if (orderState == 0){
            if (!onlinePay){
                cancelButton.setVisibility(View.GONE);
            }else {
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setText(getResources().getString(R.string.engineer_pay_now));
            }
        }else if (orderState == 2 || orderState == 9){
            cancelButton.setText(detail.get_orderState());
            cancelButton.setVisibility(View.GONE);
        }else if (orderState == 1){
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(getResources().getString(R.string.reserve_cancel_order));
        }

        if (orderState == 1 || orderState == 9){
            if (onlinePay){
                backMoneyView.setVisibility(View.VISIBLE);
            }else {
                backMoneyView.setVisibility(View.INVISIBLE);
            }
        }

        mainLayout.setVisibility(View.VISIBLE);
        CheImageLoader imageLoader = new CheImageLoader(requestQueue, mContext);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.icon_my, R.mipmap.icon_my);
        imageLoader.get(detail.get_carPic(), imageListener);
        CarBrandInfoBean infoBean = detail.get_car();
        if(infoBean != null){
            carInfoView.setText(infoBean.get_subBrandName() + " " +infoBean.get_modelName() + " " +infoBean.get_capacity());
        }
        plateView.setText(detail.get_carPlate());
        shopNameView.setText(detail.get_storeName());
        shopAddressView.setText(detail.get_storeAddress());

        ArrayList<MaintainContent> contents = detail.get_item();
        serveLayout.removeAllViews();
        if (contents != null){
            for (int i = 0; i < contents.size(); i++){
                MaintainContent content = contents.get(i);
                OrderServiceItemView itemView = new OrderServiceItemView(mContext, false);
                itemView.setViewData(content);
                serveLayout.addView(itemView);
            }
        }

        ArrayList<MaintainContent> optionals = detail.get_optional();
        optionalLayout.removeAllViews();
        if (optionals != null && optionals.size() > 0){
            for (int i = 0; i < optionals.size(); i++){
                MaintainContent content = optionals.get(i);
                OrderServiceItemView itemView = new OrderServiceItemView(mContext, false);
                itemView.setViewData(content);
                optionalLayout.addView(itemView);
            }
        }else {
            optionalLayout.setVisibility(View.GONE);
            optionalTitle.setVisibility(View.GONE);
        }

        String contentString = detail.get_free();
        if (StringUtils.isEmpty(contentString)){
            freeContentLayout.setVisibility(View.GONE);
        }else {
            freeContent.setText(contentString);
            freeCount.setText(String.format(Locale.US,getResources().getString(R.string.maintain_order_free_count), detail.get_freeCount()));
        }
        serviceTime.setText(detail.get_time());
        nameView.setText(String.format(Locale.US, getResources().getString(R.string.reserve_order_use_name), detail.get_name()));
        phoneView.setText(String.format(Locale.US, getResources().getString(R.string.reserve_order_use_phone), detail.get_phone()));
        moneyView.setText(detail.get_money());
        if (detail.is_doorState()){
            doorLayout.setVisibility(View.VISIBLE);
            takeLayout.setVisibility(View.VISIBLE);
            takeTime.setText(detail.get_doorDate());
            takeAddress.setText(String.format(Locale.US, getResources().getString(R.string.reserve_order_take_address), detail.get_doorAddA()));
            backAddress.setText(String.format(Locale.US, getResources().getString(R.string.reserve_order_back_address), detail.get_doorAddB()));
            doorMoney.setText(detail.get_doorMoney());
        }else {
            doorLayout.setVisibility(View.GONE);
            takeLayout.setVisibility(View.GONE);
        }

        ArrayList<MaintainDiscount> discounts = detail.get_discount();
        if (discounts != null && discounts.size() > 0){
            discountLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < discounts.size(); i++){
                MaintainDiscount discount = discounts.get(i);
                OrderServiceItemView itemView = new OrderServiceItemView(mContext, true);
                itemView.setViewData(discount);
                discountContentLayout.addView(itemView);
            }
        }else {
            discountLayout.setVisibility(View.GONE);
        }

    }

    private void getOrderDetail(String sn){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("sn", sn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.getOrderDetail, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainOrderActivity.this);
                MaintainOrderDetail orderDetail = MaintainOrderDetail.fromJson(object);
                if (orderDetail != null){
                    setViewData(orderDetail);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MaintainOrderActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.maintain_order_modify_service_time:
                showDateTimeDialog(1);
                break;
            case R.id.maintain_order_take_layout:
                showDateTimeDialog(2);
                break;
            case R.id.maintain_order_cancel_button:
                if (orderState == 1){
                    cancelOrder();
                }else if (orderState == 0){
                    if (onlinePay){
                        AppConfig appConfig = AppConfig.getInstance(mContext);
                        appConfig.setOrderType(AppConfig.reserveOrder);
                        appConfig.setCurrentOrder(orderSn);
                        WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), orderSn, getProgressDialog());
                        task.execute(orderDetail.get_payMoney());
                    }
                }
                break;
        }
    }

    private void showDateTimeDialog(final int type) {
        final Date date;
        if (type == 1){
            date = Utils.fromYYYYMMDDHHMM(serviceTime.getText().toString());
        }else {
            date = Utils.fromYYYYMMDDHHMM(takeTime.getText().toString());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final DateTimePickerDialog dialog = new DateTimePickerDialog(mContext,getResources().getString(R.string.please_order_change_time), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0);
        dialog.setOnClickSetDateTimeListener(new DateTimePickerDialog.OnClickSetDateTimeListener() {
            @Override
            public void setDateTime(String dateTime) {
                if (type == 1){
                    if (validTime(type, dateTime, takeTime.getText().toString())){
                        serviceTime.setText(dateTime);
                        modifyOrderDate(type, dateTime);
                    }
                }else {
                    if (validTime(type, serviceTime.getText().toString(), dateTime)){
                        takeTime.setText(dateTime);
                        modifyOrderDate(type, dateTime);
                    }
                }
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private boolean validTime(int type, String serviceTime, String takeTime){
        Calendar serviceCalendar = Utils.fromyyyymmddhhmm(serviceTime);
        Calendar takeCalendar = Utils.fromyyyymmddhhmm(takeTime);
        Calendar currentCalendar = Calendar.getInstance();
        if (type == 1 &&  serviceCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
            UIHelper.ToastMessage(mContext, R.string.reserve_order_time_notice);
            return false;
        }
        if (type == 2 &&  takeCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
            UIHelper.ToastMessage(mContext, R.string.reserve_order_time_notice);
            return false;
        }
        if (serviceCalendar.getTimeInMillis() - takeCalendar.getTimeInMillis() < 60 * 60 * 1000){
            UIHelper.ToastMessage(mContext, R.string.take_time_late_service_time);
            return false;
        }
        return true;
    }

    public void modifyOrderDate(int type, String dateString){
        HashMap<String, String> map = new HashMap<>();
        if (type == 1){
            map.put("class", "Sorder_Date");
        }else {
            map.put("class", "Sorder_Door_Date");
        }
        map.put("token", activeUser.getToken());
        map.put("value", dateString);
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(MaintainURLs.modifyOrderDate, map, new Response.Listener<JSONObject>() {
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

    public void cancelOrder(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("sn", orderSn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.cancelOrder, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainOrderActivity.this);
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
                dismissDialog(MaintainOrderActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
