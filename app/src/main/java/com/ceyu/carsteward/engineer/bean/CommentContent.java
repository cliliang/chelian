package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/5.
 */
public class CommentContent {

    private String _user;
    private int _assess; //打分
    private String _info;
    private String _date;

    public CommentContent() {
    }

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

    public int get_assess() {
        return _assess;
    }

    public void set_assess(int _assess) {
        this._assess = _assess;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    static ArrayList<CommentContent> fromString(String resource){
        ArrayList<CommentContent> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                CommentContent content = new CommentContent();
                String item = "user";
                if (object.has(item)){
                    content.set_user(object.optString(item));
                }
                item = "assess";
                if (object.has(item)){
                    content.set_assess(object.optInt(item));
                }
                item = "info";
                if (object.has(item)){
                    content.set_info(object.optString(item));
                }
                item = "date";
                if (object.has(item)){
                    content.set_date(object.optString(item));
                }
                list.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
