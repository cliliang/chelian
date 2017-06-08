package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/11.
 */
public class SelfCommodityBean {
    private int _id;
    private String _subject;
    private String _info;
    private String _pic;
    private String _content;
    private int _material;
    private int _human;

    public SelfCommodityBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public int get_material() {
        return _material;
    }

    public void set_material(int _material) {
        this._material = _material;
    }

    public int get_human() {
        return _human;
    }

    public void set_human(int _human) {
        this._human = _human;
    }

    public static List<SelfCommodityBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONArray array = new JSONArray(resource);
            List<SelfCommodityBean> beans = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                SelfCommodityBean bean = new SelfCommodityBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "subject";
                if (object.has(index)){
                    bean.set_subject(object.optString(index));
                }
                index = "info";
                if (object.has(index)){
                    bean.set_info(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "content";
                if (object.has(index)){
                    bean.set_content(object.optString(index));
                }
                index = "material";
                if (object.has(index)){
                    bean.set_material(object.optInt(index));
                }
                index = "human";
                if (object.has(index)){
                    bean.set_human(object.optInt(index));
                }
                beans.add(bean);
            }
            return beans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
