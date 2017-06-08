package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/29.
 */
public class MaintainCarInfo implements Parcelable{
    private CarBrandInfoBean _model;
    private String _name;
    private String _plate;
    private String _frame;
    private String _motor;

    public MaintainCarInfo() {
    }

    public CarBrandInfoBean get_model() {
        return _model;
    }

    public void set_model(CarBrandInfoBean _model) {
        this._model = _model;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_plate() {
        return _plate;
    }

    public void set_plate(String _plate) {
        this._plate = _plate;
    }

    public String get_frame() {
        return _frame;
    }

    public void set_frame(String _frame) {
        this._frame = _frame;
    }

    public String get_motor() {
        return _motor;
    }

    public void set_motor(String _motor) {
        this._motor = _motor;
    }

    public static MaintainCarInfo fromString(String resource){
        MaintainCarInfo carInfo = new MaintainCarInfo();
        try {
            JSONObject object = new JSONObject(resource);
            String index = "model";
            if (object.has(index)){
                carInfo.set_model(CarBrandInfoBean.fromString(object.optString(index)));
            }
            index = "name";
            if (object.has(index)){
                carInfo.set_name(object.optString(index));
            }
            index = "plate";
            if (object.has(index)){
                carInfo.set_plate(object.optString(index));
            }
            index = "frame";
            if (object.has(index)){
                carInfo.set_frame(object.optString(index));
            }
            index = "motor";
            if (object.has(index)){
                carInfo.set_motor(object.optString(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carInfo;
    }

    public static final Parcelable.Creator<MaintainCarInfo> CREATOR = new Parcelable.Creator<MaintainCarInfo>(){

        @Override
        public MaintainCarInfo createFromParcel(Parcel source) {
            return new MaintainCarInfo(source);
        }

        @Override
        public MaintainCarInfo[] newArray(int size) {
            return new MaintainCarInfo[size];
        }
    };

    private MaintainCarInfo(Parcel source){
        _model = source.readParcelable(CarBrandInfoBean.class.getClassLoader());
        _name = source.readString();
        _plate = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(_model, flags);
        dest.writeString(_name);
        dest.writeString(_plate);
    }
}
