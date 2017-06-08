package com.ceyu.carsteward.points.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.points.PointsURLs;
import com.ceyu.carsteward.points.adapter.PointMainItemAdapter;
import com.ceyu.carsteward.points.bean.PointGoodsBean;
import com.ceyu.carsteward.points.bean.PointsMainBean;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 * 积分列表
 */
public class PointStaticActivity extends BaseActivity{
    private Context mContext;
    private TextView countView;
    private PointMainItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_static);
        mContext = this;
        countView = (TextView) findViewById(R.id.points_main_count);
        ListView listView = (ListView) findViewById(R.id.points_list_view);
        adapter = new PointMainItemAdapter(mContext);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", ((AppContext)getApplicationContext()).getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(PointsURLs.getPoints, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(PointStaticActivity.this);
                PointsMainBean bean = PointsMainBean.fromJson(response);
                setViewData(bean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(PointStaticActivity.this);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(PointsMainBean bean){
        if (bean == null){
            return;
        }
        List<PointGoodsBean> beans = bean.getList();
        if (beans != null){
            adapter.setData(beans);
            adapter.notifyDataSetChanged();
        }
        countView.setText(String.valueOf(bean.getIntegral()));
    }
}
