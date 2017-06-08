package com.ceyu.carsteward.car.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/16.
 */
public class CarYearBean {
    private int _year;

    public CarYearBean() {
    }

    public int get_year() {
        return _year;
    }

    public void set_year(int _year) {
        this._year = _year;
    }

    public static ArrayList<CarYearBean> fromJsonArray(JSONArray array){
        if (array == null){
            return null;
        }

        ArrayList<CarYearBean> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                CarYearBean yearBean = new CarYearBean();
                String index = "id";
                if (object.has(index)){
                    yearBean.set_year(object.optInt(index));
                }
                list.add(yearBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
