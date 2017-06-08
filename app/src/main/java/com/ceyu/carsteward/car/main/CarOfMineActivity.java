package com.ceyu.carsteward.car.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.adapter.CarOfMineAdapter;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.common.ui.views.swipemenulistview.SwipeMenu;
import com.ceyu.carsteward.common.ui.views.swipemenulistview.SwipeMenuCreator;
import com.ceyu.carsteward.common.ui.views.swipemenulistview.SwipeMenuItem;
import com.ceyu.carsteward.common.ui.views.swipemenulistview.SwipeMenuListView;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/15.
 */
public class CarOfMineActivity extends BaseActivity {


    private Context mContext;
    private CarOfMineAdapter adapter;
    private ArrayList<CarInfoBean> carInfoBeans;
    private SwipeMenuListView listView;
    private User activeUser;
    private AppContext appContext;
    private AppConfig appConfig;
    private Button addCarButton;
    private boolean fromTuan = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.car_of_mine);
        setContentView(R.layout.car_of_mine_activity);
        mContext = CarOfMineActivity.this;
        appConfig = AppConfig.getInstance(mContext);
        appContext = (AppContext) mContext.getApplicationContext();
        carInfoBeans = new ArrayList<>();
        activeUser = appContext.getActiveUser();
        listView = (SwipeMenuListView) findViewById(R.id.car_of_mine_list);
        View bottomView = LayoutInflater.from(mContext).inflate(R.layout.car_of_mine_list_bottom_view_layout, null);
        listView.addFooterView(bottomView);

        final Bundle bundle = getIntent().getExtras();
        int choiceCarId = 0;
        if (bundle != null){
            fromTuan = bundle.getBoolean(TuanEvent.fromTuan);
            choiceCarId = bundle.getInt(TuanEvent.choiceCarId);
        }
        adapter = new CarOfMineAdapter(mContext, fromTuan, choiceCarId);
        listView.setAdapter(adapter);
        if (!fromTuan){
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(
                            getApplicationContext());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    deleteItem.setWidth(getResources().getDimensionPixelSize(R.dimen.car_of_mine_list_item_height));
                    deleteItem.setIcon(R.mipmap.ic_delete);
                    menu.addMenuItem(deleteItem);
                }
            };
            listView.setMenuCreator(creator);
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                    CarInfoBean bean = (CarInfoBean) adapter.getItem(position);
                    deleteCar(bean);
                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarInfoBean infoBean = (CarInfoBean) adapter.getItem(position);
                if (infoBean != null) {
                    if (fromTuan){
                        adapter.setCheckedId(infoBean.get_id());
                        adapter.notifyDataSetChanged();
                        Intent intent = new Intent();
                        intent.putExtra(TuanEvent.choiceCarInfo, infoBean);
                        setResult(TuanEvent.choiceCar, intent);
                        finish();
                    }else {
                        CarBrandInfoBean brandInfoBean = infoBean.getBrandInfoBean();
                        Bundle bundle = new Bundle();
                        bundle.putInt(CarEvent.carId, infoBean.get_id());
                        bundle.putInt(CarEvent.carYearId, Integer.parseInt(brandInfoBean.get_year()));
                        bundle.putInt(CarEvent.carDetailId, infoBean.get_modelId());
                        bundle.putString(CarEvent.carDetailName, brandInfoBean.get_capacity() + brandInfoBean.get_auto());
                        bundle.putString(CarEvent.carSeriesName, brandInfoBean.get_brandName() + brandInfoBean.get_modelName());
                        bundle.putParcelable(CarEvent.carBean, infoBean);
                        CarRouter.getInstance(mContext).showActivity(CarUI.addCarMileage, bundle);
                    }
                }
            }
        });
        addCarButton = (Button) bottomView.findViewById(R.id.car_of_mine_add_car_button);
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean(TuanEvent.fromTuan, fromTuan);
                CarRouter.getInstance(mContext).showActivity(CarUI.addCarBrand, b);
            }
        });
    }

    @Override
    protected void onResume() {
        listView.setVisibility(View.INVISIBLE);
        if (Utils.isNetworkConnected(mContext)){
            getCarInfos();
        }else {
            UIHelper.ToastMessage(mContext, R.string.network_not_connected);
        }
        super.onResume();
    }

    private void getCarInfos(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarList, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                dismissDialog(CarOfMineActivity.this);
                ArrayList<CarInfoBean> list = CarInfoBean.fromJsonArray(array);
                if (list != null){
                    listView.setVisibility(View.VISIBLE);
                    carInfoBeans = list;
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                    if (list.size() == 5){
                        addCarButton.setVisibility(View.GONE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(CarOfMineActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void deleteCar(final CarInfoBean bean){
        progressDialog.show(mContext);
        progressDialog = ProgressDialog.getInstance();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("cid", String.valueOf(bean.get_id()));
        CheJSONObjectRequest request = new CheJSONObjectRequest(CarURLs.deleteCar, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(CarOfMineActivity.this);
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseOK)){
                        appConfig.setRefreshCar(true);
                        CarInfoBean nowCar = appContext.getCarInfo();
                        if (nowCar != null){
                            int defaultId = nowCar.get_id();
                            if (bean.get_id() == defaultId){
                                appContext.setCarInfo(null);
                            }
                        }

                        UIHelper.ToastMessage(mContext, R.string.delete_car_success);
                        carInfoBeans.remove(bean);
                        addCarButton.setVisibility(View.VISIBLE);
                        adapter.setData(carInfoBeans);
                        adapter.notifyDataSetChanged();
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(CarOfMineActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
