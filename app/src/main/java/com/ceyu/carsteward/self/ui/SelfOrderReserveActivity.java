package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.adapter.ChoiceGridAdapter;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ReserveItemView;
import com.ceyu.carsteward.self.bean.SelfCarBean;
import com.ceyu.carsteward.self.bean.SelfOrderContentBean;
import com.ceyu.carsteward.self.bean.SelfOrderMechanicBean;
import com.ceyu.carsteward.self.bean.SelfPartBean;
import com.ceyu.carsteward.self.bean.SelfShopLocalBean;
import com.ceyu.carsteward.self.bean.SelfStoreBean;
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
 * Created by chen on 15/9/21.
 */
public class SelfOrderReserveActivity extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    private User activeUser;
    private String cid; //车辆配置ID
    private String mileage;
    private String select;
    private String mtoken; //技师ID
    private String sid; //店铺ID
    private String mycid; //我的车辆ID
    private String choiceTime;
    private String[] localArray;
    private String[] letterList;

    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private LinearLayout partsLayout, bottomLayout, rootView, choiceShopLayout;
    private TextView shopName, shopAddress, mechanicName, mechanicTime;
    private TextView carInfoView, carPlate1, carPlate2, moneyView, payView;
    private EditText userNameView, userPhoneView;
    private EditText carPlate3;
    private ImageView choicePlate1View, choicePlate2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.self_order_commit);
        setContentView(R.layout.self_order_commit_activity_layout);
        mContext = this;
        msgApi.registerApp(WXUtils.APP_ID);
        activeUser = ((AppContext)getApplicationContext()).getActiveUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            cid = bundle.getString(CarEvent.carModelId);
            mileage = bundle.getString(CarEvent.carMileage);
            select = bundle.getString(CarEvent.selectedIds);
            mtoken = bundle.getString(CarEvent.mechanicToken);
            sid = bundle.getString(CarEvent.shopId);
            mycid = bundle.getString(CarEvent.carId);
            choiceTime = bundle.getString(CarEvent.mechanicDate);
            getData();
        }
        localArray = getResources().getStringArray(R.array.province_list);
        letterList = getResources().getStringArray(R.array.letter_list);

        rootView = (LinearLayout) findViewById(R.id.self_reserve_root_view);
        partsLayout = (LinearLayout) findViewById(R.id.self_maintain_content_layout);
        bottomLayout = (LinearLayout) findViewById(R.id.self_reserve_bottom_layout);
        shopName = (TextView) findViewById(R.id.self_reserve_shop_name);
        shopAddress = (TextView) findViewById(R.id.self_reserve_shop_address);
        mechanicName = (TextView) findViewById(R.id.self_reserve_mechanic_name);
        mechanicTime = (TextView) findViewById(R.id.self_reserve_mechanic_time);
        carInfoView = (TextView) findViewById(R.id.self_reserve_car_info);
        carPlate1 = (TextView) findViewById(R.id.self_reserve_car_plate1);
        carPlate2 = (TextView) findViewById(R.id.self_reserve_car_plate2);
        carPlate3 = (EditText) findViewById(R.id.self_reserve_car_plate3);
        userNameView = (EditText) findViewById(R.id.self_reserve_use_name);
        userPhoneView = (EditText) findViewById(R.id.self_reserve_use_phone);
        moneyView = (TextView) findViewById(R.id.self_reserve_money_pay);
        payView = (TextView) findViewById(R.id.self_reserve_pay_button);
        choiceShopLayout = (LinearLayout) findViewById(R.id.self_reserve_choice_shop_layout);
        choicePlate1View = (ImageView) findViewById(R.id.self_reserve_choice_plate1);
        choicePlate2View = (ImageView) findViewById(R.id.self_reserve_choice_plate2);
        findViewById(R.id.self_reserve_choice_mechanic_layout).setOnClickListener(this);
        findViewById(R.id.self_reserve_time_layout).setOnClickListener(this);
        choicePlate1View.setOnClickListener(this);
        choicePlate2View.setOnClickListener(this);
        choiceShopLayout.setOnClickListener(this);
        payView.setOnClickListener(this);
        payView.setEnabled(false);
        rootView.setVisibility(View.INVISIBLE);
    }

    private void getData(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("cid", cid);
        map.put("mileage", mileage);
        map.put("select", select);
        map.put("mtoken", mtoken);
        map.put("sid", sid);
        map.put("mycid", mycid);
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.getOrderContent, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(SelfOrderReserveActivity.this);
                if (response.has(ResponseCode.ResponseState)){
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(response.optString(ResponseCode.ResponseInfo)));
                }else {
                    SelfOrderContentBean bean = SelfOrderContentBean.fromJson(response);
                    setViewData(bean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(SelfOrderReserveActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(SelfOrderContentBean bean){
        if (bean == null){
            return;
        }
        rootView.setVisibility(View.VISIBLE);
        payView.setEnabled(true);
        List<SelfPartBean> partBeans = bean.getPartsBean();
        if (partBeans != null){
            for (SelfPartBean partBean : partBeans){
                ReserveItemView itemView = new ReserveItemView(mContext);
                itemView.setViewData(partBean);
                partsLayout.addView(itemView);
            }
        }

        SelfStoreBean storeBean = bean.getStoreBean();
        if (storeBean != null){
            shopName.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_shop), storeBean.get_name()));
            shopAddress.setText(storeBean.get_address());
        }

        SelfOrderMechanicBean mechanicBean = bean.getMechanicBean();
        if (mechanicBean != null){
            mechanicName.setText(mechanicBean.get_name());
            mechanicTime.setText(choiceTime);
        }

        SelfCarBean carBean = bean.getCarBean();
        if (carBean != null){
            CarBrandInfoBean brandInfoBean = carBean.get_brandInfo();
            carInfoView.setText(brandInfoBean.get_subBrandName() + " " +brandInfoBean.get_capacity() + " " + brandInfoBean.get_auto());
            String plateString = carBean.get_plate();
            if (!StringUtils.isEmpty(plateString) && plateString.length() == 7){
                carPlate1.setText(String.valueOf(plateString.charAt(0)));
                carPlate2.setText(String.valueOf(plateString.charAt(1)));
                carPlate3.setText(plateString.substring(2, 7));
            }
        }
        userPhoneView.setText(activeUser.getPhoneNumber());
        moneyView.setText(String.format(Locale.US, "%.0f", bean.get_money()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.self_reserve_choice_plate1:
                showChoiceWindow(1, localArray);
                break;
            case R.id.self_reserve_choice_plate2:
                showChoiceWindow(2, letterList);
                break;
            case R.id.self_reserve_pay_button:
                String choiceTime = mechanicTime.getText().toString();
                if (StringUtils.isEmpty(choiceTime)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_please_choice_reserve_time));
                    return;
                }
                String plate = carPlate3.getText().toString();
                if (StringUtils.isEmpty(plate) || !(plate.length() == 5)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_input_right_plate_number));
                    return;
                }
                String userName = userNameView.getText().toString();
                if (StringUtils.isEmpty(userName)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.input_car_user_name));
                    return;
                }
                String phoneString = userPhoneView.getText().toString();
                if (StringUtils.isEmpty(phoneString) || !(phoneString.length() == 11)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.input_right_phone_number));
                    return;
                }
                payOrder();
                break;
            case R.id.self_reserve_choice_shop_layout:
                Bundle bundle = new Bundle();
                bundle.putBoolean(SelfEvent.choiceShop, true);
                bundle.putString(SelfEvent.choiceShopId, sid);
                SelfRouter.getInstance(mContext).showActivityForResult(SelfOrderReserveActivity.this, SelfUI.selfMap, SelfEvent.CHOICE_SHOP, bundle);
                break;
            case R.id.self_reserve_choice_mechanic_layout:
                Bundle mechanicBundle = new Bundle();
                mechanicBundle.putBoolean(SelfEvent.choiceMechanic, true);
                mechanicBundle.putString(SelfEvent.mechanicToken, mtoken);
                SelfRouter.getInstance(mContext).showActivityForResult(SelfOrderReserveActivity.this, SelfUI.selfMechanic, SelfEvent.CHOICE_MECHANIC, mechanicBundle);
                break;
            case R.id.self_reserve_time_layout:
                Bundle timeBundle = new Bundle();
                timeBundle.putBoolean(SelfEvent.choiceTime, true);
                SelfRouter.getInstance(mContext).showActivityForResult(SelfOrderReserveActivity.this, SelfUI.selfTime, SelfEvent.CHOICE_TIME, timeBundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SelfEvent.CHOICE_SHOP && resultCode == SelfEvent.CHOICE_SHOP_RESULT){
            if (data != null){
                SelfShopLocalBean bean = data.getParcelableExtra(SelfEvent.shopDetail);
                if (bean != null){
                    sid = bean.get_id();
                    shopName.setText(String.format(Locale.US, getResources().getString(R.string.self_reserve_shop), bean.get_name()));
                    shopAddress.setText(bean.get_address());
                }
            }
        }else if (requestCode == SelfEvent.CHOICE_MECHANIC && resultCode == SelfEvent.CHOICE_MECHANIC_RESULT){
            if (data != null){
                mtoken = data.getStringExtra(SelfEvent.choiceMechanicToken);
                mechanicName.setText(data.getStringExtra(SelfEvent.choiceMechanicName));
                mechanicTime.setText("");
                mechanicTime.setHint(getResources().getString(R.string.self_please_choice_reserve_time));
            }
        }else if (requestCode == SelfEvent.CHOICE_TIME && resultCode == SelfEvent.CHOICE_TIME_RESULT){
            if (data != null){
                choiceTime = data.getStringExtra(SelfEvent.choiceTimeInfo);
                mechanicTime.setText(choiceTime);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void payOrder(){
        progressDialog.show(mContext, false);
        HashMap<String, String> map = new HashMap<>();
        map.put("cid", cid);
        map.put("mileage", mileage);
        map.put("select", select);
        map.put("mtoken", mtoken);
        map.put("sid", sid);
        map.put("token", activeUser.getToken());
        map.put("mycid", mycid);
        map.put("phone", userPhoneView.getText().toString());
        map.put("name", userNameView.getText().toString());
        map.put("plate", carPlate1.getText().toString() + carPlate2.getText().toString()+carPlate3.getText().toString());
        map.put("date", choiceTime);
        map.put("pay", "WeChat");
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.payShopOrder, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(SelfOrderReserveActivity.this);
                if (response != null){
                    String state = response.optString(ResponseCode.ResponseState);
                    if (!StringUtils.isEmpty(state) && ResponseCode.ResponseOK.equals(state)){
                        AppConfig appConfig = AppConfig.getInstance(mContext);
                        appConfig.setOrderType(AppConfig.selfOrder);
                        String orderId = response.optString("sn");
                        appConfig.setCurrentOrder(orderId);
                        float money = (float) response.optDouble("money");
                        WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), orderId, getProgressDialog());
                        task.execute(money);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(SelfOrderReserveActivity.this);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void showChoiceWindow(final int type, String[] resource){
        final PopupWindow window = new PopupWindow(mContext, null);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.show_local_window_layout, null);
        window.setContentView(contentView);
        GridView gridView = (GridView) contentView.findViewById(R.id.show_simple_grid_id);
        final ChoiceGridAdapter adapter = new ChoiceGridAdapter(mContext, resource);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) adapter.getItem(position);
                if (!StringUtils.isEmpty(item)) {
                    if (type == 1) {
                        carPlate1.setText(item);
                    } else {
                        carPlate2.setText(item);
                    }
                }
                if (window.isShowing() && !SelfOrderReserveActivity.this.isFinishing()) {
                    window.dismiss();
                }
            }
        });
        View hindView = contentView.findViewById(R.id.window_hide_layout_id);
        hindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing() && !SelfOrderReserveActivity.this.isFinishing()) {
                    window.dismiss();
                }
            }
        });
        window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);
        window.showAtLocation(bottomLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
