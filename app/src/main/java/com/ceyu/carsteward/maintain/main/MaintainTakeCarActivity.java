package com.ceyu.carsteward.maintain.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.timepick.DateTimePickerDialog;
import com.ceyu.carsteward.common.ui.views.timepick.WheelMain;
import com.ceyu.carsteward.common.views.CommonDialog;

import java.util.Calendar;
import java.util.Locale;

import static com.ceyu.carsteward.R.id.take_car_agreement;

/**
 * Created by chen on 15/6/29.
 */
public class MaintainTakeCarActivity extends BaseActivity implements View.OnClickListener{

    private TextView maintainTime, takeTimeView, priceView;
    private EditText takeAddress, inputBackView;
    private LinearLayout backLayout;
    private LinearLayout agreementView;
    private Context mContext;
    private CheckBox checkBox;
    private Calendar orderCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_take_car_activity_layout);
        setTitle(R.string.home_take_back_car);
        mContext = MaintainTakeCarActivity.this;
        maintainTime = (TextView) findViewById(R.id.take_car_maintain_time);
        findViewById(R.id.take_car_time_layout).setOnClickListener(this);
        findViewById(R.id.back_car_address_same_layout).setOnClickListener(this);
        findViewById(R.id.take_car_button).setOnClickListener(this);
        takeTimeView = (TextView) findViewById(R.id.take_car_happen_time);
        priceView = (TextView) findViewById(R.id.take_car_price);
        backLayout = (LinearLayout) findViewById(R.id.back_car_address_layout);
        takeAddress = (EditText) findViewById(R.id.take_car_get_address);
        inputBackView = (EditText) findViewById(R.id.back_car_address_input);
        agreementView = (LinearLayout) findViewById(take_car_agreement);
        agreementView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(mContext, getResources().getString(R.string.take_car_agreement_title), getResources().getString(R.string.maintain_take_car_content));
                dialog.show();
            }
        });
        checkBox = (CheckBox) findViewById(R.id.take_back_same_address);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String orderTimeString = bundle.getString(MaintainEvent.maintainTime);
            orderCalendar = Utils.fromyyyymmddhhmm(orderTimeString);
            maintainTime.setText(orderTimeString);
            priceView.setText(bundle.getString(MaintainEvent.doorPrice));
            takeTimeView.setText(bundle.getString(MaintainEvent.takeCarTime));
            String takeCarAddress = bundle.getString(MaintainEvent.takeCarAddress);
            takeAddress.setText(takeCarAddress);
            String backCarAddress = bundle.getString(MaintainEvent.backCarAddress);
            if (!StringUtils.isEmpty(takeCarAddress) && !StringUtils.isEmpty(backCarAddress)){
                if (takeCarAddress.equals(backCarAddress)){
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                    backLayout.setVisibility(View.VISIBLE);
                    inputBackView.setText(backCarAddress);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.take_car_time_layout:
                showDialog();
                break;
            case R.id.back_car_address_same_layout:
                boolean isChecked = checkBox.isChecked();
                if (isChecked){
                    checkBox.setChecked(false);
                    backLayout.setVisibility(View.VISIBLE);
                }else {
                    checkBox.setChecked(true);
                    backLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.take_car_button:
                String takeTime = takeTimeView.getText().toString();
                if (StringUtils.isEmpty(takeTime)){
                    UIHelper.ToastMessage(mContext, R.string.please_choice_take_car_time);
                    return;
                }
                String takeAddressString = takeAddress.getText().toString();
                if (StringUtils.isEmpty(takeAddressString)){
                    UIHelper.ToastMessage(mContext, R.string.please_input_take_car_address);
                    return;
                }
                String backAddressString = inputBackView.getText().toString();
                if (!checkBox.isChecked()){
                    if (StringUtils.isEmpty(backAddressString)){
                        UIHelper.ToastMessage(mContext, R.string.please_input_back_car_address);
                        return;
                    }
                }else {
                    backAddressString = takeAddressString;
                }
                Intent intent = new Intent();
                intent.putExtra(MaintainEvent.takeCarTime, takeTime);
                intent.putExtra(MaintainEvent.takeCarAddress, takeAddressString);
                intent.putExtra(MaintainEvent.backCarAddress, backAddressString);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void showDialog() {
        Calendar calendar = Utils.fromyyyymmddhhmm(takeTimeView.getText().toString());
        final DateTimePickerDialog dialog = new DateTimePickerDialog(mContext, getResources().getString(R.string.please_choice_take_car_time_title), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), 0);
        dialog.setOnClickSetDateTimeListener(new DateTimePickerDialog.OnClickSetDateTimeListener() {
            @Override
            public void setDateTime(String dateTime) {
                if (validDate(orderCalendar, Utils.fromyyyymmddhhmm(dateTime))){
                    takeTimeView.setText(dateTime);
                }
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private boolean validDate(Calendar orderCalendar, Calendar takeCalendar){
        if (takeCalendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
            UIHelper.ToastMessage(mContext, R.string.reserve_order_time_notice);
            return false;
        }
        if (orderCalendar.getTimeInMillis() - takeCalendar.getTimeInMillis() < 60 * 60 * 1000){
            UIHelper.ToastMessage(mContext, R.string.take_time_late_service_time);
            return false;
        }
        return true;
    }
}
