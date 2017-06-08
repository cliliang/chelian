package com.ceyu.carsteward.tribe.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/9/6.
 */
public class TribeDetailListBean {
    private String _uname;
    private String _uid;
    private String _upic;
    private String _date;
    private String _info;

    public TribeDetailListBean() {
    }

    public String get_uname() {
        return _uname;
    }

    public void set_uname(String _uname) {
        this._uname = _uname;
    }

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

    public String get_upic() {
        return _upic;
    }

    public void set_upic(String _upic) {
        this._upic = _upic;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }

    public static ArrayList<TribeDetailListBean> fromString(String resourse){
        if (StringUtils.isEmpty(resourse)){
            return null;
        }
        ArrayList<TribeDetailListBean> listBeans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resourse);
            for (int i = 0; i < array.length(); i++){
                TribeDetailListBean bean = new TribeDetailListBean();
                JSONObject object = array.getJSONObject(i);
                String index = "uname";
                if (object.has(index)){
                    bean.set_uname(object.optString(index));
                }
                index = "uid";
                if (object.has(index)){
                    bean.set_uid(object.optString(index));
                }
                index = "upic";
                if (object.has(index)){
                    bean.set_upic(object.optString(index));
                }
                index = "date";
                if (object.has(index)){
                    bean.set_date(object.optString(index));
                }
                index = "info";
                if (object.has(index)){
                    bean.set_info(object.optString(index));
                }
                listBeans.add(bean);
            }
            return listBeans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
