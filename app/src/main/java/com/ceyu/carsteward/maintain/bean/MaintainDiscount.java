package com.ceyu.carsteward.maintain.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/7/31.
 */
public class MaintainDiscount {

    private String _title;
    private String _num;

    public MaintainDiscount() {
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_num() {
        return _num;
    }

    public void set_num(String _num) {
        this._num = _num;
    }

    public static ArrayList<MaintainDiscount> fromString(String resource){
        ArrayList<MaintainDiscount> discounts = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                MaintainDiscount discount = new MaintainDiscount();
                String index = "title";
                if (object.has(index)){
                    discount.set_title(object.optString(index));
                }
                index = "num";
                if (object.has(index)){
                    discount.set_num(object.optString(index));
                }
                discounts.add(discount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return discounts;
    }
}
