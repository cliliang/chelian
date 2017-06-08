package com.ceyu.carsteward.car.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/16.
 */
public class CarDetailBean {
    private int _id;
    private String _name;

    public CarDetailBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public static ArrayList<CarDetailBean> fromJsonArray(JSONArray array){
        if (array == null || array.length() == 0){
            return null;
        }
        ArrayList<CarDetailBean> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                CarDetailBean bean = new CarDetailBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
