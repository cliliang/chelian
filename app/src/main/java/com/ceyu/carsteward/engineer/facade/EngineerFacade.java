package com.ceyu.carsteward.engineer.facade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.Config;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.engineer.bean.OnlineEngineers;
import com.ceyu.carsteward.engineer.main.EngineerURLs;
import com.ceyu.carsteward.engineer.view.OnlineEngineerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 15/6/12.
 */
public class EngineerFacade extends ModFacadeView {

    private RequestQueue requestQueue;
    private OnlineEngineerView onlineEngineerView;
    private Context mContext;
    private int errorCount = 0;
    private AppConfig appConfig;
    private AppContext appContext;
    private String token;
    private int id = 0;
    private Map<Integer, OnlineEngineers> cache = new HashMap<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                if (appContext.getCarInfo() != null){
                    boolean clearCache = intent.getBooleanExtra(CarEvent.clearCarCache, false);
                    getOnlineEngineer(appContext.getCarInfo().get_brandId(), clearCache);
                }
            }
        }
    };

    public EngineerFacade(Context context) {
        super(context);
        this.mContext = context;
        appContext = (AppContext) mContext.getApplicationContext();
        token = appContext.getActiveUser().getToken();
        requestQueue = Volley.newRequestQueue(context);
        appConfig = AppConfig.getInstance(mContext);
        onlineEngineerView = new OnlineEngineerView(context);
        clearViews();
        insertView(onlineEngineerView, null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CarEvent.refreshOnlineAction);
        mContext.registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onResume() {
        if (Utils.isNetworkConnected(mContext)){
            getOnlineEngineer(id, false);
        }else {
            UIHelper.ToastMessage(mContext, R.string.network_not_connected);
        }
        super.onResume();
    }

    private void getOnlineEngineer(final int brandId, boolean clearCache){
        if (clearCache){
            cache.remove(brandId);
        }
        OnlineEngineers cacheData = cache.get(brandId);
        if (cacheData != null && cacheData.getEngineerInfos().size() > 0){
            onlineEngineerView.setData(cacheData.getEngineerInfos());
        }else {
            requestQueue.cancelAll(mContext);
            HashMap<String, String> map = new HashMap<>();
            AppContext appContext = (AppContext) mContext.getApplicationContext();
            map.put("city", String.valueOf(appContext.getActiveUser().getCityId()));
            map.put("model", String.valueOf(brandId));
            map.put("token", token);
            CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getOnlineEngineer, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    OnlineEngineers onlineEngineers = OnlineEngineers.fromJsonObject(object);
                    onlineEngineerView.setData(onlineEngineers.getEngineerInfos());
                    cache.put(brandId, onlineEngineers);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (errorCount < 3){
                        getOnlineEngineer(brandId, false);
                        errorCount ++;
                    }
                }
            });
            requestQueue.add(request);
            requestQueue.start();
        }
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
