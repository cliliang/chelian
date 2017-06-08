package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/17.
 */
public class ShopPhotoDescribe implements Parcelable{
    private String _url;
    private String _info;

    public ShopPhotoDescribe() {
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public static ArrayList<ShopPhotoDescribe> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONArray array = new JSONArray(resource);
            ArrayList<ShopPhotoDescribe> describes = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                ShopPhotoDescribe describe = new ShopPhotoDescribe();
                JSONObject object = array.getJSONObject(i);
                String index = "url";
                if (object.has(index)){
                    describe.set_url(object.optString(index));
                }
                index = "info";
                if (object.has(index)){
                    describe.set_info(object.optString(index));
                }
                describes.add(describe);
            }
            return describes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ShopPhotoDescribe> CREATOR = new Parcelable.Creator<ShopPhotoDescribe>(){

        @Override
        public ShopPhotoDescribe createFromParcel(Parcel source) {
            return new ShopPhotoDescribe(source);
        }

        @Override
        public ShopPhotoDescribe[] newArray(int size) {
            return new ShopPhotoDescribe[size];
        }
    };

    public ShopPhotoDescribe(Parcel dest){
        _url = dest.readString();
        _info = dest.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_url);
        dest.writeString(_info);
    }
}
