package com.ceyu.carsteward.main.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/29.
 * 首页天气、限行实体类
 */
public class WeatherBean {

    private List<LocationBean> city;    //备选城市列表
    private int mycity;                 //我的城市ID
    private String weather;             //天气详情
    private String limit;               //限行

    public List<LocationBean> getCity() {
        return city;
    }

    public void setCity(List<LocationBean> city) {
        this.city = city;
    }

    public int getMycity() {
        return mycity;
    }

    public void setMycity(int mycity) {
        this.mycity = mycity;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public static class LocationBean{
        private int id;         //城市ID
        private String name;    //城市名

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static WeatherBean parse(JSONObject jsonObject) throws JSONException{
        WeatherBean bean = new WeatherBean();
        List<LocationBean> list = new LinkedList<LocationBean>();
        if(jsonObject.has("city")){
            JSONArray cityList = jsonObject.getJSONArray("city");
            for(int i=0; i<cityList.length(); i++){
                LocationBean locationBean = new LocationBean();
                JSONObject cityObject = cityList.getJSONObject(i);
                if(cityObject.has("id")){
                    locationBean.setId(cityObject.getInt("id"));
                }
                if(cityObject.has("name")){
                    locationBean.setName(cityObject.getString("name"));
                }
                list.add(locationBean);
            }
            bean.setCity(new ArrayList<>(list));

        }
        if(jsonObject.has("mycity")){
            bean.setMycity(jsonObject.getInt("mycity"));
        }
        if(jsonObject.has("weather")){
            bean.setWeather(jsonObject.getString("weather"));
        }
        if(jsonObject.has("limit")){
            bean.setLimit(jsonObject.getString("limit"));
        }
        return bean;
    }

    public static LocationBean cityFinder(WeatherBean bean){
        Iterator<LocationBean> it = bean.getCity().iterator();
        int id = bean.getMycity();
        while(it.hasNext()){
            LocationBean myCity = it.next();
            if(myCity.getId() == id) return myCity;
        }
        return null;
    }

}


