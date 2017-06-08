package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainSubContent implements Parcelable{
    private String _name;
    private ArrayList<MaintainDetailContent> _sub;
    private String _content;
    private String _price;
    private int _sel;
    private boolean select;

    public MaintainSubContent() {
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ArrayList<MaintainDetailContent> get_sub() {
        return _sub;
    }

    public void set_sub(ArrayList<MaintainDetailContent> _sub) {
        this._sub = _sub;
    }

    public int get_sel() {
        return _sel;
    }

    public void set_sel(int _sel) {
        this._sel = _sel;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_price() {
        return _price;
    }

    public void set_price(String _price) {
        this._price = _price;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public static ArrayList<MaintainSubContent> fromString(String resource){
        ArrayList<MaintainSubContent> subContents = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                MaintainSubContent subContent = new MaintainSubContent();
                JSONObject object = array.getJSONObject(i);
                String index = "name";
                if (object.has(index)){
                    subContent.set_name(object.optString( index));
                }
                index = "sub";
                if (object.has(index)){
                    String res = object.optString(index);
                    subContent.set_sub(MaintainDetailContent.fromString(res));
                }
                index = "sel";
                if (object.has(index)){
                    int selId = object.optInt(index);
                    subContent.set_sel(selId);
                    subContent.setSelect(selId > 0);
                }
                subContents.add(subContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subContents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MaintainSubContent> CREATOR = new Parcelable.Creator<MaintainSubContent>(){

        @Override
        public MaintainSubContent createFromParcel(Parcel source) {
            return new MaintainSubContent(source);
        }

        @Override
        public MaintainSubContent[] newArray(int size) {
            return new MaintainSubContent[size];
        }
    };

    private MaintainSubContent(Parcel source){
        _name = source.readString();
        Parcelable[] array = source.readParcelableArray(MaintainDetailContent.class.getClassLoader());
        _sub = new ArrayList<>();
        for (int i = 0; i < array.length; i++){
            _sub.add((MaintainDetailContent) array[i]);
        }
        _content = source.readString();
        _price = source.readString();
        _sel = source.readInt();
        select = source.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_name);
        MaintainDetailContent[] array = new MaintainDetailContent[_sub.size()];
        for (int i = 0; i < _sub.size(); i++){
            array[i] = _sub.get(i);
        }
        dest.writeParcelableArray(array, flags);
        dest.writeString(_content);
        dest.writeString(_price);
        dest.writeInt(_sel);
        dest.writeInt(select ? 1 : 0);
    }
}
