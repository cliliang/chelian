package com.ceyu.carsteward.self.ui;

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
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ReserveItemView;
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.self.bean.SelfMechanicBean;
import com.ceyu.carsteward.self.bean.SelfOrderDetail;
import com.ceyu.carsteward.self.bean.SelfPartBean;
import com.ceyu.carsteward.self.router.SelfRouter;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/9/23.
 */
public class SelfOrderActivity extends BaseActivity {
    private Context mContext;
    private User activeUser;
    private CheImageLoader imageLoader;
    private ImageView carImageView;
    private LinearLayout itemLayout, rootView;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private TextView carModelView, carPlateView, shopNameView, shopAddressView;
    private TextView reserveMechanicView, reserveShopTime, userNameView, userPhoneView;
    private TextView moneyView, buttonView;
    private int orderState; //0:未支付  1:已预约  2：已完成  9：已取消
    private String orderSn, mechanicToken;
    private float money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_order_info_activity_layout);
        setTitle(R.string.self_order_detail);
        mContext = this;
        imageLoader = new CheImageLoader(requestQueue, mContext);
        activeUser = ((AppContext)getApplicationContext()).getActiveUser();
        msgApi.registerApp(WXUtils.APP_ID);
        findView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            orderSn = bundle.getString(MaintainEvent.orderSN);
            getData(orderSn);
        }
        rootView.setVisibility(View.INVISIBLE);
    }

    private void findView(){
        rootView = (LinearLayout) findViewById(R.id.self_order_root_view);
        carImageView = (ImageView) findViewById(R.id.self_order_car_image);
        carModelView = (TextView) findViewById(R.id.self_order_car_model);
        carPlateView = (TextView) findViewById(R.id.self_order_car_plate);
        shopNameView = (TextView) findViewById(R.id.self_order_shop_name);
        shopAddressView = (TextView) findViewById(R.id.self_order_shop_address);
        itemLayout = (LinearLayout) findViewById(R.id.self_order_item_layout);
        reserveMechanicView = (TextView) findViewById(R.id.self_order_reserve_mechanic);
        reserveShopTime = (TextView) findViewById(R.id.self_order_reserve_time);
        userNameView = (TextView) findViewById(R.id.self_order_user_name);
        userPhoneView = (TextView) findViewById(R.id.self_order_user_phone);
        moneyView = (TextView) findViewById(R.id.self_order_pay_money);
        buttonView = (TextView) findViewById(R.id.self_order_pay_button);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderState == 0){
                    AppConfig appConfig = AppConfig.getInstance(mContext);
                    appConfig.setOrderType(AppConfig.selfOrder);
                    appConfig.setCurrentOrder(orderSn);
                    WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), orderSn, getProgressDialog());
                    task.execute(money);
                }else if (orderState == 1 || orderState == 2){
                    Bundle bundle = new Bundle();
                    bundle.putString(MaintainEvent.mechanicToken, mechanicToken);
                    bundle.putString(MaintainEvent.orderSN, orderSn);
                    SelfRouter.getInstance(mContext).showActivity(SelfUI.selfComment, bundle);
                }
            }
        });
    }

    private void getData(String sn){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("sn", sn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.getOrderInfo, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(SelfOrderActivity.this);
                SelfOrderDetail detail = SelfOrderDetail.fromJson(response);
                setViewData(detail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(SelfOrderActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(SelfOrderDetail detail){
        if (detail == null){
            return;
        }
        rootView.setVisibility(View.VISIBLE);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(carImageView, R.mipmap.default_car, R.mipmap.default_car);
        imageLoader.get(detail.get_carPic(), listener);
        CarBrandInfoBean infoBean = detail.getCarInfoBean();
        if (infoBean != null){
            carModelView.setText(infoBean.get_subBrandName() + " " + infoBean.get_modelName() + " " + infoBean.get_capacity());
        }
        carPlateView.setText(detail.get_carPlate());
        shopNameView.setText(detail.get_storeName());
        shopAddressView.setText(detail.get_storeAddress());
        List<SelfPartBean> items = detail.get_item();
        if (items != null && items.size() > 0){
            for (SelfPartBean partBean : items){
                ReserveItemView itemView = new ReserveItemView(mContext);
                itemView.setViewData(partBean);
                itemLayout.addView(itemView);
            }
        }
        SelfMechanicBean mechanicBean = detail.get_mechanic();
        if (mechanicBean != null){
            mechanicToken = mechanicBean.get_token();
            reserveMechanicView.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_mechanic_name), mechanicBean.get_name()));
        }
        reserveShopTime.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_receive_shop_time), detail.get_time()));
        userNameView.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_user_name), detail.get_name()));
        userPhoneView.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_user_phone), detail.get_phone()));
        moneyView.setText(String.format(Locale.US, "%.0f", detail.get_payMoney()));
        money = detail.get_payMoney();
        //0:未支付  1:已预约  2：已完成  9：已取消
        buttonView.setEnabled(true);
        orderState = detail.get_orderStateCode();
        if (orderState == 0){
            buttonView.setText(getResources().getString(R.string.engineer_pay_now));
        }else if (orderState == 1){
            buttonView.setText(getResources().getString(R.string.self_reserve_finish));
        }else if (orderState == 2){
            buttonView.setText(getResources().getString(R.string.self_reserve_finish));
        }else if (orderState == 9){
            buttonView.setText(detail.get_orderState());
        }
    }
}
