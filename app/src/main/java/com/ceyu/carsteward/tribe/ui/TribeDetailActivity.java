package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.tribe.adapter.TribeDetailAdapter;
import com.ceyu.carsteward.tribe.bean.TribeDetailBean;
import com.ceyu.carsteward.tribe.bean.TribeDetailList;
import com.ceyu.carsteward.tribe.bean.TribeDetailListBean;
import com.ceyu.carsteward.user.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/9/2.
 */
public class TribeDetailActivity extends BaseActivity {
    private TribeDetailAdapter adapter;
    private EditText editText;
    private Button replyButton;
    private Context mContext;
    private View rootView;
    private TribeDetailBean detailBean;
    private TribeDetailListBean tribeDetailListBean;
    private int detailId;
    private User activeUser;
    private int replyType = 1; //回复楼主：1   回复评论：2
    private boolean hadSupport = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tribe_detail_activity_layout);
        mContext = this;
        AppContext appContext = (AppContext) getApplicationContext();
        activeUser = appContext.getActiveUser();
        rootView = findViewById(R.id.tribe_detail_root_view);
        editText = (EditText) findViewById(R.id.tribe_detail_reply_txt);
        replyButton = (Button) findViewById(R.id.tribe_detail_reply_button);
        PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.tribe_detail_list);
        adapter = new TribeDetailAdapter(this, Utils.getScreenWidth(TribeDetailActivity.this));
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailId = bundle.getInt(TribeEvent.tribeDetailId);
            getData();
        }
        adapter.setOnReplyClickedListener(new TribeDetailAdapter.OnReplyClickListener() {
            @Override
            public void onReplyClick(TribeDetailListBean bean) {
                if (bean != null){
                    replyType = 2;
                    tribeDetailListBean = bean;
                    editText.setHint(getResources().getString(R.string.tribe_reply) + "@" + tribeDetailListBean.get_uname() + ":");
                }
            }
        });
        adapter.setOnMessageClickListener(new TribeDetailAdapter.OnMessageClickListener() {
            @Override
            public void onMessageClick() {
                replyType = 1;
                editText.setHint(getResources().getString(R.string.tribe_reply_default_hint));
                editText.requestFocus();
            }
        });
        adapter.setOnSupportClickListener(new TribeDetailAdapter.OnSupportClickListener() {
            @Override
            public void onSupportClick() {
                if (!hadSupport){
                    hadSupport = true;
                    support();
                    detailBean.set_good(detailBean.get_good() + 1);
                    adapter.setData(detailBean);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (StringUtils.isEmpty(content)) {
                    return;
                }
                if (detailBean == null) {
                    return;
                }

                if (replyType == 1) {
                    operateEdit(content, detailBean.get_uid());
                } else {
                    content = getResources().getString(R.string.tribe_reply) + "@" + tribeDetailListBean.get_uname() + ":" + content;
                    operateEdit(content, tribeDetailListBean.get_uid());
                }
            }
        });
        rootView.setVisibility(View.INVISIBLE);
    }

    private void getData(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("bid", String.valueOf(detailId));
        map.put("token", activeUser.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.getTribeDetail, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                rootView.setVisibility(View.VISIBLE);
                dismissDialog(TribeDetailActivity.this);
                detailBean = TribeDetailBean.fromJson(response);
                adapter.setData(detailBean);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(TribeDetailActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void reply(String txt, String toToken){
        HashMap<String, String> map = new HashMap<>();
        map.put("bid", String.valueOf(detailId));
        map.put("txt", txt);
        map.put("token", activeUser.getToken());
        if (detailBean != null){
            map.put("to", toToken);
        }
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.replyTribe, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has(ResponseCode.ResponseState)){
                    String state = response.optString(ResponseCode.ResponseState);
                    if (!ResponseCode.ResponseOK.equals(state)){
                        String info = response.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void support(){
        HashMap<String, String> map = new HashMap<>();
        map.put("bid", String.valueOf(detailId));
        map.put("token", activeUser.getToken());
        final CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.supportTribe, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String state = response.optString(ResponseCode.ResponseState);
                if (!ResponseCode.ResponseOK.equals(state)){
                    String info = response.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void operateEdit(String content, String uid){

        reply(content, uid);
        editText.setText("");
        editText.clearFocus();
        hideSoftInput();
        TribeDetailListBean bean = new TribeDetailListBean();
        bean.set_info(content);
        bean.set_date(getResources().getString(R.string.tribe_reply_moment));
        bean.set_upic(activeUser.getUserPic());
        bean.set_uid(activeUser.getToken());
        bean.set_uname(activeUser.getName());
        if (detailBean != null){
            TribeDetailList detailList = detailBean.getDetailList();
            if (detailList == null){
                detailList = new TribeDetailList(0, new ArrayList<TribeDetailListBean>());
            }
            ArrayList<TribeDetailListBean> oldBeans = detailList.getDetails();
            if (oldBeans == null){
                oldBeans = new ArrayList<>();
            }
            oldBeans.add(0, bean);
            detailList.set_num(detailList.get_num());
            detailList.setDetails(oldBeans);
            detailBean.setDetailList(detailList);
            adapter.setData(detailBean);
            adapter.notifyDataSetChanged();
        }
    }
}
