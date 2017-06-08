package com.ceyu.carsteward.maintain.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/24.
 */
public class ListShopBean implements Comparable{
    private int _id;
    private String _name;
    private String _pic;
    private String _address;
    private String _quote;
    private String _distance;
    private String _onlinePay;

    public ListShopBean() {
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

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_quote() {
        return _quote;
    }

    public void set_quote(String _quote) {
        this._quote = _quote;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public String get_onlinePay() {
        return _onlinePay;
    }

    public void set_onlinePay(String _onlinePay) {
        this._onlinePay = _onlinePay;
    }

    public static ArrayList<ListShopBean> fromJsonString(String resource){
        ArrayList<ListShopBean> shopBeans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                ListShopBean bean = new ListShopBean();
                String index = "sid";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "address";
                if (object.has(index)){
                    bean.set_address(object.optString(index));
                }
                index = "quote";
                if (object.has(index)){
                    bean.set_quote(object.optString(index));
                }
                index = "distance";
                if (object.has(index)){
                    bean.set_distance(object.optString(index));
                }
                index = "onlinePay";
                if (object.has(index)){
                    bean.set_onlinePay(object.optString(index));
                }
                shopBeans.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopBeans;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
