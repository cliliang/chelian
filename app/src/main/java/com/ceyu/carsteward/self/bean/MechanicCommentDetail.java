package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/17.
 */
public class MechanicCommentDetail {
    private String _user;
    private int _assess;
    private String _info;
    private String _date;

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

    public static List<MechanicCommentDetail> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONArray array = new JSONArray(resource);
            List<MechanicCommentDetail> details = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                MechanicCommentDetail detail = new MechanicCommentDetail();
                String index = "user";
                if (object.has(index)){
                    detail.set_user(object.optString(index));
                }
                index = "assess";
                if (object.has(index)){
                    detail.set_assess(object.optInt(index));
                }
                index = "info";
                if (object.has(index)){
                    detail.set_info(object.optString(index));
                }
                index = "date";
                if (object.has(index)){
                    detail.set_date(object.optString(index));
                }
                details.add(detail);
            }
            return details;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
