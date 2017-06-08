package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/9/23.
 */
public class SelfCommentActivity extends BaseActivity {
    private String mToken, orderSn;
    private Context mContext;
    private User activeUser;
    private EditText commentView;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_comment_activity_layout);
        setRightTitle(getResources().getString(R.string.tribe_publish), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = commentView.getText().toString();
                if (StringUtils.isEmpty(content)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_comment_content_empty));
                    return;
                }
                float rating = ratingBar.getRating();
                if (rating == 0){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_comment_rating));
                    return;
                }
                uploadComment(content, rating);
            }
        });
        mContext = this;
        activeUser = ((AppContext)getApplicationContext()).getActiveUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mToken = bundle.getString(MaintainEvent.mechanicToken);
            orderSn = bundle.getString(MaintainEvent.orderSN);
        }
        commentView = (EditText) findViewById(R.id.self_input_comment_text);
        ratingBar = (RatingBar) findViewById(R.id.self_input_raking_star);
    }

    private void uploadComment(String info, float assess){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("mtoken", mToken);
        map.put("info", info);
        map.put("assess", String.valueOf(assess));
        map.put("sn", orderSn);
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.uploadComment, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(SelfCommentActivity.this);
                String state = response.optString(ResponseCode.ResponseState);
                if (ResponseCode.ResponseOK.equals(state)){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_comment_publish_success));
                    finish();
                }else {
                    String info = ErrorCode.getInstance(mContext).getErrorCode(response.optString(ResponseCode.ResponseInfo));
                    UIHelper.ToastMessage(mContext, info);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(SelfCommentActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
