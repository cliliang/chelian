package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/23.
 */
public class MileageBean implements Parcelable{
    private int _num;
    private String[] _parts;
    private String _factory;
    private String _discount;

    public MileageBean() {
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public String[] get_parts() {
        return _parts;
    }

    public void set_parts(String[] _parts) {
        this._parts = _parts;
    }

    public String get_factory() {
        return _factory;
    }

    public void set_factory(String _factory) {
        this._factory = _factory;
    }

    public String get_discount() {
        return _discount;
    }

    public void set_discount(String _discount) {
        this._discount = _discount;
    }

    public static ArrayList<MileageBean> fromJsonArray(JSONArray array){
        ArrayList<MileageBean> beans = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                MileageBean bean = new MileageBean();
                String index = "num";
                if (object.has(index)){
                    bean.set_num(object.optInt(index));
                }
                index = "parts";
                if (object.has(index)){
                    String partsString = object.optString(index);
                    String[] parts = partsString.replace("[", "").replace("]", "").replaceAll("\"", "").split(",");
                    bean.set_parts(parts);
                }
                index = "factory";
                if (object.has(index)){
                    bean.set_factory(object.optString(index));
                }
                index = "discount";
                if (object.has(index)){
                    bean.set_discount(object.optString(index));
                }
                beans.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beans;
    }

    public static final Parcelable.Creator<MileageBean> CREATOR = new Parcelable.Creator<MileageBean>(){

        @Override
        public MileageBean createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public MileageBean[] newArray(int size) {
            return new MileageBean[0];
        }
    };

    public MileageBean(Parcel source){
        _num = source.readInt();
        _parts = source.createStringArray();
        _factory = source.readString();
        _discount = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_num);
        dest.writeStringArray(_parts);
        dest.writeString(_factory);
        dest.writeString(_discount);
    }
}
