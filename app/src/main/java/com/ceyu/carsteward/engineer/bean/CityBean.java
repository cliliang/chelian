package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class CityBean {
    private static final String CITY_ID = "id";
    private static final String CITY_NAME = "name";

    private int cityId;
    private String cityName;

    public CityBean() {
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public static ArrayList<CityBean> fromJSONObjectString(String resource){
        ArrayList<CityBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                CityBean bean = new CityBean();
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.has(CITY_ID)){
                    bean.setCityId(jsonObject.optInt(CITY_ID));
                }
                if (jsonObject.has(CITY_NAME)){
                    bean.setCityName(jsonObject.optString(CITY_NAME));
                }
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

