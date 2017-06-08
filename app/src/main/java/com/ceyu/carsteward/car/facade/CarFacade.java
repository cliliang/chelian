package com.ceyu.carsteward.car.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarURLs;
import com.ceyu.carsteward.car.views.CarFacadeView;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/15.
 */
public class CarFacade extends ModFacadeView{
    private RequestQueue requestQueue;
    private CarFacadeView carFacadeView;
    private Context mContext;
    private int errorCount = 0;
    private AppConfig appConfig;
    public CarFacade(Context context) {
        super(context);
        this.mContext = context;
        requestQueue = Volley.newRequestQueue(context);
        clearViews();
        carFacadeView = new CarFacadeView(mContext);
        insertView(carFacadeView, null);
        appConfig = AppConfig.getInstance(mContext);
        appConfig.setRefreshCar(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.isNetworkConnected(mContext)){
            carFacadeView.updateIndicatro();
            if (appConfig.getRefreshCar()){
                appConfig.setRefreshCar(false);
                getCarInfos();
            }
        }
    }

    private void getCarInfos(){
        AppContext appContext = (AppContext) getContext().getApplicationContext();
        User user = appContext.getActiveUser();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(CarURLs.getCarList, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                ArrayList<CarInfoBean> list = CarInfoBean.fromJsonArray(array);
                if (list != null){
                    carFacadeView.setData(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (errorCount < 3){
                    getCarInfos();
                    errorCount++;
                }
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }
}
