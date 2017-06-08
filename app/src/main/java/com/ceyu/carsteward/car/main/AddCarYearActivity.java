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
import com.ceyu.carsteward.car.adapter.ChoiceYearAdapter;
import com.ceyu.carsteward.car.bean.CarYearBean;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/15.
 */
public class AddCarYearActivity extends BaseActivity {
    private Context mContext;
    private ChoiceYearAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_listview_layout);
        final Bundle bundle = getIntent().getExtras();
        int seriesId;
        if (bundle != null){
            seriesId = bundle.getInt(CarEvent.carSeriesId);
            String title = bundle.getString(CarEvent.carSeriesName);
            if (!StringUtils.isEmpty(title)){
                setTitle(title);
            }
        }else {
            return;
        }
        mContext = AddCarYearActivity.this;
        ListView listView = (ListView) findViewById(R.id.simple_item_text_id);
        adapter = new ChoiceYearAdapter(mContext);
        listView.setAdapter(adapter);
        getYear(seriesId);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarYearBean bean = (CarYearBean) adapter.getItem(position);
                if (bean != null) {
                    bundle.putInt(CarEvent.carYearId, bean.get_year());
                    CarRouter.getInstance(mContext).showActivity(CarUI.addCarDetail, bundle);
                }

            }
        });
    }

    private void getYear(int id){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarYear, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(AddCarYearActivity.this);
                ArrayList<CarYearBean> list = CarYearBean.fromJsonArray(array);
                if (list != null && list.size()>0){
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarYearActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
