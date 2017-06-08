package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/17.
 */
public class ShopPhotos {
    private int _num;
    private ArrayList<ShopPhotoDescribe> _list;

    public ShopPhotos() {
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public ArrayList<ShopPhotoDescribe> get_list() {
        return _list;
    }

    public void set_list(ArrayList<ShopPhotoDescribe> _list) {
        this._list = _list;
    }

    public static ShopPhotos fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(resource);
            ShopPhotos photos = new ShopPhotos();
            String index = "num";
            if (object.has(index)){
                photos.set_num(object.optInt(index));
            }
            index = "list";
            if (object.has(index)){
                photos.set_list(ShopPhotoDescribe.fromString(object.optString(index)));
            }
            return photos;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
