package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/9/22.
 */
public class SelfOrderMechanicBean {
    private String _name;
    private String[] _free;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String[] get_free() {
        return _free;
    }

    public void set_free(String[] _free) {
        this._free = _free;
    }

    public static SelfOrderMechanicBean fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(resource);
            SelfOrderMechanicBean bean = new SelfOrderMechanicBean();
            String index = "name";
            if (object.has(index)){
                bean.set_name(object.optString(index));
            }
            index = "free";
            if (object.has(index)){
                String res = object.optString(index);
                bean.set_free(res.split(","));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
