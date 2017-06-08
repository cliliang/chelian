package com.ceyu.carsteward.car.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.adapter.ChoiceBrandAdapter;
import com.ceyu.carsteward.car.adapter.ChoiceModelAdapter;
import com.ceyu.carsteward.car.bean.CarBrandBean;
import com.ceyu.carsteward.car.bean.CarModelBean;
import com.ceyu.carsteward.car.bean.CarSeries;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.tuan.main.TuanEvent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/15.
 */
public class AddCarBrandActivity extends BaseActivity {

    private ChoiceBrandAdapter brandAdapter;
    private ChoiceModelAdapter modelAdapter;
    private ExpandableListView modelView;
    private CarBrandBean nowCarBrandBean;
    private LinearLayout hindLayout;
    private Context mContext;
    private boolean fromTuan;
    private boolean fromBreakRule = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.add_car_choice_brand);
        setContentView(R.layout.add_car_choice_brand_activity_layout);
        mContext = AddCarBrandActivity.this;
        ListView brandView = (ListView) findViewById(R.id.add_car_choice_brand_list);
        modelView = (ExpandableListView) findViewById(R.id.add_car_choice_model_list);
        hindLayout = (LinearLayout) findViewById(R.id.simple_item_list_layout);
        brandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hindLayout.setVisibility(View.VISIBLE);
                CarBrandBean brandBean = (CarBrandBean) brandAdapter.getItem(position);
                if (brandBean != null) {
                    nowCarBrandBean = brandBean;
                    getModels(brandBean.get_id());
                }
            }
        });
        modelView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            fromTuan = bundle.getBoolean(TuanEvent.fromTuan);
            fromBreakRule = bundle.getBoolean(CarEvent.fromBreakRule);
        }

        modelView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CarModelBean bean = (CarModelBean) modelAdapter.getChild(groupPosition, childPosition);
                if (bean != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(CarEvent.carSeriesId, bean.get_id());
                    bundle.putString(CarEvent.carSeriesName, nowCarBrandBean.get_name() + bean.get_name());
                    bundle.putBoolean(TuanEvent.fromTuan, fromTuan);
                    bundle.putBoolean(CarEvent.fromBreakRule, fromBreakRule);
                    CarRouter.getInstance(mContext).showActivity(CarUI.addCarYear, bundle);
                }
                return false;
            }
        });
        brandAdapter = new ChoiceBrandAdapter(mContext, requestQueue);
        brandView.setAdapter(brandAdapter);
        modelAdapter = new ChoiceModelAdapter(mContext);
        modelView.setAdapter(modelAdapter);
        modelView.setGroupIndicator(null);
        getBrands();
    }
    private void getBrands(){
        progressDialog.show(mContext);
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarBrand, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                dismissDialog(AddCarBrandActivity.this);
                ArrayList<CarBrandBean> brandList = CarBrandBean.fromJsonArray(jsonArray);
                if (brandList != null && brandList.size() > 0){
                    brandAdapter.setData(brandList);
                    brandAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarBrandActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getModels(int brandId){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(brandId));
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarModel, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(AddCarBrandActivity.this);
                ArrayList<CarSeries> carSeries = CarSeries.fromjsonArray(array);
                modelAdapter.setData(carSeries);
                modelAdapter.notifyDataSetChanged();
                for (int i = 0; i < modelAdapter.getGroupCount(); i++){
                    modelView.expandGroup(i);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(AddCarBrandActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
