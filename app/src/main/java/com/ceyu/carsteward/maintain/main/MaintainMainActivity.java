package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.car.main.CarURLs;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.views.IndicatorView;
import com.ceyu.carsteward.maintain.adapter.MaintainListAdapter;
import com.ceyu.carsteward.maintain.adapter.MaintainPagerAdapter;
import com.ceyu.carsteward.maintain.bean.MileageBean;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainMainActivity extends BaseActivity {

    private Context mContext;
    private IndicatorView indicatorView;
    private MaintainListAdapter adapter;
    private ListView listView;
    private int dotCount;
    private int carId, carKilo, modelId;
    private int shopClass = 1; //店铺类型（1:4s/2:修理厂）
    private String kiloString;
    private ArrayList<CarInfoBean> carInfoBeans = null;
    private MaintainPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private User activeUser;
    private HashMap<Integer, JSONArray> cacheData = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_activity_layout);
        mContext = MaintainMainActivity.this;
        activeUser = ((AppContext)getApplicationContext()).getActiveUser();
        viewPager = (ViewPager) findViewById(R.id.maintain_top_view_pager);
        indicatorView = (IndicatorView) findViewById(R.id.maintain_top_view_indicator);
        pagerAdapter = new MaintainPagerAdapter(mContext);
        viewPager.setAdapter(pagerAdapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            carInfoBeans = bundle.getParcelableArrayList(CarEvent.carInfoList);
            shopClass = bundle.getInt(CarEvent.shopClass);
            kiloString = bundle.getString(CarEvent.carMileage);
            if (shopClass == 1){
                setTitle(R.string.home_block_string_upkeep_4s);
            }else {
                setTitle(R.string.home_block_string_upkeep_factory);
            }
            if (carInfoBeans != null && carInfoBeans.size() > 0){
                pagerAdapter.setDatas(carInfoBeans);
                pagerAdapter.notifyDataSetChanged();
                dotCount = carInfoBeans.size();
                indicatorView.updateScreen(0, dotCount);
            }else {
                getCarInfos();
            }
        }else {
            finish();
        }
        adapter = new MaintainListAdapter(mContext);
        listView = (ListView) findViewById(R.id.upkeep_main_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MileageBean mileageBean = (MileageBean) adapter.getItem(position);
                if (mileageBean != null) {
                    adapter.setSelection(position);
                    adapter.notifyDataSetChanged();
                    Bundle b = new Bundle();
                    b.putInt(MaintainEvent.CarKilo, mileageBean.get_num());
                    b.putInt(MaintainEvent.CarModelId, modelId);
                    b.putInt(MaintainEvent.CarId, carId);
                    b.putInt(CarEvent.shopClass, shopClass);
                    MaintainRouter.getInstance(mContext).showActivity(MaintainUI.getMaintainList, b);
                }
            }
        });
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        CarInfoBean carInfoBean = appContext.getCarInfo();
        if (carInfoBean != null){
            carId = carInfoBean.get_id();
        }

        if (carInfoBeans != null && carInfoBeans.size() > 0) {
            for (int i = 0; i < carInfoBeans.size(); i++){
                int id = carInfoBeans.get(i).get_id();
                if (id == carId){
                    modelId = carInfoBeans.get(i).get_modelId();
                    indicatorView.updateScreen(i, dotCount);
                    viewPager.setCurrentItem(i);
                    getMileageList(modelId, true);
                    break;
                }
            }
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.updateScreen(position, dotCount);
                ArrayList<CarInfoBean> infoBeans = pagerAdapter.getData();
                if (infoBeans != null && infoBeans.size() > position) {
                    CarInfoBean bean = infoBeans.get(position);
                    if (bean != null) {
                        carId = bean.get_id();
                        modelId = bean.get_modelId();
                        getMileageList(bean.get_modelId(), false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getCarInfos(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarList, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(MaintainMainActivity.this);
                ArrayList<CarInfoBean> list = CarInfoBean.fromJsonArray(array);
                if (list != null && list.size() > 0){
                    carInfoBeans = list;
                    pagerAdapter.setDatas(carInfoBeans);
                    pagerAdapter.notifyDataSetChanged();
                    dotCount = carInfoBeans.size();
                    indicatorView.updateScreen(0, dotCount);
                    modelId = carInfoBeans.get(0).get_modelId();
                    carId = carInfoBeans.get(0).get_id();
                    viewPager.setCurrentItem(0);
                    getMileageList(modelId, true);
                }else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_add_car_first));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MaintainMainActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getMileageList(final int id, final boolean scroll){
        JSONArray array = cacheData.get(id);
        if (array != null){
            updateUI(array, scroll);
        }else {
            if (requestQueue != null){
                requestQueue.cancelAll(this);
            }
            progressDialog.show(mContext);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            map.put("class", String.valueOf(shopClass));
            map.put("city", String.valueOf(activeUser.getCityId()));
            CheJSONArrayRequest request = new CheJSONArrayRequest(MaintainURLs.getMileages, map, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray array) {
                    dismissDialog(MaintainMainActivity.this);
                    if (array != null){
                        cacheData.put(id, array);
                        updateUI(array, scroll);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    dismissDialog(MaintainMainActivity.this);
                    HandleVolleyError.showErrorMessage(mContext, volleyError);
                }
            });
            requestQueue.add(request);
            requestQueue.start();
        }
    }

    private void updateUI(JSONArray array, boolean scroll){
        ArrayList<MileageBean> beans = MileageBean.fromJsonArray(array);
        int selection = 0;
        if (scroll){
            selection = getItemIndex(beans);
        }
        adapter.setSelection(selection);
        adapter.setData(beans);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setSelection(selection);

    }

    private int getItemIndex(ArrayList<MileageBean> mileageBeans){
        List<Integer> kiloList = new ArrayList<>();
        if (mileageBeans != null && mileageBeans.size() > 0){
            for (int i = 0; i < mileageBeans.size(); i++){
                kiloList.add(mileageBeans.get(i).get_num());
            }
            try {
                int mileage = Integer.parseInt(kiloString);
                if (kiloList.size() > 1) {
                    for (int j = 0; j < kiloList.size() - 1; j++) {
                        int indexMileage = kiloList.get(j);
                        int nextMileage = kiloList.get(j + 1);
                        if (mileage >= indexMileage && mileage < nextMileage){
                            return j;
                        }
                    }
                    if (kiloList.get(kiloList.size() - 1) <= mileage){
                        return kiloList.size() - 1;
                    }
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return 0;
    }
}