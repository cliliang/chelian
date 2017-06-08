package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/8/6.
 */
public class MaintainPayBean implements Parcelable {

    private String _sum;
    private String sum_txt;
    private String _human;
    private String _human_txt;
    private String _factory_txt;


    public MaintainPayBean() {
    }

    public String get_sum() {
        return _sum;
    }

    public void set_sum(String _sum) {
        this._sum = _sum;
    }

    public String getSum_txt() {
        return sum_txt;
    }

    public void setSum_txt(String sum_txt) {
        this.sum_txt = sum_txt;
    }

    public String get_human() {
        return _human;
    }

    public void set_human(String _human) {
        this._human = _human;
    }

    public String get_human_txt() {
        return _human_txt;
    }

    public void set_human_txt(String _human_txt) {
        this._human_txt = _human_txt;
    }

    public String get_factory_txt() {
        return _factory_txt;
    }

    public void set_factory_txt(String _factory_txt) {
        this._factory_txt = _factory_txt;
    }

    public static MaintainPayBean fromString(String resource){
        try {
            JSONObject object = new JSONObject(resource);
            MaintainPayBean payBean = new MaintainPayBean();
            String index = "sum";
            if (object.has(index)){
                payBean.set_sum(object.optString(index));
            }
            index = "sum_txt";
            if (object.has(index)){
                payBean.setSum_txt(object.optString(index));
            }
            index = "human";
            if (object.has(index)){
                payBean.set_human(object.optString(index));
            }
            index = "human_txt";
            if (object.has(index)){
                payBean.set_human_txt(object.optString(index));
            }
            index = "factory_txt";
            if (object.has(index)){
                payBean.set_factory_txt(object.optString(index));
            }
            return payBean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Parcelable.Creator<MaintainPayBean> CREATOR = new Parcelable.Creator<MaintainPayBean>(){

        @Override
        public MaintainPayBean createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public MaintainPayBean[] newArray(int size) {
            return new MaintainPayBean[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
