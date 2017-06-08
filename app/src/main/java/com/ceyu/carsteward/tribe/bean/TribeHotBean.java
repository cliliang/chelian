package com.ceyu.carsteward.tribe.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/28.
 */
public class TribeHotBean{
    private int _id;
    private String _pic;
    private int _good;
    private int _click;
    private int _reply;

    public TribeHotBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public int get_good() {
        return _good;
    }

    public void set_good(int _good) {
        this._good = _good;
    }

    public int get_click() {
        return _click;
    }

    public void set_click(int _click) {
        this._click = _click;
    }

    public int get_reply() {
        return _reply;
    }

    public void set_reply(int _reply) {
        this._reply = _reply;
    }

    public static ArrayList<TribeHotBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        ArrayList<TribeHotBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                TribeHotBean bean = new TribeHotBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "good";
                if (object.has(index)){
                    bean.set_good(object.optInt(index));
                }
                index = "click";
                if (object.has(index)){
                    bean.set_click(object.optInt(index));
                }
                index = "reply";
                if (object.has(index)){
                    bean.set_reply(object.optInt(index));
                }
                beans.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
