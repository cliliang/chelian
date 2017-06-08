package com.ceyu.carsteward.engineer.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
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
import com.ceyu.carsteward.engineer.bean.EngineerOrderInfo;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/6/18.
 */
public class EngineerCommentActivity extends BaseActivity {


    private CircleHeadImageView headImageView;
    private TextView nameView, levelView, brandView;
    private RatingBar ratingBar;
    private EditText editText;
    private Button mButton;
    private ProgressDialog progressDialog;
    private Context mContext;
    private EngineerOrderInfo info;
    private String sn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_comment_layout);
        setTitle(R.string.do_comment_for_engineer);
        initView();
        mContext = EngineerCommentActivity.this;
        progressDialog = ProgressDialog.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            info = bundle.getParcelable(EngineerEvent.orderInfo);
            if (info != null){
                sn = info.get_sn();
                CheImageLoader loader = new CheImageLoader(requestQueue, mContext);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(headImageView, R.mipmap.icon_my, R.mipmap.icon_my);
                loader.get(info.get_pic(), imageListener);
                nameView.setText(info.get_name());
                levelView.setText(info.get_level());
                brandView.setText(info.get_model());
            }else {
                finish();
            }
        }else {
            finish();
        }
    }

    private void initView(){
        headImageView = (CircleHeadImageView) findViewById(R.id.engineer_common_of_my_head);
        nameView = (TextView) findViewById(R.id.engineer_common_of_my_name);
        levelView = (TextView) findViewById(R.id.engineer_common_of_my_level);
        brandView = (TextView) findViewById(R.id.engineer_common_of_my_brand);
        ratingBar = (RatingBar) findViewById(R.id.user_input_raking_star);
        editText = (EditText) findViewById(R.id.user_input_comment_id);
        mButton = (Button) findViewById(R.id.engineer_commit_comment);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }

    private void dismissDialog(){
        if (progressDialog.isShowing() && !isFinishing()){
            progressDialog.dismiss();
        }
    }

    private void submitComment(){
        float rating = ratingBar.getRating();
        if (rating == 0){
            UIHelper.ToastMessage(mContext, R.string.submit_choice_star);
            return;
        }
        String comment = editText.getText().toString().trim();
        if (StringUtils.isEmpty(comment)){
            UIHelper.ToastMessage(mContext, R.string.submit_input_comment);
            return;
        }
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();;
        map.put("mtoken", info.get_token());
        map.put("token", appContext.getActiveUser().getToken());
        map.put("info", comment);
        map.put("assess", String.valueOf(rating));
        map.put("sn", sn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.submitComment, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.commit_comment_success));
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
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
