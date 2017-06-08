package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/17.
 */
public class SelfMechanicBean {
    private String _token;
    private String _name;
    private int _year;
    private String _model;
    private String _pic;
    private String[] _photo;
    private SelfMechanicComments _comment;
    private String _free;

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_year() {
        return _year;
    }

    public void set_year(int _year) {
        this._year = _year;
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

    public String[] get_photo() {
        return _photo;
    }

    public void set_photo(String[] _photo) {
        this._photo = _photo;
    }

    public SelfMechanicComments get_comment() {
        return _comment;
    }

    public void set_comment(SelfMechanicComments _comment) {
        this._comment = _comment;
    }

    public String get_free() {
        return _free;
    }

    public void set_free(String _free) {
        this._free = _free;
    }

    public static SelfMechanicBean fromRes(String res){
        if (StringUtils.isEmpty(res)){
            return null;
        }
        SelfMechanicBean bean = new SelfMechanicBean();
        try {
            JSONObject object = new JSONObject(res);
            String index = "name";
            if (object.has(index)){
                bean.set_name(object.optString(index));
            }
            index = "token";
            if (object.has(index)){
                bean.set_token(object.optString(index));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SelfMechanicBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONArray array = new JSONArray( resource);
            List<SelfMechanicBean> beans = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                SelfMechanicBean bean = new SelfMechanicBean();
                String index = "token";
                if (object.has(index)){
                    bean.set_token(object.optString(index));
                }
                index = "name";
                if (object.has(index)){
                    bean.set_name(object.optString(index));
                }
                index = "year";
                if (object.has(index)){
                    bean.set_year(object.optInt(index));
                }
                index = "model";
                if (object.has(index)){
                    bean.set_model(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "photo";
                if (object.has(index)){
                    String photoString = object.optString(index);
                    String[] arrayString = photoString.split("\\|");
                    String[] photoArray = new String[arrayString.length];
                    for (int j = 0; j < arrayString.length; j++){
                        photoArray[j] = arrayString[j].split(",")[0];
                    }
                    bean.set_photo(photoArray);
                }
                index = "comment";
                if (object.has(index)){
                    String res = object.optString(index);
                    SelfMechanicComments commonBeans = SelfMechanicComments.fromString(res);
                    if (commonBeans != null){
                        bean.set_comment(commonBeans);
                    }
                }
                index = "free";
                if (object.has(index)){
                    bean.set_free(object.optString(index));
                }
                beans.add(bean);
            }
            return beans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
