package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainDetailContent implements Parcelable{
    private int _id;
    private String _info;
    private String _money;
    private String _human;
    private String _sum;
    private String _moneyTxt;

    public MaintainDetailContent() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public String get_human() {
        return _human;
    }

    public void set_human(String _human) {
        this._human = _human;
    }

    public String get_sum() {
        return _sum;
    }

    public void set_sum(String _sum) {
        this._sum = _sum;
    }

    public String get_moneyTxt() {
        return _moneyTxt;
    }

    public void set_moneyTxt(String _moneyTxt) {
        this._moneyTxt = _moneyTxt;
    }

    public static ArrayList<MaintainDetailContent> fromString(String resource){
        ArrayList<MaintainDetailContent> contents = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                MaintainDetailContent content = new MaintainDetailContent();
                String index = "id";
                if (object.has(index)){
                    content.set_id(object.optInt(index));
                }
                index = "info";
                if (object.has(index)){
                    String info = object.optString(index);
                    if (!info.equals("null")){
                        content.set_info(info);
                    }
                }
                index = "money";
                if (object.has(index)){
                    content.set_money(object.optString(index));
                }
                index = "human";
                if (object.has(index)){
                    content.set_human(object.optString(index));
                }
                index = "sum";
                if (object.has(index)){
                    content.set_sum(object.optString(index));
                }
                index = "money_txt";
                if (object.has(index)){
                    content.set_moneyTxt(object.optString(index));
                }
                contents.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MaintainDetailContent> CREATOR = new Parcelable.Creator<MaintainDetailContent>(){

        @Override
        public MaintainDetailContent createFromParcel(Parcel source) {
            return new MaintainDetailContent(source);
        }

        @Override
        public MaintainDetailContent[] newArray(int size) {
            return new MaintainDetailContent[size];
        }
    };

    private MaintainDetailContent(Parcel source){
        _id = source.readInt();
        _info = source.readString();
        _money = source.readString();
        _human = source.readString();
        _sum = source.readString();
        _moneyTxt = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_info);
        dest.writeString(_money);
        dest.writeString(_human);
        dest.writeString(_sum);
        dest.writeString(_moneyTxt);
    }
}
