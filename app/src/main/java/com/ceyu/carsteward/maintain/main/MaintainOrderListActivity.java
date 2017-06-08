package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.maintain.adapter.MaintainOrderAdapter;
import com.ceyu.carsteward.maintain.bean.MaintainOrderList;
import com.ceyu.carsteward.maintain.bean.MaintainOrderListBean;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderListActivity extends BaseActivity {

    private PullToRefreshListView listView;
    private MaintainOrderAdapter adapter;
    private Context mContext;
    private int page = 1;
    private boolean isLoading = false;
    private MaintainOrderList data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_order_activity_layout);
        mContext = MaintainOrderListActivity.this;
        setTitle(R.string.reserve_my_orders);
        listView = (PullToRefreshListView) findViewById(R.id.maintain_order_list);
        adapter = new MaintainOrderAdapter(mContext);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLoading) {
                    return;
                }
                if (data != null && data.is_haveMore()) {
                    getOrderData(false);
                } else {
                    listView.onRefreshComplete(true);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    MaintainOrderListBean bean = (MaintainOrderListBean) adapter.getItem(position - 1);
                    if (bean != null) {
                        String orderClass = bean.get_class();
                        Bundle bundle = new Bundle();
                        bundle.putString(MaintainEvent.orderSN, bean.get_sn());
                        if (!StringUtils.isEmpty(orderClass)){
                            if (orderClass.equals(MaintainEvent.orderTuan)){
                                MainRouter.getInstance(mContext).showActivity(ModuleNames.Tuan, TuanUI.tuanOrderDetail, bundle);
                            }else if (orderClass.equals(MaintainEvent.orderMaintain)){
                                MaintainRouter.getInstance(mContext).showActivity(MaintainUI.getOrderInfo, bundle);
                            }else if (orderClass.equals(MaintainEvent.orderSelf)){
                                MainRouter.getInstance(mContext).showActivity(ModuleNames.Self, SelfUI.selfOrder, bundle);
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        listView.setVisibility(View.GONE);
        getOrderData(true);
        super.onResume();
    }

    private void getOrderData(final boolean firstPage){
        isLoading = true;
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", appContext.getActiveUser().getToken());
        page = (firstPage ? 1 : page + 1);
        map.put("page", String.valueOf(page));
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(MaintainURLs.getOrderList, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainOrderListActivity.this);
                listView.onRefreshComplete();
                isLoading = false;
                if (object.has(ResponseCode.ResponseState)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    MaintainOrderList orderList = MaintainOrderList.fromJson(object);
                    if (orderList != null){
                        listView.setVisibility(View.VISIBLE);
                        data = orderList;
                        adapter.setData(orderList.get_list(), firstPage);
                        adapter.notifyDataSetChanged();
                        if (!orderList.is_haveMore()){
                            listView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }else{
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                dismissDialog(MaintainOrderListActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();
    }
}
