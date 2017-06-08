package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.adapter.ChoiceGridAdapter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.AppManager;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ReserveItemView;
import com.ceyu.carsteward.common.ui.views.SlideSwitch;
import com.ceyu.carsteward.common.ui.views.timepick.DateTimePickerDialog;
import com.ceyu.carsteward.maintain.bean.MaintainCarInfo;
import com.ceyu.carsteward.maintain.bean.MaintainParts;
import com.ceyu.carsteward.maintain.bean.MaintainPayBean;
import com.ceyu.carsteward.maintain.bean.MaintainShopInfo;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.packet.bean.MyPackets;
import com.ceyu.carsteward.packet.bean.PacketInfo;
import com.ceyu.carsteward.packet.main.PacketURLs;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wxapi.WXPayTask;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/26.
 */
public class MaintainReserveActivity extends BaseActivity implements View.OnClickListener{

    private TextView shopNameView, shopAddressView, timeView, priceView, localView, licenseView, wechatDiscount, ticketView, optionalTitle;
    private LinearLayout maintainLayout, mainLayout, bottomLayout, takeCarLayout, wechatPayLayout, shopPayLayout, ticketLayout, optionalLayout;
    private EditText nameView, phoneView, inputLicense;
    private CheckBox wechatBox, alipayBox, shopBox;
    private SlideSwitch slideSwitch;
    private TextView payButton, noticeView;
    private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Context mContext;
    private final int Request_Ask = 10; //上门取车
    private final int Request_Ticket = 11; //代金券
    private PacketInfo packetInfo;
    private boolean getCarHome = false;
    private String[] localArray;
    private String[] letterList;
    private String takeTime, takeAddress, backAddress, select, optional;
    private int sid, cid, mycid, kilo;
    private String doorPrice = "79";
//    private int door = 0;
    private boolean onlinePay = false;
    private int marketChoiceType = 0; // 当支付在线支付时，存储上次选择的付费方式  1.微信支付  2.支付宝支付 3.到店支付
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.reserve_detail_content);
        setContentView(R.layout.maintain_reserve_activity_layout);
        msgApi.registerApp(WXUtils.APP_ID);
        mContext = MaintainReserveActivity.this;
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            sid = bundle.getInt(MaintainEvent.shopId);
            cid = bundle.getInt(MaintainEvent.CarModelId);
            mycid = bundle.getInt(MaintainEvent.CarId);
            kilo = bundle.getInt(MaintainEvent.CarKilo);
            select = bundle.getString(MaintainEvent.selectIds);
            optional = bundle.getString(MaintainEvent.optionalIds);
            onlinePay = bundle.getBoolean(MaintainEvent.onlinePay);
        }
        localArray = getResources().getStringArray(R.array.province_list);
        letterList = getResources().getStringArray(R.array.letter_list);
        mainLayout.setVisibility(View.INVISIBLE);
        getComboData(true);
        getRedPacket();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView(){
        mainLayout = (LinearLayout) findViewById(R.id.reserve_main_layout);
        shopNameView = (TextView) findViewById(R.id.reserve_shop_name);
        bottomLayout = (LinearLayout) findViewById(R.id.reserve_bottom_view);
        shopAddressView = (TextView) findViewById(R.id.reserve_shop_address);
        maintainLayout = (LinearLayout) findViewById(R.id.reserve_maintain_item_layout);
        nameView = (EditText) findViewById(R.id.reserve_input_name_view);
        phoneView = (EditText) findViewById(R.id.reserve_input_phone_view);
        inputLicense = (EditText) findViewById(R.id.reserve_input_license_number);
        timeView = (TextView) findViewById(R.id.reserve_choice_time_view);
        wechatDiscount = (TextView) findViewById(R.id.reserve_wechat_discount);
        localView = (TextView) findViewById(R.id.reserve_license_local_view);
        licenseView = (TextView) findViewById(R.id.reserve_license_number_view);
        wechatBox = (CheckBox) findViewById(R.id.reserve_wechat_box);
        alipayBox = (CheckBox) findViewById(R.id.reserve_alipay_box);
        shopBox = (CheckBox) findViewById(R.id.reserve_shop_box);
        ticketView = (TextView) findViewById(R.id.reserve_pay_with_ticket_text);
        slideSwitch = (SlideSwitch) findViewById(R.id.reserve_go_home_switch);
        takeCarLayout = (LinearLayout) findViewById(R.id.reserve_home_take_car_layout);
        priceView = (TextView) findViewById(R.id.reserve_total_price_view);
        noticeView = (TextView) findViewById(R.id.maintain_reserve_online_notice);
        payButton = (TextView) findViewById(R.id.maintain_reserve_button);
        optionalTitle = (TextView) findViewById(R.id.reserve_optional_title);
        optionalLayout = (LinearLayout) findViewById(R.id.reserve_optional_maintain_item_layout);
        payButton.setOnClickListener(this);
        wechatPayLayout = (LinearLayout) findViewById(R.id.reserve_choice_wechat_pay_layout);
        wechatPayLayout.setOnClickListener(this);
        ticketLayout = (LinearLayout) findViewById(R.id.reserve_pay_with_ticket_layout);
        findViewById(R.id.reserve_choice_time_layout).setOnClickListener(this);
        findViewById(R.id.reserve_choice_alipay_pay_layout).setOnClickListener(this);
        shopPayLayout = (LinearLayout) findViewById(R.id.reserve_choice_shop_pay_layout);
        shopPayLayout.setOnClickListener(this);
        findViewById(R.id.reserve_show_choice_local).setOnClickListener(this);
        findViewById(R.id.reserve_show_choice_number).setOnClickListener(this);
        nameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    return true;
                }
                return false;
            }
        });

        slideSwitch.setOnChangedListener(new SlideSwitch.OnChangedListener() {
            @Override
            public void onChanged(SlideSwitch view, boolean on, boolean fromUser) {
                String maintainTime = timeView.getText().toString();
                if (on) {
                    if (!StringUtils.isEmpty(maintainTime)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(MaintainEvent.maintainTime, maintainTime);
                        bundle.putString(MaintainEvent.doorPrice, doorPrice);
                        bundle.putString(MaintainEvent.takeCarTime, takeTime);
                        bundle.putString(MaintainEvent.takeCarAddress, takeAddress);
                        bundle.putString(MaintainEvent.backCarAddress, backAddress);
                        MaintainRouter.getInstance(mContext).showActivityForResult(MaintainReserveActivity.this, MaintainUI.takeCatAtHome, Request_Ask, bundle);
                    } else {
                        slideSwitch.setStatus(false);
                        UIHelper.ToastMessage(mContext, R.string.please_choice_service_time);
                    }
                }else {
                    if (fromUser){
                        getComboData(false);
                    }
                }
            }
        });

        inputLicense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String licenseString = s.toString();
                if (!StringUtils.isEmpty(licenseString) && licenseString.length() == 5) {
                    hideSoftInput();
                }
            }
        });
    }

    private void getComboData(final boolean firstGet) {
        requestQueue.cancelAll(this);
        progressDialog.show(mContext, false);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        User user = appContext.getActiveUser();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        map.put("cid", String.valueOf(cid));
        map.put("sid", String.valueOf(sid));
        map.put("mileage", String.valueOf(kilo));
        map.put("select", select);
        map.put("optional", optional);
        map.put("mycid", String.valueOf(mycid));
        if (onlinePay){
            map.put("door", String.valueOf(slideSwitch.getState() ? 1 : 0));
            if (packetInfo != null){
                ArrayList<Integer> list = new ArrayList<>();
                list.add(packetInfo.get_id());
                map.put("coupons", list.toString());
            }
        }
        map.put("pay", String.valueOf(onlinePay ? 1 : 0));
        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.getMaintainInfo_v2, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainReserveActivity.this);
                MaintainShopInfo info = MaintainShopInfo.fromJsonObject(object);
                if (info != null) {
                    setViewData(info, firstGet);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MaintainReserveActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(MaintainShopInfo shopInfo, boolean firstSet){
        if (shopInfo != null){
            mainLayout.setVisibility(View.VISIBLE);
            payButton.setEnabled(true);
            doorPrice = shopInfo.get_door();
            shopNameView.setText(shopInfo.get_name());
            shopAddressView.setText(shopInfo.get_address().replace("*", ""));
            String discount = shopInfo.get_onlinePay();
            if (!StringUtils.isEmpty(discount)){
                wechatDiscount.setText(discount);
            }
            MaintainParts parts = shopInfo.get_parts();
            if (parts != null){
                ArrayList<MaintainSubContent> subContent = parts.getSubContents();
                MaintainSubContent humanSubContent = new MaintainSubContent();
                humanSubContent.set_name(getResources().getString(R.string.maintain_human_price));
                MaintainPayBean payBean = shopInfo.get_money();
                if (payBean != null){
                    humanSubContent.set_price(payBean.get_human_txt());
                }

                if (subContent == null){
                    subContent = new ArrayList<>();
                }
                subContent.add(humanSubContent);
                maintainLayout.removeAllViews();
                for (int i = 0; i < subContent.size(); i++){
                    ReserveItemView itemView = new ReserveItemView(mContext);
                    maintainLayout.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    itemView.setViewData(subContent.get(i));
                }
                if (payBean != null){
                    priceView.setText(payBean.get_sum());
                }
            }

            optionalLayout.removeAllViews();
            MaintainParts optionals = shopInfo.get_optional();
            if (optionals != null){
                ArrayList<MaintainSubContent> subContents = optionals.getSubContents();
                ArrayList<MaintainSubContent> selectOptional = new ArrayList<>();
                for (MaintainSubContent subContent : subContents){
                    if (subContent.isSelect()){
                        selectOptional.add(subContent);
                    }
                }
                if (selectOptional.size() > 0){
                    for (MaintainSubContent subContent : selectOptional){
                        ReserveItemView itemView = new ReserveItemView(mContext);
                        optionalLayout.addView(itemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        itemView.setViewData(subContent);
                    }
                }else {
                    optionalTitle.setVisibility(View.GONE);
                    optionalLayout.setVisibility(View.GONE);
                }
            }

            onlinePay = shopInfo.is_onlinePayState();
            if (onlinePay){
                if (marketChoiceType == 1){
                    onlinePay = true;
                    wechatBox.setChecked(true);
                    alipayBox.setChecked(false);
                    shopBox.setChecked(false);
                    noticeView.setVisibility(View.VISIBLE);
                    ticketLayout.setVisibility(View.VISIBLE);
                }else if (marketChoiceType == 2){
                    onlinePay = true;
                    wechatBox.setChecked(false);
                    alipayBox.setChecked(true);
                    shopBox.setChecked(false);
                    noticeView.setVisibility(View.VISIBLE);
                    ticketLayout.setVisibility(View.VISIBLE);
                }else if (marketChoiceType == 3){
                    onlinePay = false;
                    wechatBox.setChecked(false);
                    alipayBox.setChecked(false);
                    shopBox.setChecked(true);
                    noticeView.setVisibility(View.GONE);
                    ticketLayout.setVisibility(View.GONE);
                }
                wechatPayLayout.setVisibility(View.VISIBLE);
                shopPayLayout.setVisibility(View.VISIBLE);

            }else {
                shopBox.setChecked(true);
                wechatBox.setChecked(false);
                alipayBox.setChecked(false);
                noticeView.setVisibility(View.GONE);
                ticketLayout.setVisibility(View.GONE);
                wechatPayLayout.setVisibility(View.GONE);
                shopPayLayout.setVisibility(View.VISIBLE);
                takeCarLayout.setVisibility(View.GONE);
            }

            if (firstSet){
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.roll(Calendar.DAY_OF_MONTH, 1);
                int hour = nowCalendar.get(Calendar.HOUR_OF_DAY);
                String hourString = hour < 10 ? "0" + hour : String.valueOf(hour);
                int min = nowCalendar.get(Calendar.MINUTE);
                String minString = min < 30 ? "00" : "30";
                timeView.setText(String.format(Locale.US, "%tF", nowCalendar.getTime()) + " " + hourString + ":" + minString);
                //phone number
                AppContext appContext = (AppContext) mContext.getApplicationContext();
                String inputPhone = phoneView.getText().toString();
                phoneView.setText(appContext.getActiveUser().getPhoneNumber());
                //car license

                MaintainCarInfo carInfo = shopInfo.get_carInfo();
                if (carInfo != null){
                    String plate = carInfo.get_plate();
                    if (!StringUtils.isEmpty(plate) && plate.length() == 7){
                        localView.setText(String.valueOf(plate.charAt(0)));
                        licenseView.setText(String.valueOf(plate.charAt(1)));
                        inputLicense.setText(plate.substring(2, 7));
                    }
                    String nameString = carInfo.get_name();
                    if (!StringUtils.isEmpty(nameString)){
                        nameView.setText(carInfo.get_name());
                    }
                }
            }
        }
    }

    private void showDateTimeDialog() {
        Date date = Utils.fromYYYYMMDDHHMM(timeView.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final DateTimePickerDialog dialog = new DateTimePickerDialog(mContext,getResources().getString(R.string.please_choice_shop_reserve_time), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0);
        dialog.setOnClickSetDateTimeListener(new DateTimePickerDialog.OnClickSetDateTimeListener() {
            @Override
            public void setDateTime(String dateTime) {
                if (validDate(dateTime)){
                    timeView.setText(dateTime);
                }
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private boolean validDate(String serviceTime){
        Calendar choiceCalendar = Utils.fromyyyymmddhhmm(serviceTime);
        int choiceHour = choiceCalendar.get(Calendar.HOUR_OF_DAY);
        int choiceMinute = choiceCalendar.get(Calendar.MINUTE);

        Calendar currentCalendar = Calendar.getInstance();
        if (choiceCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
            UIHelper.ToastMessage(mContext, getResources().getString(R.string.reserve_order_time_notice));
            return false;
        }
        if (slideSwitch.getState()){
            Calendar doorCalendar = Utils.fromyyyymmddhhmm(takeTime);
            if (choiceCalendar.getTimeInMillis() - doorCalendar.getTimeInMillis() < 60 * 60 * 1000){
                UIHelper.ToastMessage(mContext, R.string.take_time_late_service_time);
                return false;
            }
        }
        return true;
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
                        localView.setText(item);
                    } else {
                        licenseView.setText(item);
                    }
                }
                if (window.isShowing() && !MaintainReserveActivity.this.isFinishing()) {
                    window.dismiss();
                }
            }
        });
        View hindView = contentView.findViewById(R.id.window_hide_layout_id);
        hindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing() && !MaintainReserveActivity.this.isFinishing()) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Request_Ask){
            if (resultCode == RESULT_OK){
                if (data != null){
                    takeTime = data.getStringExtra(MaintainEvent.takeCarTime);
                    takeAddress = data.getStringExtra(MaintainEvent.takeCarAddress);
                    backAddress = data.getStringExtra(MaintainEvent.backCarAddress);
                    getComboData(false);
                }
            }else {
                slideSwitch.setStatus(false);
            }
        }else if (requestCode == Request_Ticket){
            if (resultCode == RESULT_OK && data != null){
                packetInfo = data.getParcelableExtra(MaintainEvent.payTicket);
                if (packetInfo != null){
                    ticketView.setText(String.format(Locale.US, getResources().getString(R.string.reserve_with_packet_money), packetInfo.getNum()));
                    getComboData(false);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.reserve_choice_wechat_pay_layout:
                marketChoiceType = 1;
                onlinePay = true;
                wechatBox.setChecked(true);
                alipayBox.setChecked(false);
                shopBox.setChecked(false);
                getComboData(false);
                buttonClickable();
                takeCarLayout.setVisibility(View.VISIBLE);
                ticketLayout.setVisibility(View.VISIBLE);
                takeCarLayout.startAnimation(Utils.getShowAnimation());
                ticketLayout.startAnimation(Utils.getShowAnimation());
                break;
            case R.id.reserve_choice_alipay_pay_layout:
                marketChoiceType = 2;
                onlinePay = true;
                wechatBox.setChecked(false);
                alipayBox.setChecked(true);
                shopBox.setChecked(false);
                getComboData(false);
                buttonClickable();
                takeCarLayout.setVisibility(View.VISIBLE);
                ticketLayout.setVisibility(View.VISIBLE);
                takeCarLayout.startAnimation(Utils.getShowAnimation());
                ticketLayout.startAnimation(Utils.getShowAnimation());
                break;
            case R.id.reserve_choice_shop_pay_layout:
                marketChoiceType = 3;
                onlinePay = false;
                wechatBox.setChecked(false);
                alipayBox.setChecked(false);
                shopBox.setChecked(true);
                buttonClickable();
                takeCarLayout.startAnimation(Utils.getHiddenAnimation(takeCarLayout));
                ticketLayout.startAnimation(Utils.getHiddenAnimation(ticketLayout));
//                takeCarLayout.setVisibility(View.INVISIBLE);
//                ticketLayout.setVisibility(View.INVISIBLE);
                getComboData(false);
                break;
            case R.id.reserve_pay_with_ticket_layout:
                Bundle bundle = new Bundle();
                if (packetInfo != null){
                    bundle.putInt(MaintainEvent.payWithTicketId, packetInfo.get_id());
                }else {
                    bundle.putInt(MaintainEvent.payWithTicketId, 0);
                }
                MainRouter.getInstance(mContext).showActivityForResult(ModuleNames.Packet, MaintainReserveActivity.this, PacketUI.myPacket, Request_Ticket, bundle);
                break;
            case R.id.reserve_choice_time_layout:
                showDateTimeDialog();
                break;
            case R.id.reserve_show_choice_local:
                showChoiceWindow(1, localArray);
                break;
            case R.id.reserve_show_choice_number:
                showChoiceWindow(2, letterList);
                break;
            case R.id.maintain_reserve_button:
                Calendar choiceCalendar = Utils.fromyyyymmddhhmm(timeView.getText().toString());
                int choiceHour = choiceCalendar.get(Calendar.HOUR_OF_DAY);
                int choiceMinute = choiceCalendar.get(Calendar.MINUTE);
                if (choiceHour * 60 + choiceMinute > 17 * 60 + 30){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.reserve_time_over_17_30));
                    return;
                }
                uploadOrder();
                break;
        }
    }

    private void uploadOrder(){
        String inputName = nameView.getText().toString();
        if (StringUtils.isEmpty(inputName)){
            UIHelper.ToastMessage(mContext, R.string.please_input_you_name);
            return;
        }
        String inputPhone = phoneView.getText().toString();
        if (StringUtils.isEmpty(inputPhone)){
            UIHelper.ToastMessage(mContext, R.string.please_input_phone_num);
            return;
        }
        String orderTime = timeView.getText().toString();
        if (StringUtils.isEmpty(orderTime)){
            UIHelper.ToastMessage(mContext, R.string.please_input_order_time);
            return;
        }
        String licenseNumberString = inputLicense.getText().toString();
        if (StringUtils.isEmpty(licenseNumberString) || licenseNumberString.length() != 5){
            UIHelper.ToastMessage(mContext, R.string.please_input_right_car_license);
            return;
        }
        progressDialog.show(mContext, false);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", String.valueOf(sid));
        map.put("cid", String.valueOf(cid));
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        map.put("token", appContext.getActiveUser().getToken());
        map.put("plate", localView.getText().toString() + licenseView.getText().toString() + inputLicense.getText().toString());
        map.put("mileage", String.valueOf(kilo));
        map.put("select", select);
        map.put("optional", optional);
        if (onlinePay){
            if (wechatBox.isChecked()){
                map.put("pay", "WeChat");
            }else if (alipayBox.isChecked()){
                map.put("pay", "Alipay");
            }
            if (packetInfo != null){
                int[] coupons = new int[]{packetInfo.get_id()};
                map.put("coupons", Arrays.toString(coupons));
            }
            if (slideSwitch.getState()){
                map.put("door", String.valueOf(1));
                map.put("doorA", takeTime);
                map.put("doorB", takeAddress);
                map.put("doorC", backAddress);
            }else {
                map.put("door", String.valueOf(0));
            }
        }
        map.put("name", inputName);
        map.put("phone", inputPhone);
        map.put("date", orderTime);

        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.getMaintainOrder, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainReserveActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    if (!onlinePay){
                        MaintainRouter.getInstance(mContext).showActivity(MaintainUI.paySuccess);
                        AppManager appManager = AppManager.getAppManager();
                        appManager.finishActivity(MaintainMainActivity.class);
                        appManager.finishActivity(MaintainShopListActivity.class);
                        appManager.finishActivity(MaintainComboActivity.class);
                        finish();
                    }else {
                        AppConfig appConfig = AppConfig.getInstance(mContext);
                        appConfig.setOrderType(AppConfig.reserveOrder);
                        String orderId = object.optString("sn");
                        appConfig.setCurrentOrder(orderId);
                        float money = (float) object.optDouble("money");
                        WXPayTask task = new WXPayTask(mContext, msgApi, getResources().getString(R.string.reserve_product_describe), orderId, getProgressDialog());
                        task.execute(money);
                    }
                }else {
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MaintainReserveActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getRedPacket(){
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", appContext.getActiveUser().getToken());
        //用途（0、全部；1、技师专用, 2、保养券）
        map.put("to", String.valueOf(2));
        CheJSONObjectRequest request = new CheJSONObjectRequest(PacketURLs.myPacket_v2, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                MyPackets packets = MyPackets.fromJsonObject(object);
                if (packets != null){
                    ArrayList<PacketInfo> packetInfos = packets.getPacketInfos();
                    if (packetInfos != null && packetInfos.size() > 0){
                        ticketLayout.setOnClickListener(MaintainReserveActivity.this);
                        ticketView.setText(getResources().getString(R.string.reserve_no_use_ticket));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void buttonClickable(){
        payButton.setEnabled(false);
    }
}
