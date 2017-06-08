package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.tuan.bean.TuanListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/8/17.
 */
public class ShopInfo {
    private ShopPhotos _pic;
    private String _name;
    private String _address;
    private String _content;
    private String _characteristic;
    private String _distance;
    private List<TuanListBean> _group;

    public ShopInfo() {
    }

    public ShopPhotos get_pic() {
        return _pic;
    }

    public void set_pic(ShopPhotos _pic) {
        this._pic = _pic;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_characteristic() {
        return _characteristic;
    }

    public void set_characteristic(String _characteristic) {
        this._characteristic = _characteristic;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public List<TuanListBean> get_group() {
        return _group;
    }

    public void set_group(List<TuanListBean> _group) {
        this._group = _group;
    }

    public static ShopInfo fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        ShopInfo info = new ShopInfo();
        String index = "pic";
        if (object.has(index)){
            ShopPhotos photos = ShopPhotos.fromString(object.optString(index));
            if (photos != null){
                info.set_pic(photos);
            }
        }
        index = "name";
        if (object.has(index)){
            info.set_name(object.optString(index));
        }
        index = "address";
        if (object.has(index)){
            info.set_address(object.optString(index));
        }
        index = "content";
        if (object.has(index)){
            info.set_content(object.optString(index));
        }
        index = "characteristic";
        if (object.has(index)){
            String res = object.optString(index);
            try {
                JSONArray array = new JSONArray(res);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < array.length(); i++){
                    builder.append(String.valueOf(i + 1));
                    builder.append(".");
                    builder.append(array.getString(i));
                    if (i != array.length() - 1){
                        builder.append("\n");
                    }
                }
                info.set_characteristic(builder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        index = "distance";
        if (object.has(index)){
            info.set_distance(object.optString(index));
        }
        index = "group";
        if (object.has(index)){
            List<TuanListBean> listBeans = TuanListBean.fromString(object.optString(index));
            if (listBeans != null){
                info.set_group(listBeans);
            }
        }
        return info;
    }
}
