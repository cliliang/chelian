package com.ceyu.carsteward.car.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.adapter.ChoiceInsuranceAdapter;
import com.ceyu.carsteward.car.bean.InsuranceBean;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/17.
 */
public class ShowInsuranceActivity extends BaseActivity {

    private Context mContext;
    private ChoiceInsuranceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.choice_insurance_company);
        setContentView(R.layout.common_simple_listview_layout);
        mContext = ShowInsuranceActivity.this;
        ListView listView = (ListView) findViewById(R.id.common_simple_listview);
        adapter = new ChoiceInsuranceAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InsuranceBean bean = (InsuranceBean) adapter.getItem(position);
                if (bean != null){
                    Intent intent = new Intent();
                    intent.putExtra(CarEvent.insuranceBean, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        getInsuranceList();
    }

    public void getInsuranceList(){
        progressDialog.show(mContext);
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getInsurance, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(ShowInsuranceActivity.this);
                ArrayList<InsuranceBean> list = InsuranceBean.fromJsonArray(array);
                if (list != null && list.size() > 0){
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(ShowInsuranceActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
