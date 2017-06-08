package com.ceyu.carsteward.engineer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/11.
 */
public class EngineerOrderInfo implements Parcelable{
    private String _class;
    private String _date;
    private String _sn;
    private String _orderState;
    private int _orderStateCode;
    private String _name;
    private String _level;
    private String _model;
    private String _pic;
    private float _money;
    private String _token;
    private boolean _onLine;
    private int _long;
    private boolean _comment;

    public EngineerOrderInfo() {
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_sn() {
        return _sn;
    }

    public void set_sn(String _sn) {
        this._sn = _sn;
    }

    public String get_orderState() {
        return _orderState;
    }

    public void set_orderState(String _orderState) {
        this._orderState = _orderState;
    }

    public int get_orderStateCode() {
        return _orderStateCode;
    }

    public void set_orderStateCode(int _orderStateCode) {
        this._orderStateCode = _orderStateCode;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_level() {
        return _level;
    }

    public void set_level(String _level) {
        this._level = _level;
    }

    public String get_model() {
        return _model;
    }

    public void set_model(String _model) {
        this._model = _model;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public float get_money() {
        return _money;
    }

    public void set_money(float _money) {
        this._money = _money;
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public boolean is_onLine() {
        return _onLine;
    }

    public void set_onLine(boolean _onLine) {
        this._onLine = _onLine;
    }

    public int get_long() {
        return _long;
    }

    public void set_long(int _long) {
        this._long = _long;
    }

    public boolean is_comment() {
        return _comment;
    }

    public void set_comment(boolean _comment) {
        this._comment = _comment;
    }

    public static ArrayList<EngineerOrderInfo> fromString(String resource){
        ArrayList<EngineerOrderInfo> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                EngineerOrderInfo info = new EngineerOrderInfo();
                String index = "class";
                if (object.has(index)){
                    info.set_class(object.optString(index));
                }
                index = "date";
                if (object.has(index)){
                    info.set_date(object.optString(index));
                }
                index = "sn";
                if (object.has(index)){
                    info.set_sn(object.optString(index));
                }
                index = "orderState";
                if (object.has(index)){
                    info.set_orderState(object.optString(index));
                }
                index = "orderStateCode";
                if (object.has(index)){
                    info.set_orderStateCode(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    info.set_name(object.optString(index));
                }
                index = "level";
                if (object.has(index)){
                    info.set_level(object.optString(index));
                }
                index = "model";
                if (object.has(index)){
                    info.set_model(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    info.set_pic(object.optString(index));
                }
                index = "money";
                if (object.has(index)){
                    info.set_money((float) object.optDouble(index));
                }
                index = "token";
                if (object.has(index)){
                    info.set_token(object.optString(index));
                }
                index = "online";
                if (object.has(index)){
                    info.set_onLine(object.optInt(index) == 1);
                }
                index = "long";
                if (object.has(index)){
                    info.set_long(object.optInt(index));
                }
                index = "comment";
                if (object.has(index)){
                    info.set_comment(object.optInt(index) == 1);
                }
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_class);
        dest.writeString(_date);
        dest.writeString(_sn);
        dest.writeInt(_orderStateCode);
        dest.writeString(_orderState);
        dest.writeString(_name);
        dest.writeString(_level);
        dest.writeString(_model);
        dest.writeString(_pic);
        dest.writeFloat(_money);
        dest.writeString(_token);
        dest.writeInt(_onLine ? 1 : 0);
        dest.writeInt(_comment ? 1 : 0);

    }

    public static final Parcelable.Creator<EngineerOrderInfo> CREATOR = new Parcelable.Creator<EngineerOrderInfo>(){

        @Override
        public EngineerOrderInfo createFromParcel(Parcel source) {
            return new EngineerOrderInfo(source);
        }

        @Override
        public EngineerOrderInfo[] newArray(int size) {
            return new EngineerOrderInfo[size];
        }
    };

    private EngineerOrderInfo(Parcel source){
        _class = source.readString();
        _date = source.readString();
        _sn = source.readString();
        _orderStateCode = source.readInt();
        _orderState = source.readString();
        _name = source.readString();
        _level = source.readString();
        _model = source.readString();
        _pic = source.readString();
        _money = source.readFloat();
        _token = source.readString();
        _onLine = source.readInt() == 1;
        _comment = source.readInt() == 1;
    }
}
