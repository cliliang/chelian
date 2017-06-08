package com.ceyu.carsteward.tribe.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/28.
 */
public class TribeHomeBean {
    private int _id;
    private String _title;
    private String _pic;
    private String _uid;
    private String _uname;
    private String _upic;
    private int _good;
    private int _click;
    private int _reply;

    public TribeHomeBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

    public String get_uname() {
        return _uname;
    }

    public void set_uname(String _uname) {
        this._uname = _uname;
    }

    public String get_upic() {
        return _upic;
    }

    public void set_upic(String _upic) {
        this._upic = _upic;
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

    public static ArrayList<TribeHomeBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        ArrayList<TribeHomeBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                TribeHomeBean bean = new TribeHomeBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "title";
                if (object.has(index)){
                    bean.set_title(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "uid";
                if (object.has(index)){
                    bean.set_uid(object.optString(index));
                }
                index = "uname";
                if (object.has(index)){
                    bean.set_uname(object.optString(index));
                }
                index = "upic";
                if (object.has(index)){
                    bean.set_upic(object.optString(index));
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
