package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/12.
 */
public class OnlineEngineerInfo {
    private String _token;
    private String _function;
    private String _pic;
    private String _name;

    public OnlineEngineerInfo() {
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public String get_model() {
        return _function;
    }

    public void set_model(String _model) {
        this._function = _model;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public static ArrayList<OnlineEngineerInfo> fromString(String resource){
        ArrayList<OnlineEngineerInfo> infos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                OnlineEngineerInfo info = new OnlineEngineerInfo();
                JSONObject object = array.getJSONObject(i);
                String index = "token";
                if (object.has(index)){
                    info.set_token(object.optString(index));
                }
                index = "function";
                if (object.has(index)){
                    info.set_model(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    info.set_pic(object.optString(index));
                }
                index = "name";
                if (object.has(index)){
                    info.set_name(object.optString(index));
                }
                infos.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infos;
    }
}
