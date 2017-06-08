package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class ProvinceBean {
    private static final String PROVINCE_ID = "id";
    private static final String PROVINCE_NAME = "name";
    private static final String PROVINCE_SUB = "sub";

    private int provinceId;
    private String provinceName;
    private ArrayList<CityBean> provinceSub;

    public ProvinceBean() {
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public ArrayList<CityBean> getProvinceSub() {
        return provinceSub;
    }

    public void setProvinceSub(ArrayList<CityBean> provinceSub) {
        this.provinceSub = provinceSub;
    }

    public static ArrayList<ProvinceBean> fromJSONArrayString(String resource){
        ArrayList<ProvinceBean> provinceList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                ProvinceBean provinceBean = new ProvinceBean();
                if (object.has(PROVINCE_ID)){
                    provinceBean.setProvinceId(object.optInt(PROVINCE_ID));
                }
                if (object.has(PROVINCE_NAME)){
                    provinceBean.setProvinceName(object.optString(PROVINCE_NAME));
                }
                if (object.has(PROVINCE_SUB)){
                    String citiesString = object.optString(PROVINCE_SUB);
                    ArrayList<CityBean> cityList = CityBean.fromJSONObjectString(citiesString);
                    if (cityList != null){
                        provinceBean.setProvinceSub(cityList);
                    }
                }
                provinceList.add(provinceBean);
            }
            return provinceList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
