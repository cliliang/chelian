package com.ceyu.carsteward.car.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.adapter.ChoiceDetailAdapter;
import com.ceyu.carsteward.car.bean.CarDetailBean;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/16.
 */
public class AddCarDetailActivity extends BaseActivity {

    private Context mContext;
    private ChoiceDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_listview_layout);
        final Bundle bundle = getIntent().getExtras();
        int seriesId, yearId;
        if (bundle != null){
            String title = bundle.getString(CarEvent.carSeriesName);
            seriesId = bundle.getInt(CarEvent.carSeriesId);
            yearId = bundle.getInt(CarEvent.carYearId);
            setTitle(title);
        }else {
            return;
        }
        mContext = AddCarDetailActivity.this;
        ListView listView = (ListView) findViewById(R.id.simple_item_text_id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarDetailBean bean = (CarDetailBean) adapter.getItem(position);
                if (bean != null) {
                    bundle.putInt(CarEvent.carDetailId, bean.get_id());
                    bundle.putString(CarEvent.carDetailName, bean.get_name());
                    CarRouter.getInstance(mContext).showActivity(CarUI.addCarMileage, bundle);
                }
            }
        });
        adapter = new ChoiceDetailAdapter(mContext);
        listView.setAdapter(adapter);
        getCarDetail(seriesId, yearId);
    }

    private void getCarDetail(int seriesId, int yearId){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(seriesId));
        map.put("year", String.valueOf(yearId));
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarDetail, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(AddCarDetailActivity.this);
                ArrayList<CarDetailBean> list = CarDetailBean.fromJsonArray(array);
                if (list != null && list.size() > 0){
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarDetailActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
