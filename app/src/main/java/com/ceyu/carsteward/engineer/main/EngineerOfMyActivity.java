package com.ceyu.carsteward.engineer.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.engineer.adapter.EngineerOfMyAdapter;
import com.ceyu.carsteward.engineer.bean.EngineerOrderInfo;
import com.ceyu.carsteward.engineer.bean.MyEngineers;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/6/11.
 */
public class EngineerOfMyActivity extends BaseActivity {

    private Context mContext;
    private int page = 1;
    private PullToRefreshListView listView;
    private EngineerOfMyAdapter adapter;
    private Dialog alertDialog;
    private MyEngineers mEngineers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.engineer_of_my);
        setContentView(R.layout.engineer_of_my_activity_layout);
        mContext = EngineerOfMyActivity.this;
        listView = (PullToRefreshListView) findViewById(R.id.engineer_list_of_mine);
        adapter = new EngineerOfMyAdapter(mContext, requestQueue);
        adapter.setOnStateButtonClickedListener(new EngineerOfMyAdapter.OnStateButtonClickListener() {
            @Override
            public void onStateClickedListener(EngineerOrderInfo info) {
                if (info != null) {
                    if (info.get_orderStateCode() == 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(EngineerEvent.ENGINEER_TOKEN, info.get_token());
                        EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerPay, bundle);
                    } else {
                        callToEngineer(info.get_sn());
                    }
                }
            }
        });
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mEngineers.isHaveNext()) {
                    getEngineerOfMy(true);
                }
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("chen", "position: " + position);
                if (position > 0){
                    EngineerOrderInfo orderInfo = (EngineerOrderInfo) adapter.getItem(position - 1);
                    if (orderInfo != null){
                        Bundle bundle = new Bundle();
                        bundle.putString(EngineerEvent.ENGINEER_TOKEN, orderInfo.get_token());
                        EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerInfo, bundle);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        listView.setVisibility(View.INVISIBLE);
        getEngineerOfMy(false);
        if (alertDialog != null){
            if (alertDialog.isShowing() && !isFinishing()){
                alertDialog.dismiss();
            }
        }
        super.onResume();
    }

    private void getEngineerOfMy(final boolean nextPage){
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", appContext.getActiveUser().getToken());
        if (nextPage){
            page += 1;
        }
        map.put("page", String.valueOf(page));
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerOfMy, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(EngineerOfMyActivity.this);
                if (object.has(ResponseCode.ResponseState)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    MyEngineers myEngineers = MyEngineers.fromJsonObject(object);
                    if (myEngineers != null){
                        listView.setVisibility(View.VISIBLE);
                        mEngineers = myEngineers;
                        adapter.setData(myEngineers.getInfos(), !nextPage);
                        adapter.notifyDataSetChanged();
                        boolean hasNext = myEngineers.isHaveNext();
                        if (!hasNext){
                            listView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(EngineerOfMyActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void callToEngineer(String sn){
        showWaitDialog();
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("sn", sn);
        map.put("token", appContext.getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.callToEngineer, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (object.has(ResponseCode.ResponseState)){
                    String state = object.optString(ResponseCode.ResponseState);
                    if (!StringUtils.isEmpty(state)){
                        if (!state.equals(ResponseCode.ResponseOK)){
                            if (alertDialog != null){
                                if (alertDialog.isShowing() && !isFinishing()){
                                    alertDialog.dismiss();
                                }
                            }
                            String info = object.optString(ResponseCode.ResponseInfo);
                            UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                        }
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
    private void showWaitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.wait_for_engineer_title);
        builder.setMessage(getResources().getString(R.string.wait_for_engineer_content));
        alertDialog = builder.show();
    }
}
