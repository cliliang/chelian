package com.ceyu.carsteward.self.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/16.
 */
public class SelfShopLocalBean implements Parcelable{

    private String _id;
    private String _name;
    private String _address;
    private double _lat;
    private double _lon;
    private String _distance;

    public SelfShopLocalBean() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public static List<SelfShopLocalBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        List<SelfShopLocalBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                SelfShopLocalBean bean = new SelfShopLocalBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optString(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                index = "address";
                if (object.has(index)){
                    bean.set_address(object.optString(index));
                }
                index = "lat";
                if (object.has(index)){
                    bean.set_lat(object.optDouble(index));
                }
                index = "lon";
                if (object.has(index)){
                    bean.set_lon(object.optDouble(index));
                }
                index = "distance";
                if (object.has(index)){
                    bean.set_distance(object.optString(index));
                }
                beans.add(bean);
            }
            return beans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<SelfShopLocalBean> CREATOR = new Parcelable.Creator<SelfShopLocalBean>(){

        @Override
        public SelfShopLocalBean createFromParcel(Parcel source) {
            return new SelfShopLocalBean(source);
        }

        @Override
        public SelfShopLocalBean[] newArray(int size) {
            return new SelfShopLocalBean[size];
        }
    };

    public SelfShopLocalBean(Parcel source){
        _id = source.readString();
        _name = source.readString();
        _address = source.readString();
        _lat = source.readDouble();
        _lon = source.readDouble();
        _distance = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(_name);
        dest.writeString(_address);
        dest.writeDouble(_lat);
        dest.writeDouble(_lon);
        dest.writeString(_distance);
    }
}
