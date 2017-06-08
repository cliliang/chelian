package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.maintain.router.MaintainRouter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainRule implements Parcelable{
    private String _start;
    private String _end;
    private String _info1;
    private String _info2;
    private String _info3;

    public MaintainRule() {
    }

    public String get_start() {
        return _start;
    }

    public void set_start(String _start) {
        this._start = _start;
    }

    public String get_end() {
        return _end;
    }

    public void set_end(String _end) {
        this._end = _end;
    }

    public String get_info1() {
        return _info1;
    }

    public void set_info1(String _info1) {
        this._info1 = _info1;
    }

    public String get_info2() {
        return _info2;
    }

    public void set_info2(String _info2) {
        this._info2 = _info2;
    }

    public String get_info3() {
        return _info3;
    }

    public void set_info3(String _info3) {
        this._info3 = _info3;
    }

    public static MaintainRule fromString(String resource){
        MaintainRule rule = new MaintainRule();
        try {
            JSONObject object = new JSONObject(resource);
            String index = "start";
            if (object.has(index)){
                rule.set_start(object.optString(index));
            }
            index = "end";
            if (object.has(index)){
                rule.set_end(object.optString(index));
            }
            index = "info1";
            if (object.has(index)){
                rule.set_info1(object.optString(index));
            }
            index = "info2";
            if (object.has(index)){
                rule.set_info2(object.optString(index));
            }
            index = "info3";
            if (object.has(index)){
                rule.set_info3(object.optString(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rule;
    }

    public static final Parcelable.Creator<MaintainRule> CREATOR = new Parcelable.Creator<MaintainRule>(){

        @Override
        public MaintainRule createFromParcel(Parcel source) {
            return new MaintainRule(source);
        }

        @Override
        public MaintainRule[] newArray(int size) {
            return new MaintainRule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_start);
        dest.writeString(_end);
        dest.writeString(_info1);
        dest.writeString(_info2);
        dest.writeString(_info3);
    }

    private MaintainRule(Parcel dest){
        _start = dest.readString();
        _end = dest.readString();
        _info1 = dest.readString();
        _info2 = dest.readString();
        _info3 = dest.readString();
    }
}
