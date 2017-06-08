package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/9/16.
 */
public class SelfMyLocalBean {
    private double _lat;
    private double _lon;

    public SelfMyLocalBean() {
    }

    public double get_lat() {
        return _lat;
    }

    public void set_lat(double _lat) {
        this._lat = _lat;
    }

    public double get_lon() {
        return _lon;
    }

    public void set_lon(double _lon) {
        this._lon = _lon;
    }

    public static SelfMyLocalBean fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        SelfMyLocalBean bean = new SelfMyLocalBean();
        try {
            JSONObject object = new JSONObject(resource);
            String index = "lat";
            if (object.has(index)){
                bean.set_lat(object.optDouble(index));
            }
            index = "lon";
            if (object.has(index)){
                bean.set_lon(object.optDouble(index));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
