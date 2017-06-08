package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.FileUtils;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/8/27.
 */
public class TribePublishActivity extends BaseActivity {
    private Context mContext;
    private String imageString;
    private EditText txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tribe_action_bar_title);
        setContentView(R.layout.tribe_public_activity_layout);
        txtView = (EditText) findViewById(R.id.tribe_publish_content_id);
        RoundCornerImageView imageView = (RoundCornerImageView) findViewById(R.id.tribe_publish_image_id);
        mContext = TribePublishActivity.this;
        setRightTitle(getResources().getString(R.string.tribe_publish), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(imageString)){
                    return;
                }
                String txt = txtView.getText().toString().trim();
                if (StringUtils.isEmpty(txt)){
                    return;
                }
                publishTribe();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String path = bundle.getString(TribeEvent.publishImagePath);
            Bitmap bitmap = FileUtils.getInstance(this).getPublishBitmap(path);
            if (bitmap != null){
                imageView.setImageBitmap(bitmap);
                imageString = Utils.bitmapToString(bitmap);
            }
        }
    }

    private void publishTribe(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        map.put("token", appContext.getActiveUser().getToken());
        map.put("pic", imageString);
        map.put("ext", "png");
        map.put("txt", txtView.getText().toString());
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.publishTribe, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(TribePublishActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.tribe_publish_success));
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
                dismissDialog(TribePublishActivity.this);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }


}
