package com.ceyu.carsteward.user.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import  org.json.JSONObject;

/**
 * Created by chen on 15/6/11.
 */
public class BangSuggestActivity extends BaseActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.bang_suggest_and_back));
        setContentView(R.layout.bang_suggest_layout);
        findViewById(R.id.tv_bang_suggest_submit).setOnClickListener(this);
        findViewById(R.id.tv_bang_suggest_phonenum).setOnClickListener(this);
    }

    //点击提交
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_bang_suggest_submit:   //提交建议
                submitSuggest();
                break;
            case R.id.tv_bang_suggest_phonenum: //拨打客服电话
                phoneTheService();
                break;
        }
    }

    private void submitSuggest() {
        EditText editText = (EditText) findViewById(R.id.et_bang_suggest_editbox);
        String suggest = editText.getText().toString();
        if(illegalSuggest(suggest)){
            UIHelper.ToastMessage(this, R.string.bang_suggest_atleastfivewords);
        }else{
            AppContext appContext = (AppContext)getApplicationContext();
            Map<String, String> params = new HashMap<>();
            params.put("token", appContext.getActiveUser().getToken());
            params.put("info",suggest);
            CheJSONObjectRequest request = new CheJSONObjectRequest(UserURLs.suggest, params, this, this);
            appContext.queue().add(request);
        }
    }

    private boolean illegalSuggest(String editBody){
        return TextUtils.isEmpty(editBody) || editBody.length() < 5;
    }

    private void phoneTheService(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.wait_for_engineer_title))
                .setMessage(getString(R.string.bang_service_phonenum_servicenumber) + getString(R.string.bang_service_phonenum_formatted))
                .setPositiveButton(getString(R.string.confirm),
                        new DialogInterface.OnClickListener() { //确认拨打
                    @Override
                    public void onClick(DialogInterface dialog,  int which) {

                        Intent intent = new Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:" + getString(R.string.bang_service_phonenum)));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Utility.LogUtils.ex(volleyError);
        UIHelper.ToastMessage(this, R.string.http_connection_timeout);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        Utility.LogUtils.e("suggest", "response===>"+jsonObject.toString());
        if(Utility.errorCodeOk(jsonObject)){    //上传成功
            UIHelper.ToastMessage(this, R.string.bang_suggest_update_success);
            ((EditText)findViewById(R.id.et_bang_suggest_editbox)).setText("");
        }else{  //上传失败
            onErrorResponse(null);
        }
    }

}
