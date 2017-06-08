package com.ceyu.carsteward.car.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/17.
 */
public class InsuranceBean implements Parcelable{
    private int _id;
    private String _name;
    private String _phone;

    public InsuranceBean() {
    }

    public InsuranceBean(int _id, String _name, String _phone) {
        this._id = _id;
        this._name = _name;
        this._phone = _phone;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public static ArrayList<InsuranceBean> fromJsonArray(JSONArray array){
        ArrayList<InsuranceBean> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                InsuranceBean bean = new InsuranceBean();
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                index = "phone";
                if (object.has(index)){
                    bean.set_phone(object.optString(index));
                }
                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_phone);
    }

    public static final Parcelable.Creator<InsuranceBean> CREATOR = new Parcelable.Creator<InsuranceBean>(){

        @Override
        public InsuranceBean createFromParcel(Parcel source) {
            return new InsuranceBean(source);
        }

        @Override
        public InsuranceBean[] newArray(int size) {
            return new InsuranceBean[size];
        }
    };

    private InsuranceBean(Parcel source){
        _id = source.readInt();
        _name = source.readString();
        _phone = source.readString();
    }
}
