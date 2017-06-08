package com.ceyu.carsteward.car.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class CarModelBean {
    private int _id;
    private String _name;

    public CarModelBean() {
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public static ArrayList<CarModelBean> fromString(String resource){
        ArrayList<CarModelBean> list = new ArrayList<>();
        JSONArray array;
        try {
            array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                CarModelBean bean = new CarModelBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
