package com.ceyu.carsteward.points.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.points.PointsURLs;
import com.ceyu.carsteward.points.bean.PointGoodsBean;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/10/13.
 */
public class PointBuyActivity extends BaseActivity {
    private TextView goodNameView, goodPointsView;
    private EditText userNameView, userPhoneView, userYoubianView, userAddressView;
    private TextView buttonView;
    private PointGoodsBean bean;
    private Context mContext;
    private User activerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_buy_activity_layout);
        setTitle(R.string.point_good_title);
        mContext = this;
        activerUser = ((AppContext)getApplicationContext()).getActiveUser();
        findView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            bean = bundle.getParcelable("points");
            if (bean != null){
                buttonView.setEnabled(true);
                goodNameView.setText(bean.get_subject());
                goodPointsView.setText(String.valueOf(bean.get_integral()));
            }
        }
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null){
                    String name = userNameView.getText().toString();
                    if (StringUtils.isEmpty(name)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.point_user_name_hint));
                        return;
                    }
                    String phone = userPhoneView.getText().toString();
                    if (StringUtils.isEmpty(phone)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.point_user_phone_hint));
                        return;
                    }
                    String address = userAddressView.getText().toString();
                    if (StringUtils.isEmpty(address)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.point_user_address_hint));
                        return;
                    }
                    submitInfo(bean.get_id(), name, phone, address);
                }
            }
        });
    }

    private void findView(){
        goodNameView = (TextView) findViewById(R.id.points_good_name_view);
        goodPointsView = (TextView) findViewById(R.id.points_good_points_view);
        userNameView = (EditText) findViewById(R.id.points_user_name_view);
        userPhoneView = (EditText) findViewById(R.id.points_user_phone_view);
        userYoubianView = (EditText) findViewById(R.id.points_user_youbian_view);
        userAddressView = (EditText) findViewById(R.id.points_user_address_view);
        buttonView = (TextView) findViewById(R.id.points_submit_button);
        userPhoneView.setText(activerUser.getPhoneNumber());
    }

    private void submitInfo(int sid, String name, String phone, String address){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activerUser.getToken());
        map.put("sid", String.valueOf(sid));
        map.put("name", name);
        map.put("phone", phone);
        map.put("address", address);
        CheJSONObjectRequest request = new CheJSONObjectRequest(PointsURLs.submitInfo, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(PointBuyActivity.this);
                String state = response.optString(ResponseCode.ResponseState);
                if (state != null && state.equals(ResponseCode.ResponseOK)){
                    UIHelper.ToastMessage(mContext, mContext.getResources().getString(R.string.point_buy_success));
                    finish();
                }else {
                    String info = response.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(PointBuyActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
