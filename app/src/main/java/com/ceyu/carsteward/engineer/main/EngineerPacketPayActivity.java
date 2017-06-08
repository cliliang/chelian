package com.ceyu.carsteward.engineer.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.config.AppConfig;
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
import com.ceyu.carsteward.engineer.adapter.PacketPayAdapter;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.packet.bean.MyPackets;
import com.ceyu.carsteward.packet.bean.PacketInfo;
import com.ceyu.carsteward.packet.main.PacketURLs;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/11.
 */
public class EngineerPacketPayActivity extends BaseActivity {

    private Context mContext;
//    private TextView sumView;
    private PacketPayAdapter adapter;
    private ListView listView;
    private ProgressDialog progressDialog;
    private MyPackets myPackets;
    private User activeUser;
    private String engineerToken;
    private ArrayList<Integer> packetList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_packet_pay_activity_layout);
        setTitle(getResources().getString(R.string.user_my_packet));
        setRightTitle(getResources().getString(R.string.engineer_bang_right_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(mContext, getResources().getString(R.string.red_packet_use_describe), getResources().getString(R.string.engineer_packet_use_rule));
                dialog.show();
            }
        });
        mContext = EngineerPacketPayActivity.this;
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            engineerToken = bundle.getString(EngineerEvent.ENGINEER_TOKEN);
            if (StringUtils.isEmpty(engineerToken)) {
                finish();
            }
        } else {
            finish();
        }

        Button payButton = (Button) findViewById(R.id.engineer_packet_pay_button);
        listView = (ListView) findViewById(R.id.engineer_packet_pay_list);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.my_red_packet_top_view, null);
//        sumView = (TextView) headerView.findViewById(R.id.my_total_red_packed_money);
//        listView.addHeaderView(headerView);

        adapter = new PacketPayAdapter(mContext);
        listView.setAdapter(adapter);
        progressDialog = ProgressDialog.getInstance();
        progressDialog.show(mContext);
        getRedPacket();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PacketInfo packetInfo = (PacketInfo) adapter.getItem(position);
                if (packetInfo != null && packetInfo.is_usable()){
                    int packetId = packetInfo.get_id();
                    if (packetList.contains(packetId)) {
                        packetList.remove(Integer.valueOf(packetId));
                    } else {
                        packetList.add(packetId);
                    }
                    adapter.setCheckedIds(packetList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (packetList.size() > 0){
                    payByPacket();
                }else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_choice_red_packet));
                }
            }
        });
    }

    private void getRedPacket() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        //用途（0、全部；1、技师专用）
        map.put("to", String.valueOf(1));
        CheJSONObjectRequest request = new CheJSONObjectRequest(PacketURLs.myPacket_v2, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseError)) {
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                } else {
                    MyPackets packets = MyPackets.fromJsonObject(object);
                    if (packets != null) {
                        myPackets = packets;
                        setViewData(myPackets);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void payByPacket() {
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("mtoken", engineerToken);
        map.put("token", activeUser.getToken());
        map.put("coupons", packetList.toString());
        map.put("pay", "WeChat");
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerPayId, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                if (object != null){
                    float moneyMore = (float) object.optDouble("money");
                    if (moneyMore == 0){
                        //remember order sn
                        AppConfig config = AppConfig.getInstance(mContext);
                        String sn = object.optString("sn");
                        config.setCurrentOrder(sn);

                        Bundle bundle = new Bundle();
                        bundle.putInt(EngineerEvent.payResult, 0);
                        bundle.putString(EngineerEvent.ENGINEER_TOKEN, engineerToken);
                        EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerInfo, bundle);
                        finish();
                    }else {
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.weixin_pay_fail));
                    }
                }else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.weixin_pay_fail));
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

    private void setViewData(MyPackets packets) {
        if (packets != null) {
            listView.setVisibility(View.VISIBLE);
            adapter.setData(packets.getPacketInfos());
            adapter.notifyDataSetChanged();
        }
    }

    private void dismissDialog() {
        if (progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }
}
