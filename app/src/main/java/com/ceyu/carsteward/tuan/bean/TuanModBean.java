package com.ceyu.carsteward.tuan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.tuan.TuanMod;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/7/20.
 */
public class TuanModBean implements Parcelable{
    private String _name;
    private String _pic;
    private String _address;
    private String _class;

    public TuanModBean() {
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public static TuanModBean fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        TuanModBean modBean = new TuanModBean();
        try {
            JSONObject object = new JSONObject(resource);
            String index = "name";
            if (object.has(index)){
                modBean.set_name(object.optString(index));
            }
            index = "pic";
            if (object.has(index)){
                modBean.set_pic(object.optString(index));
            }
            index = "address";
            if (object.has(index)){
                modBean.set_address(object.optString(index));
            }
            index = "class";
            if (object.has(index)){
                modBean.set_class(object.optString(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TuanModBean> CREATOR = new Parcelable.Creator<TuanModBean>(){

        @Override
        public TuanModBean createFromParcel(Parcel source) {
            return new TuanModBean(source);
        }

        @Override
        public TuanModBean[] newArray(int size) {
            return new TuanModBean[size];
        }
    };

    private TuanModBean(Parcel source){
        _name = source.readString();
        _pic = source.readString();
        _address = source.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_name);
        dest.writeString(_pic);
        dest.writeString(_address);
    }
}
