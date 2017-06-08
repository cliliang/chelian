package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/9/22.
 */
public class SelfStoreBean {
    private String _name;
    private String _address;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public static SelfStoreBean fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(resource);
            SelfStoreBean bean = new SelfStoreBean();
            String index = "name";
            if (object.has(index)){
                bean.set_name(object.optString(index));
            }
            index = "address";
            if (object.has(index)){
                bean.set_address(object.optString(index));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
