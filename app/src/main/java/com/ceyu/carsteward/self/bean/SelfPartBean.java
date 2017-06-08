package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/22.
 */
public class SelfPartBean {
    private String _name;
    private String _info;
    private String _money;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
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

    public static List<SelfPartBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        List<SelfPartBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                SelfPartBean bean = new SelfPartBean();
                String index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                index = "info";
                if (object.has(index)){
                    bean.set_info(object.optString(index));
                }
                index = "money";
                if (object.has(index)){
                    bean.set_money(object.optString(index));
                }
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
