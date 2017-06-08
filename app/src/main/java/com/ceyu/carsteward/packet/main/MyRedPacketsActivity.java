package com.ceyu.carsteward.packet.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.common.views.CommonDialog;
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.packet.adapter.MyPacketAdapter;
import com.ceyu.carsteward.packet.bean.MyPackets;
import com.ceyu.carsteward.packet.bean.PacketInfo;
import com.ceyu.carsteward.user.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/6/8.
 */
public class MyRedPacketsActivity extends BaseActivity {
    private ListView listView;
//    private TextView sumView;
    private Context mContext;
    private MyPackets myPackets;
    private MyPacketAdapter adapter;
    private LinearLayout container;
    private String ruleContent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_red_packet_activity_layout);
        setTitle(R.string.user_my_packet);
        listView = (ListView) findViewById(R.id.my_total_red_packet_list);
        mContext = MyRedPacketsActivity.this;
        container = (LinearLayout) findViewById(R.id.red_packet_container_view);
        boolean fromMaintain = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            fromMaintain = true;
            ruleContent = getResources().getString(R.string.maintain_red_packet_rule);
            int ticketId = bundle.getInt(MaintainEvent.payWithTicketId, 0);
            adapter = new MyPacketAdapter(mContext, true);
            adapter.setChoiceId(ticketId);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position < adapter.getCount()){
                        PacketInfo packetInfo = (PacketInfo) adapter.getItem(position);
                        if (packetInfo != null && packetInfo.is_usable()){
                            Intent i = new Intent();
                            i.putExtra(MaintainEvent.payTicket, packetInfo);
                            setResult(RESULT_OK, i);
                            adapter.setChoiceId(packetInfo.get_id());
                            adapter.notifyDataSetChanged();
                            finish();
                        }
                    }
                }
            });
        }else {
            ruleContent = getResources().getString(R.string.red_packet_rule_describe);
            View headerView = LayoutInflater.from(mContext).inflate(R.layout.my_red_packet_top_view, null);
            final EditText codeText = (EditText) headerView.findViewById(R.id.packet_exchange_edit);
            TextView buttonView = (TextView) headerView.findViewById(R.id.packet_exchange_button);
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = codeText.getText().toString();
                    if (!StringUtils.isEmpty(input)) {
                        exchangeCode(input);
                    } else {
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_input_exchange_not_empty));
                    }
                }
            });
            listView.addHeaderView(headerView);
            adapter = new MyPacketAdapter(mContext, false);
        }
        listView.setAdapter(adapter);
        if (fromMaintain){
            getRedPacket(2);
        }else {
            getRedPacket(0);
        }
        setRightTitle(getResources().getString(R.string.engineer_bang_right_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(mContext, getResources().getString(R.string.red_packet_use_describe), ruleContent);
                dialog.show();
            }
        });
    }

    private void getRedPacket(int type){
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", appContext.getActiveUser().getToken());
        //用途（0、全部；1、技师专用, 2、保养券）
        map.put("to", String.valueOf(type));
        CheJSONObjectRequest request = new CheJSONObjectRequest(PacketURLs.myPacket_v2, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MyRedPacketsActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseError)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    MyPackets packets = MyPackets.fromJsonObject(object);
                    if (packets != null){
                        myPackets = packets;
                        setViewData(myPackets);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MyRedPacketsActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(MyPackets packets){
        if (packets != null){
            listView.setVisibility(View.VISIBLE);
            adapter.setData(packets.getPacketInfos());
            adapter.notifyDataSetChanged();
        }
    }

    private void exchangeCode(String code){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        User user = appContext.getActiveUser();
        map.put("code", code);
        map.put("token", user.getToken());
        long current = System.currentTimeMillis() / 1000;
        map.put("code1", String.valueOf(current));
        String spellString = user.getPhoneNumber() + String.valueOf(current * 2 - 1);
        map.put("code2", StringUtils.MD5(spellString));
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(PacketURLs.exchangeCode, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MyRedPacketsActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_exchange_success));
                        getRedPacket(0);
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MyRedPacketsActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();

    }

}
