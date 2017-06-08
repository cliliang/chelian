package com.ceyu.carsteward.maintain.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainContent {
    private String _part;
    private String _info;
    private String _money;

    public MaintainContent() {
    }

    public String get_part() {
        return _part;
    }

    public void set_part(String _part) {
        this._part = _part;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public static ArrayList<MaintainContent> fromString(String resource){
        ArrayList<MaintainContent> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                MaintainContent content = new MaintainContent();
                String index = "part";
                if (object.has(index)){
                    content.set_part(object.optString(index));
                }
                index = "info";
                if (object.has(index)){
                    content.set_info(object.optString(index));
                }
                index = "money";
                if (object.has(index)){
                    content.set_money(object.optString(index));
                }
                list.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
