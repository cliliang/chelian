package com.ceyu.carsteward.main.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.db.CheDBM;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.main.bean.WeatherBean;
import com.ceyu.carsteward.main.ui.MainURLs;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/29.
 * 首页最上端天气、限行一条
 */
public class WeatherFacadeView extends LinearLayout implements Response.Listener<JSONObject>, Response.ErrorListener, View.OnClickListener{

    private Context mContext;
    private List<WeatherBean.LocationBean> cityBeans;
    private String[] cities;
    private TextView cityNameView;
    private AppContext appContext;
    private CheDBM dbm;
    private User activeUser;
    private Intent broadcastIntent;
    public WeatherFacadeView(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.weather_facade_view_layout, this);
        dbm = CheDBM.getInstance(mContext);
        appContext = (AppContext)context.getApplicationContext();
        activeUser = appContext.getActiveUser();
        broadcastIntent = new Intent();
        broadcastIntent.setAction(CarEvent.refreshOnlineAction);
        broadcastIntent.putExtra(CarEvent.clearCarCache, true);
        getWeatherInfo(context);
    }

    public void getWeatherInfo(Context context) {
        AppContext appContext = (AppContext) context.getApplicationContext();
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", appContext.getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(MainURLs.getWeatherInfo, map, this, null);
        appContext.queue().add(request);
    }

    public void setData(WeatherBean bean){
        if (bean == null){
            return;
        }
        TextView tvCarLimit = (TextView)findViewById(R.id.tv_weather_facade_view_layout_carlimit);
        tvCarLimit.setText(bean.getLimit());
        TextView tvWeather = (TextView)findViewById(R.id.tv_weather_facade_view_layout_weatherdetail);
        tvWeather.setText(bean.getWeather());
        cityNameView = (TextView) findViewById(R.id.tv_weather_facade_view_layout_location);
        WeatherBean.LocationBean myCity = WeatherBean.cityFinder(bean);
        if (myCity != null){
            cityNameView.setText(myCity.getName());
            activeUser.setCityId(myCity.getId());
            activeUser.setCityName(myCity.getName());
            appContext.setActiveUser(activeUser);
            dbm.updateUser(myCity.getId(), myCity.getName());
        }
        cityBeans = bean.getCity();
        if (cityBeans != null){
            cities = new String[cityBeans.size()];
            for (int i = 0; i < cityBeans.size(); i++){
                cities[i] = cityBeans.get(i).getName();
            }
        }
        findViewById(R.id.tv_weather_facade_view_layout_locationparent).setOnClickListener(this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        try{
            WeatherBean weatherBean = WeatherBean.parse(jsonObject);
            setData(weatherBean);
        }catch (Exception e){
            Utility.LogUtils.ex(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Utility.LogUtils.ex(volleyError);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_weather_facade_view_layout_locationparent: //点击选择城市
                chooseCity();
                break;
            default:
                break;
        }
    }

    private void chooseCity(){
        if (cities != null){
            new AlertDialog.Builder(mContext)
                    .setTitle(mContext.getString(R.string.weather_choose_location))
                    .setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (cityBeans.size() > which){
                                WeatherBean.LocationBean bean = cityBeans.get(which);
                                if (bean != null){
                                    cityNameView.setText(bean.getName());
                                    activeUser.setCityId(bean.getId());
                                    activeUser.setCityName(bean.getName());
                                    appContext.setActiveUser(activeUser);
                                    dbm.updateUser(bean.getId(), bean.getName());
                                    mContext.sendBroadcast(broadcastIntent);
                                }
                            }
                        }
                    })
                    .setNegativeButton(mContext.getString(R.string.cancel), null)
                    .show();
        }
    }
}
