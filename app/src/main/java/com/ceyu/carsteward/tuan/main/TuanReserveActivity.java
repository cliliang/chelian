package com.ceyu.carsteward.tuan.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarURLs;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.timepick.DateTimePickerDialog;
import com.ceyu.carsteward.tuan.TuanMod;
import com.ceyu.carsteward.tuan.bean.TuanContentBean;
import com.ceyu.carsteward.tuan.bean.TuanModBean;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/7/21.
 */
public class TuanReserveActivity extends BaseActivity implements View.OnClickListener{

    private TextView shopNameView, shopAddressView, endTimeView, contentView, arriveShopView;
    private TextView lincenseNumberView, carInfoView;
    private LinearLayout achieveLayout, wechatPayLayout, alipayLayout, carInfoLayout;
    private EditText nameView, phoneView;
    private CheckBox wechatBox, alipayBox;
    private TextView moneyView, buttonView;
    private AppContext appContext;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private TuanContentBean tuanContentBean;
    private CarInfoBean carInfoBean;
    private Context mContext;
    private User activeUser;
    private String gid;
    private int payPath = 1;//1.微信支付  2.支付宝支付
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tuan_reserve_title);
        setContentView(R.layout.tuan_reserve_activity_layout);
        msgApi.registerApp(WXUtils.APP_ID);
        mContext = TuanReserveActivity.this;
        initView();
        appContext = (AppContext) mContext.getApplicationContext();
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            gid = bundle.getString(TuanEvent.shopId);
            tuanContentBean = bundle.getParcelable(TuanEvent.shopContent);
            carInfoBean = appContext.getTempCarInfo();
            if (carInfoBean == null){
                carInfoBean = appContext.getCarInfo();
            }
            setViewData(tuanContentBean, carInfoBean);
        }else {
            finish();
        }

    }

    private void initView(){
        shopNameView = (TextView) findViewById(R.id.tuan_reserve_shop_name);
        shopAddressView = (TextView) findViewById(R.id.tuan_reserve_shop_address_view);
        endTimeView = (TextView) findViewById(R.id.tuan_reserve_end_time);
        contentView = (TextView) findViewById(R.id.tuan_reserve_content_detail);
        arriveShopView = (TextView) findViewById(R.id.tuan_reserve_achive_shop_time);
        lincenseNumberView = (TextView) findViewById(R.id.reserve_input_license_number);
        carInfoView = (TextView) findViewById(R.id.tuan_reserve_car_info);
        nameView = (EditText) findViewById(R.id.tuan_reserve_input_name);
        phoneView = (EditText) findViewById(R.id.tuan_reserve_phone_number);
        wechatBox = (CheckBox) findViewById(R.id.tuan_reserve_wechat_box);
        alipayBox = (CheckBox) findViewById(R.id.tuan_reserve_alipay_box);
        moneyView = (TextView) findViewById(R.id.tuan_reserve_pay_money);
        buttonView = (TextView) findViewById(R.id.tuan_reserve_pay_button);
        achieveLayout = (LinearLayout) findViewById(R.id.tuan_reserve_achieve_layout);
        wechatPayLayout = (LinearLayout) findViewById(R.id.tuan_reserve_choice_wechat_pay_layout);
        alipayLayout = (LinearLayout) findViewById(R.id.tuan_reserve_choice_alipay_pay_layout);
        carInfoLayout = (LinearLayout) findViewById(R.id.tuan_reserve_add_car_layout);
        achieveLayout.setOnClickListener(this);
        wechatPayLayout.setOnClickListener(this);
        alipayLayout.setOnClickListener(this);
        carInfoLayout.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }

    private void setViewData(TuanContentBean contentBean, CarInfoBean carInfoBean){
        if (contentBean != null){
            TuanModBean modBean = contentBean.get_store();
            if (modBean != null){
                shopNameView.setText(modBean.get_name());
                shopAddressView.setText(modBean.get_address().replace("*", ""));
            }
            endTimeView.setText(String.format(Locale.US, getResources().getString(R.string.tuan_content_end_time), contentBean.get_end_service()));
            contentView.setText(contentBean.get_item());
            phoneView.setText(activeUser.getPhoneNumber());
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.roll(Calendar.DAY_OF_MONTH, 1);
            int hour = nowCalendar.get(Calendar.HOUR_OF_DAY);
            String hourString = hour < 10 ? "0" + hour : String.valueOf(hour);
            int min = nowCalendar.get(Calendar.MINUTE);
            String minString = min < 30 ? "00" : "30";
            arriveShopView.setText(String.format(Locale.US, "%tF", nowCalendar.getTime()) + " " + hourString + ":" + minString);
            moneyView.setText(contentBean.get_money());
        }

        if (carInfoBean != null){
            CarBrandInfoBean carBrandInfoBean = carInfoBean.getBrandInfoBean();
            if (carBrandInfoBean != null){
                carInfoView.setText(carBrandInfoBean.get_subBrandName() + " " + carBrandInfoBean.get_modelName() + " " + carBrandInfoBean.get_capacity() + " " + carBrandInfoBean.get_auto());
            }
            nameView.setText(carInfoBean.get_name());
            lincenseNumberView.setText(carInfoBean.get_plate());
        }
    }

    private void setCarInfoData(CarInfoBean carInfoData){
        if (carInfoData != null){
            CarBrandInfoBean brandInfoBean = carInfoData.getBrandInfoBean();
            if (brandInfoBean != null){
                carInfoView.setText(brandInfoBean.get_subBrandName() + " " + brandInfoBean.get_modelName());
            }
            lincenseNumberView.setText(carInfoData.get_plate());
            if (!StringUtils.isEmpty(carInfoData.get_name())){
                nameView.setText(carInfoData.get_name());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tuan_reserve_achieve_layout:
                showDateTimeDialog();
                break;
            case R.id.tuan_reserve_choice_wechat_pay_layout:
                payPath = 1;
                wechatBox.setChecked(true);
                alipayBox.setChecked(false);
                break;
            case R.id.tuan_reserve_choice_alipay_pay_layout:
                payPath = 2;
                wechatBox.setChecked(false);
                alipayBox.setChecked(true);
                break;
            case R.id.tuan_reserve_add_car_layout:
                Bundle bundle = new Bundle();
                bundle.putBoolean(TuanEvent.fromTuan, true);
                if (carInfoBean != null){
                    bundle.putInt(TuanEvent.choiceCarId, carInfoBean.get_id());
                }
                MainRouter.getInstance(mContext).showActivityForResult(ModuleNames.Car, TuanReserveActivity.this, CarUI.carOfMine,100, bundle);
                break;
            case R.id.tuan_reserve_pay_button:
                getOrderInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            if (resultCode == TuanEvent.choiceCar && data != null){
                Bundle bundle = data.getExtras();
                if (bundle != null){
                    carInfoBean = bundle.getParcelable(TuanEvent.choiceCarInfo);
                    setCarInfoData(carInfoBean);
                    appContext.setTempCarInfo(carInfoBean);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            int addCar = bundle.getInt(TuanEvent.addCarIntent);
            if (addCar == TuanEvent.addCar){
                getCarInfos();
            }
        }
        super.onNewIntent(intent);
    }

    private void showDateTimeDialog() {
        Date date = Utils.fromYYYYMMDDHHMM(arriveShopView.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final DateTimePickerDialog dialog = new DateTimePickerDialog(mContext,getResources().getString(R.string.please_choice_shop_reserve_time), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0);
        dialog.setOnClickSetDateTimeListener(new DateTimePickerDialog.OnClickSetDateTimeListener() {
            @Override
            public void setDateTime(String dateTime) {
                if (validDate(dateTime)){
                    arriveShopView.setText(dateTime);
                }
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private boolean validDate(String res){
        Calendar choice = Utils.fromyyyymmddhhmm(res);
        Calendar nowCalendar = Calendar.getInstance();
        if (choice.getTimeInMillis() <= nowCalendar.getTimeInMillis()){
            UIHelper.ToastMessage(mContext, getResources().getString(R.string.reserve_order_time_notice));
            return false;
        }
        return true;
    }

    private void getCarInfos(){
        progressDialog.show(mContext, false);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarList, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(TuanReserveActivity.this);
                ArrayList<CarInfoBean> list = CarInfoBean.fromJsonArray(array);
                if (list != null && list.size() > 0){
                    CarInfoBean bean = list.get(0);
                    if(bean != null){
                        carInfoBean = bean;
                        setCarInfoData(carInfoBean);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(TuanReserveActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getOrderInfo(){
        if (carInfoBean == null){
            UIHelper.ToastMessage(mContext, getResources().getString(R.string.tuan_order_please_add_car));
            return;
        }
        String name = nameView.getText().toString();
        if (StringUtils.isEmpty(name)){
            UIHelper.ToastMessage(mContext, getResources().getString(R.string.tuan_order_please_input_name));
            return;
        }
        String phone = phoneView.getText().toString();
        if (StringUtils.isEmpty(phone)){
            UIHelper.ToastMessage(mContext, getResources().getString(R.string.tuan_order_please_input_phone));
            return;
        }
        progressDialog.show(mContext, false);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("gid", gid);
        map.put("cid", String.valueOf(carInfoBean.get_id()));
        map.put("name", name);
        map.put("phone", phone);
        map.put("date", arriveShopView.getText().toString());
        if (payPath == 1){
            map.put("pay", "WeChat");
        }else if (payPath == 2){
            map.put("pay", "Alipay");
        }
        CheJSONObjectRequest request = new CheJSONObjectRequest(TuanURLs.addTuanOrder, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(TuanReserveActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    AppConfig appConfig = AppConfig.getInstance(mContext);
                    appConfig.setOrderType(AppConfig.tuanOrder);
                    String sn = object.optString("sn");
                    appConfig.setCurrentOrder(sn);
                    float money = (float)object.optDouble("money");
                    WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), sn, getProgressDialog());
                    task.execute(money);
                }else {
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(TuanReserveActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();

    }
}
