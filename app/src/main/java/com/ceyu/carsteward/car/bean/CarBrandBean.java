package com.ceyu.carsteward.car.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class CarBrandBean {
    private int _id;
    private String _name;
    private String _index;
    private String _pic;

    public CarBrandBean() {
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

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public static ArrayList<CarBrandBean> fromJsonArray(JSONArray array){
         ArrayList<CarBrandBean> list = new ArrayList<>();
         for (int i = 0; i < array.length(); i++){
             JSONObject object;
             try {
                 object = array.getJSONObject(i);
                 CarBrandBean bean = new CarBrandBean();
                 String index = "id";
                 if (object.has(index)){
                     bean.set_id(object.optInt(index));
                 }
                 index = "name";
                 if (object.has(index)){
                     bean.set_name(object.optString(index));
                 }
                 index = "index";
                 if (object.has(index)){
                     bean.set_index(object.optString(index));
                 }
                 index = "pic";
                 if (object.has(index)){
                     bean.set_pic(object.optString(index));
                 }
                 list.add(bean);
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }
         return list;
     }
}
