package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.main.ui.MainURLs;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/29.
 * 百度定位服务
 */
public class BaiduLBS {

    private static volatile BaiduLBS instance;
    //百度定位返回的结果，包含经纬度、地址等信息
    private BDLocation mLocation;
    //百度定位客户端
    private LocationClient mLocationClient;
    private RequestQueue requestQueue;
    private Context mContext;
    private boolean hasUpload = false;

    public static BaiduLBS getInstanse(Context context) {
        if (instance == null) {
            synchronized (BaiduLBS.class) {
                instance = new BaiduLBS(context);
            }
        }
        return instance;
    }

    private BaiduLBS(Context context) {
        this.mContext = context;
        this.mLocationClient = new LocationClient(mContext);//声明LocationClient类
    }

    private void setBaiduLocation(BDLocation location){
        this.mLocation = location;
    }

    public void startDetailLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        //option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        //option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        //option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        //option.setAddrType("all");//加上就出来地址了。
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BaiduLocationListner());    //注册监听函数
        mLocationClient.start();
    }

    //百度定位的回调监听
    public class BaiduLocationListner implements BDLocationListener {
        @Override
        public synchronized void onReceiveLocation(BDLocation bdLocation) {
            setBaiduLocation(bdLocation);
        }
    }

    public void uploadLocation(){
        if (hasUpload){
            return;
        }
        Map<String, String> map = getUploadSet();
        if (map == null){
            return;
        }
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext);
        }
        CheJSONObjectRequest request = new CheJSONObjectRequest(MainURLs.sendLocation, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    hasUpload = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    /*
    info: "用户环境信息",
    set: "环境信息（手机品牌|手机型号|操作系统|操作系统版本|软件版本|手机序列号|渠道名称|经度|纬度）"
    */
    public Map<String, String> getUploadSet() {
        if (mLocation == null){
            return null;
        }
        if (Double.compare(mLocation.getLongitude(), 0) == 0 || Double.compare(mLocation.getLatitude(), 0) == 0){
            return null;
        }
        Map<String, String> params = new HashMap<>();
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        params.put("token", appContext.getActiveUser().getToken());

        //params.put("set","手机品牌|手机型号|操作系统|操作系统版本|软件版本|手机序列号|渠道名称|经度|纬度");
        //手机品牌|手机型号|操作系统|操作系统版本|软件版本|手机序列号|渠道名称|经度|纬度|省份名称,城市名称,城市代码|详细地址|deviceToken
        StringBuilder builder = new StringBuilder();
        builder.append(Utils.getPhoneInfo(mContext))    //手机品牌|手机型号|操作系统|操作系统版本|软件版本|手机序列号|渠道名称|
                .append(getOnlyLocationSet())
                .append(PushAgent.getInstance(mContext).getRegistrationId()); //友盟返回的设备id

        params.put("set", builder.toString());
        return params;
    }

    public String getOnlyLocationSet() {
        if (mLocation == null){
            return "||||";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(mLocation.getLongitude()).append("|")         //经度|
                .append(mLocation.getLatitude()).append("|")   //纬度|
                .append(mLocation.getProvince()).append(",")   //省份名称,
                .append(mLocation.getCity()).append(",")       //城市名称,
                .append(mLocation.getCityCode()).append("|")   //城市代码|
                .append(mLocation.getAddrStr()).append("|");               //详细地址
        return sb.toString();
    }
}
