package com.ceyu.carsteward.car.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.adapter.ChoiceGridAdapter;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.bean.InsuranceBean;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
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
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/16.
 */
public class AddCarMileageActivity extends BaseActivity implements View.OnClickListener {

    private TextView yearView,modelView, localView, licenseView, buyCarView, insuranceView, insuranceDateView,deleteButton1, deleteButton2;
    private LinearLayout choiceLocalLayout, showAllLayout, shouldHindLayout;
    private EditText inputLicenseView, inputFrameView, inputEngineerView, inputOwnView, inputKiloView;
    private ImageView yearArrow, modelArrow;
    private Context mContext;
    private AppConfig appConfig;
    private final int requestInsurance = 11;
    private CarInfoBean carInfoBean;
    private int detailId;
    private User activeUser;
    private AppContext appContext;
    private String[] localArray;
    private String[] letterList;
    private Bundle bundle;
    private boolean fromHome;
    private boolean fromTuan = false;
    private boolean fromBreakRule = false;
    private HashMap<String, String> modify = new HashMap<>();
    private boolean editCarInfo = false;
    private InsuranceBean insuranceBean = new InsuranceBean(0, "", "");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_mileage_activity_layout);
        bundle = getIntent().getExtras();
        if (bundle != null){
            findView();
            int yearId = bundle.getInt(CarEvent.carYearId);
            detailId = bundle.getInt(CarEvent.carDetailId);
            String detailName = bundle.getString(CarEvent.carDetailName);
            String title = bundle.getString(CarEvent.carSeriesName);
            setTitle(title);
            modelView.setText(detailName);
            yearView.setText(String.valueOf(yearId));

            CarInfoBean infoBean = bundle.getParcelable(CarEvent.carBean);
            if (infoBean != null){
                editCarInfo = true;
                carInfoBean = infoBean;
                fromHome = bundle.getBoolean(CarEvent.fromHomeEdit, false);
                initEditView();
            }
            fromTuan = bundle.getBoolean(TuanEvent.fromTuan);
            fromBreakRule = bundle.getBoolean(CarEvent.fromBreakRule);
            if (fromBreakRule){

            }
        }else {
            return;
        }
        setRightTitle(getResources().getString(R.string.save), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editCarInfo){
                    saveContent();
                }else {
                    saveEditContent();
                }

            }
        });
        mContext = AddCarMileageActivity.this;
        appContext = (AppContext)mContext.getApplicationContext();
        activeUser = appContext.getActiveUser();
        localArray = getResources().getStringArray(R.array.province_list);
        letterList = getResources().getStringArray(R.array.letter_list);
        appConfig = AppConfig.getInstance(mContext);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        bundle = intent.getExtras();
        if (bundle != null){
            int yearId = bundle.getInt(CarEvent.carYearId);
            detailId = bundle.getInt(CarEvent.carDetailId);
            String detailName = bundle.getString(CarEvent.carDetailName);
            String title = bundle.getString(CarEvent.carSeriesName);
            setTitle(title);
            modelView.setText(detailName);
            yearView.setText(String.valueOf(yearId));
        }
        super.onNewIntent(intent);
    }

    private void findView(){
        yearView = (TextView) findViewById(R.id.add_car_year_id);
        modelView = (TextView) findViewById(R.id.add_car_model_id);
        localView = (TextView) findViewById(R.id.add_car_choice_local_text);
        licenseView = (TextView) findViewById(R.id.add_car_choice_license_text);
        buyCarView = (TextView) findViewById(R.id.add_car_choice_buy_car_time);
        insuranceView = (TextView) findViewById(R.id.add_car_choice_insurance_company);
        insuranceDateView = (TextView) findViewById(R.id.add_car_choice_buy_insurance_time);

        yearArrow = (ImageView) findViewById(R.id.add_car_year_arrow);
        modelArrow = (ImageView) findViewById(R.id.add_car_model_arrow);

        shouldHindLayout = (LinearLayout) findViewById(R.id.add_car_should_hind_view);
        choiceLocalLayout = (LinearLayout) findViewById(R.id.add_car_choice_local);
        View choiceLicenseLayout = findViewById(R.id.add_car_choice_license);
        View resetYearLayout = findViewById(R.id.reset_car_year);
        View resetModelLayout = findViewById(R.id.reset_car_model);
        showAllLayout = (LinearLayout) findViewById(R.id.add_car_show_all_id);
        View hindAllLayout = findViewById(R.id.add_car_hind_all_layout);
        View showEngineerView = findViewById(R.id.add_car_show_engineer_demo);
        View showFrameView = findViewById(R.id.add_car_show_frame_demo);
        deleteButton1 = (TextView) findViewById(R.id.add_car_delete_button1);
        deleteButton1.setOnClickListener(this);
        deleteButton2 = (TextView) findViewById(R.id.add_car_delete_button2);
        deleteButton2.setOnClickListener(this);

        choiceLicenseLayout.setOnClickListener(this);
        choiceLocalLayout.setOnClickListener(this);
        showAllLayout.setOnClickListener(this);
        hindAllLayout.setOnClickListener(this);
        showFrameView.setOnClickListener(this);
        showEngineerView.setOnClickListener(this);
        buyCarView.setOnClickListener(this);
        insuranceDateView.setOnClickListener(this);
        insuranceView.setOnClickListener(this);
        resetModelLayout.setOnClickListener(this);
        resetYearLayout.setOnClickListener(this);


        inputLicenseView = (EditText) findViewById(R.id.add_car_input_license);
        inputFrameView = (EditText) findViewById(R.id.add_car_input_frame);
        inputEngineerView = (EditText) findViewById(R.id.add_car_input_engineer);
        inputOwnView = (EditText) findViewById(R.id.add_car_input_own_name);
        inputKiloView = (EditText) findViewById(R.id.add_car_input_kilo);

        inputLicenseView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String licenseString = s.toString();
                if (!StringUtils.isEmpty(licenseString) && licenseString.length() == 5){
                    hideSoftInput();
                    modify.put("plate", localView.getText().toString() + licenseView.getText().toString() + inputLicenseView.getText().toString());
                }
            }
        });
        inputFrameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String frameString = s.toString().trim();
                if (!StringUtils.isEmpty(frameString) && frameString.length()  == 6){
                    hideSoftInput();
                    modify.put("frame", frameString);
                }
            }
        });
        inputEngineerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String motorString = s.toString().trim();
                if (!StringUtils.isEmpty(motorString)){
                    modify.put("motor", motorString);
                }
            }
        });
    }

    private void initEditView(){
        yearArrow.setVisibility(View.GONE);
        modelArrow.setVisibility(View.GONE);
        deleteButton1.setVisibility(fromHome ? View.VISIBLE : View.GONE);
        deleteButton2.setVisibility(View.GONE);
        String plate = carInfoBean.get_plate();
        if (!StringUtils.isEmpty(plate) && plate.length() == 7){
            localView.setText(String.valueOf(plate.charAt(0)));
            licenseView.setText(String.valueOf(plate.charAt(1)));
            inputLicenseView.setText(plate.substring(2, plate.length()));
        }
        inputFrameView.setText(carInfoBean.get_frame());
        inputEngineerView.setText(carInfoBean.get_motor());
        buyCarView.setText(carInfoBean.get_buyDate());
        insuranceView.setText(carInfoBean.get_insurName());
        insuranceDateView.setText(carInfoBean.get_insurDate());
        insuranceBean.set_id(carInfoBean.get_insurId());
        inputOwnView.setText(carInfoBean.get_name());
        inputKiloView.setText(carInfoBean.get_mileage());

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.add_car_choice_local:
                showChoiceWindow(1, localArray);
                break;
            case R.id.add_car_choice_license:
                showChoiceWindow(2, letterList);
                break;
            case R.id.add_car_show_all_id:
                shouldHindLayout.setVisibility(View.VISIBLE);
                showAllLayout.setVisibility(View.GONE);
                deleteButton1.setVisibility(View.GONE);
                deleteButton2.setVisibility(fromHome ? View.VISIBLE : View.GONE);
                break;
            case R.id.add_car_hind_all_layout:
                shouldHindLayout.setVisibility(View.INVISIBLE);
                showAllLayout.setVisibility(View.VISIBLE);
                deleteButton1.setVisibility(fromHome ? View.VISIBLE : View.GONE);
                deleteButton2.setVisibility(View.GONE);
                break;
            case R.id.add_car_show_engineer_demo:
                showPhotoDialog();
                break;
            case R.id.add_car_show_frame_demo:
                showPhotoDialog();
                break;
            case R.id.add_car_choice_buy_car_time:
                showDateDialog(1, Utils.fromYYYYMMDD(buyCarView.getText().toString()));
                break;
            case R.id.add_car_choice_insurance_company:
                CarRouter.getInstance(mContext).showActivityForResult(AddCarMileageActivity.this, CarUI.choiceInsurance, requestInsurance, null);
                break;
            case R.id.add_car_choice_buy_insurance_time:
                showDateDialog(2, Utils.fromYYYYMMDD(insuranceDateView.getText().toString()));
                break;
            case R.id.reset_car_model:
                if (!editCarInfo){
                    CarRouter.getInstance(mContext).showActivity(CarUI.addCarDetail, bundle);
                }
                break;
            case R.id.reset_car_year:
                if (!editCarInfo){
                    CarRouter.getInstance(mContext).showActivity(CarUI.addCarYear, bundle);
                }
                break;
            case R.id.add_car_delete_button2:
            case R.id.add_car_delete_button1:
                deleteCar();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestInsurance && resultCode == RESULT_OK){
            if (data != null){
                InsuranceBean bean = data.getParcelableExtra(CarEvent.insuranceBean);
                if (bean != null){
                    insuranceBean = bean;
                    insuranceView.setText(insuranceBean.get_name());
                    modify.put("inscl", String.valueOf(insuranceBean.get_id()));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    modify.put("plate", localView.getText().toString() + licenseView.getText().toString() + inputLicenseView.getText().toString());
                }
                if (window.isShowing() && !AddCarMileageActivity.this.isFinishing()) {
                    window.dismiss();
                }
            }
        });
        View hindView = contentView.findViewById(R.id.window_hide_layout_id);
        hindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window.isShowing() && !AddCarMileageActivity.this.isFinishing()) {
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
        window.showAtLocation(choiceLocalLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showDateDialog(final int type, Date dateRes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRes);
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (Utils.fromYMD(year, monthOfYear, dayOfMonth).getTime() <= Calendar.getInstance().getTime().getTime()){
                    Calendar choiceCalendar = Calendar.getInstance();
                    choiceCalendar.set(year, monthOfYear, dayOfMonth);
                    Date date = choiceCalendar.getTime();
                    String dateString = String.format(Locale.US, "%tF", date);
                    if (type == 1){
                        buyCarView.setText(dateString);
                        modify.put("buyda", dateString);
                    }else {
                        insuranceDateView.setText(dateString);
                        modify.put("insda", dateString);
                    }
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveContent(){
        if (!invalidValue()){
            return;
        }
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("model", String.valueOf(detailId));
        map.put("plate", localView.getText().toString() + licenseView.getText().toString() + inputLicenseView.getText().toString().trim());
        map.put("frame", inputFrameView.getText().toString());
        map.put("motor", inputEngineerView.getText().toString());
        map.put("inscl", String.valueOf(insuranceBean.get_id()));
        map.put("insda", insuranceDateView.getText().toString());
        map.put("buyda", buyCarView.getText().toString());
        map.put("name", inputOwnView.getText().toString());
        map.put("mileage", inputKiloView.getText().toString());
        CheJSONObjectRequest request = new CheJSONObjectRequest(CarURLs.saveCarInfo, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dismissDialog(AddCarMileageActivity.this);
                String state = jsonObject.optString(ResponseCode.ResponseState);
                if (ResponseCode.ResponseOK.equals(state)){
                    UIHelper.ToastMessage(mContext, R.string.save_car_info_success);
                    try{
                        AppManager.getAppManager().finishActivity(AddCarBrandActivity.class);
                        AppManager.getAppManager().finishActivity(AddCarYearActivity.class);
                        AppManager.getAppManager().finishActivity(AddCarDetailActivity.class);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    appConfig.setRefreshCar(true);
                    if (fromTuan){
                        AppManager.getAppManager().finishActivity(CarOfMineActivity.class);
//                        setResult(TuanEvent.addCar);
                        Bundle bundle = new Bundle();
                        bundle.putInt(TuanEvent.addCarIntent, TuanEvent.addCar);
                        MainRouter.getInstance(mContext).showActivity(ModuleNames.Tuan, TuanUI.tuanReserve, bundle);
                        finish();
                    }
                    showSaveDialog();
                }else {
                    String info = jsonObject.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarMileageActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void saveEditContent(){
        if (!invalidValue()){
            return;
        }
        progressDialog.show(mContext);
        User user = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        modify.put("token", user.getToken());
        modify.put("cid", String.valueOf(carInfoBean.get_id()));
        modify.put("name", inputOwnView.getText().toString());
        modify.put("mileage", inputKiloView.getText().toString());
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(CarURLs.modifyCar, modify, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(AddCarMileageActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        UIHelper.ToastMessage(mContext, R.string.save_car_info_success);
                        finish();
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarMileageActivity.this);
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();
    }

    private boolean invalidValue(){
        String licenseString = inputLicenseView.getText().toString().trim();
        if (StringUtils.isEmpty(licenseString)){
            UIHelper.ToastMessage(mContext, R.string.please_input_right_plate_number);
            return false;
        }
        if (licenseString.length() != 5){
            UIHelper.ToastMessage(mContext, R.string.please_input_right_plate_number);
            return false;
        }
        if (fromBreakRule){
            String frameString = inputFrameView.getText().toString().trim();
            if (StringUtils.isEmpty(frameString) || frameString.length() != 6){
                UIHelper.ToastMessage(mContext, R.string.input_car_frame_number);
                return false;
            }
            String engineerString = inputEngineerView.getText().toString().trim();
            if (StringUtils.isEmpty(engineerString)){
                UIHelper.ToastMessage(mContext, R.string.input_car_engineer_number);
                return false;
            }
        }
        return true;
    }

    private void showPhotoDialog(){
        final Dialog dialog = new Dialog(mContext, R.style.showPhoto);
        View view = LayoutInflater.from(mContext).inflate(R.layout.engineer_show_photo_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.engineer_show_photo_dialog_id);
        imageView.setImageResource(R.mipmap.default_engineer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing() && !AddCarMileageActivity.this.isFinishing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void deleteCar(){
        progressDialog.show(mContext);
        progressDialog = ProgressDialog.getInstance();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("cid", String.valueOf(carInfoBean.get_id()));
        CheJSONObjectRequest request = new CheJSONObjectRequest(CarURLs.deleteCar, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(AddCarMileageActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        appConfig.setRefreshCar(true);
                        CarInfoBean nowCarInfo = appContext.getCarInfo();
                        if (nowCarInfo != null){
                            int defaultId = nowCarInfo.get_id();
                            if (carInfoBean.get_id() == defaultId){
                                appContext.setCarInfo(null);
                            }
                        }
                        UIHelper.ToastMessage(mContext, R.string.delete_car_success);
                        finish();
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarMileageActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void showSaveDialog(){
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.user_add_car_title)
                .setMessage(R.string.user_add_car_message)
                .setNegativeButton(R.string.user_add_car_know, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setPositiveButton(R.string.user_add_car_go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CarEvent.shopClass, 1);
                        MainRouter.getInstance(mContext).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain, bundle);
                        finish();
                    }
                }).create();
        dialog.show();
    }
}
