package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.maintain.adapter.MaintainShopListAdapter;
import com.ceyu.carsteward.maintain.bean.ListShopBean;
import com.ceyu.carsteward.maintain.bean.MaintainListBean;
import com.ceyu.carsteward.maintain.bean.ShopComparable;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/24.
 */
public class MaintainShopListActivity extends BaseActivity {

    private User user;
    private Context mContext;
    private TextView countView, sortDistanceView, sortPriceView, emptyView;
    private ArrayList<ListShopBean> shopBeans = new ArrayList<>();
    private MaintainShopListAdapter adapter;
    private ListView listView;
    private int modelId, kilo, carId, shopClass;
    private Bundle bundle;
    private HashMap<Integer, JSONObject> cache = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_shop_list_activity);
        setTitle(R.string.choice_maintain_shop);
        mContext = MaintainShopListActivity.this;
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        user = appContext.getActiveUser();
        emptyView = (TextView) findViewById(R.id.maintain_shop_list_empty);
        countView = (TextView) findViewById(R.id.maintain_list_shop_count);
        sortDistanceView = (TextView) findViewById(R.id.maintain_list_sort_by_distance);
        sortPriceView = (TextView) findViewById(R.id.maintain_list_sort_by_price);
        listView = (ListView) findViewById(R.id.maintain_list_view);
        adapter = new MaintainShopListAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListShopBean shopBean = (ListShopBean) adapter.getItem(position);
                if (shopBean != null){
                    bundle.putInt(MaintainEvent.shopId, shopBean.get_id());
                    MaintainRouter.getInstance(mContext).showActivity(MaintainUI.getMaintainCombo, bundle);
                }
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null){
            kilo = bundle.getInt(MaintainEvent.CarKilo);
            modelId = bundle.getInt(MaintainEvent.CarModelId);
            carId = bundle.getInt(MaintainEvent.CarId);
            shopClass = bundle.getInt(CarEvent.shopClass);
            getShopList(modelId, kilo, 1);
        }
        sortDistanceView = (TextView) findViewById(R.id.maintain_list_sort_by_distance);
        sortDistanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDistanceView.setTextColor(getResources().getColor(R.color.orange));
                sortPriceView.setTextColor(getResources().getColor(R.color.text_hint));
                getShopList(modelId, kilo, 2);
            }
        });
        sortPriceView = (TextView) findViewById(R.id.maintain_list_sort_by_price);
        sortPriceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPriceView.setTextColor(getResources().getColor(R.color.orange));
                sortDistanceView.setTextColor(getResources().getColor(R.color.text_hint));
                getShopList(modelId, kilo, 1);
            }
        });
    }

    private void getShopList(int carid, int carkilo, final int order){
        JSONObject object = cache.get(order);
        if (object != null){
            updateUI(object);
        }else {
            progressDialog.show(mContext);
            HashMap<String, String> map = new HashMap<>();
            map.put("token", user.getToken());
            map.put("cid", String.valueOf(carid));
            map.put("class", String.valueOf(shopClass));
            map.put("mileage", String.valueOf(carkilo));
            map.put("city", String.valueOf(user.getCityId()));
            //排序（1：价格；2：距离）
            map.put("order", String.valueOf(order));
            CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.getMaintainList, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    dismissDialog(MaintainShopListActivity.this);
                    if (object != null){
                        cache.put(order, object);
                        updateUI(object);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    dismissDialog(MaintainShopListActivity.this);
                    HandleVolleyError.showErrorMessage(mContext, volleyError);
                }
            });
            requestQueue.add(request);
            requestQueue.start();
        }

    }

    private void updateUI(JSONObject object){
        MaintainListBean listBean = MaintainListBean.fromJson(object);
        if (listBean != null){
            countView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.maintain_shop_count), listBean.get_num()));
            shopBeans = listBean.getShopBeans();
            if (shopBeans != null && shopBeans.size() > 0){
                adapter.setData(shopBeans);
                adapter.notifyDataSetChanged();
            }else {
                listView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

        }
    }
}