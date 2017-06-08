package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainOnlinePay implements Parcelable{
    private String _txt;
    private float _num;

    public MaintainOnlinePay() {
    }

    public String get_txt() {
        return _txt;
    }

    public void set_txt(String _txt) {
        this._txt = _txt;
    }

    public float get_num() {
        return _num;
    }

    public void set_num(float _num) {
        this._num = _num;
    }

    public static MaintainOnlinePay fromString(String resource){
        MaintainOnlinePay pay = new MaintainOnlinePay();
        try {
            JSONObject object = new JSONObject(resource);
            String index = "txt";
            if (object.has(index)){
                pay.set_txt(object.optString(index));
            }
            index = "num";
            if (object.has(index)){
                pay.set_num((float)object.optDouble(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MaintainOnlinePay> CREATOR = new Parcelable.Creator<MaintainOnlinePay>(){

        @Override
        public MaintainOnlinePay createFromParcel(Parcel source) {
            return new MaintainOnlinePay(source);
        }

        @Override
        public MaintainOnlinePay[] newArray(int size) {
            return new MaintainOnlinePay[size];
        }
    };

    private MaintainOnlinePay(Parcel source){
        _txt = source.readString();
        _num = source.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_txt);
        dest.writeFloat(_num);
    }
}
