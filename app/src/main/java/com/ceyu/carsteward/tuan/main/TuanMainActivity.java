package com.ceyu.carsteward.tuan.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.tuan.adapter.TuanHomeListAdapter;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.bean.TuanMainBean;
import com.ceyu.carsteward.tuan.router.TuanRouter;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.ceyu.carsteward.user.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ceyu.carsteward.R.id.tuan_list_order_by_sale;

/**
 * Created by chen on 15/7/20.
 */
public class TuanMainActivity extends BaseActivity {

    private TextView moneyView, saleView;
    private TuanHomeListAdapter adapter;
    private PullToRefreshListView listView;
    private Context mContext;
    private TuanMainBean mainBean;
    private List<TuanListBean> tempList = new ArrayList<>();
    private User activeUser;
    private boolean isLoading;
    private int page = 1;
    private int type = 0;  //0:推荐度 1：销量；2：价格
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tuan_facade_title);
        setContentView(R.layout.tuan_main_activity_layout);
        mContext = TuanMainActivity.this;
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        listView = (PullToRefreshListView) findViewById(R.id.tuan_main_list_id);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLoading) {
                    return;
                }
                if (mainBean != null && mainBean.getHaveNext()) {
                    getData(false);
                } else {
                    listView.onRefreshComplete(true);
                }
            }
        });
        adapter = new TuanHomeListAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    TuanListBean tuanListBean = (TuanListBean) adapter.getItem(position - 1);
                    if (tuanListBean != null){
                        Bundle bundle = new Bundle();
                        bundle.putString(TuanEvent.shopId, tuanListBean.get_gid());
                        TuanRouter.getInstance(mContext).showActivity(TuanUI.tuanContent, bundle);
                    }
                }

            }
        });
        moneyView = (TextView) findViewById(R.id.tuan_list_order_by_money);
        saleView = (TextView) findViewById(tuan_list_order_by_sale);
        moneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                tempList.clear();
                moneyView.setTextColor(getResources().getColor(R.color.orange));
                saleView.setTextColor(getResources().getColor(R.color.text_hint));
                getData(true);
            }
        });
        saleView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                type = 1;
                tempList.clear();
                moneyView.setTextColor(getResources().getColor(R.color.text_hint));
                saleView.setTextColor(getResources().getColor(R.color.orange));
                getData(true);
            }
        });
        getData(true);
    }

    private void getData(boolean reload){
        isLoading = true;
        if (reload){
            progressDialog.show(mContext);
        }
        HashMap<String, String > map = new HashMap<>();
        map.put("token", activeUser.getToken());
        page = reload ? 1 : ++page;
        map.put("page", String.valueOf(page));
        map.put("order", String.valueOf(type));
        CheJSONObjectRequest request = new CheJSONObjectRequest(TuanURLs.getMainTuan, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                isLoading = false;
                dismissDialog(TuanMainActivity.this);
                if (object.has(ResponseCode.ResponseState)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    mainBean = TuanMainBean.fromJson(object);
                    if (mainBean != null){
                        if (tempList.size() == 0){
                            tempList = mainBean.getListBeans();
                        }else {
                            tempList.addAll(mainBean.getListBeans());
                        }
                        adapter.setData(tempList);
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete(true);
                        if (mainBean.getHaveNext()){
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        }else {
                            listView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                dismissDialog(TuanMainActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
