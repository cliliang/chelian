package com.ceyu.carsteward.tribe.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/9/6.
 */
public class TribeDetailList {
    private int _num;
    private ArrayList<TribeDetailListBean> details;

    public TribeDetailList(int _num, ArrayList<TribeDetailListBean> details) {
        this._num = _num;
        this.details = details;
    }

    public TribeDetailList() {
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public ArrayList<TribeDetailListBean> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TribeDetailListBean> details) {
        this.details = details;
    }

    public static TribeDetailList fromString(String resourse){
        if (StringUtils.isEmpty(resourse)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(resourse);
            TribeDetailList bean = new TribeDetailList();
            String index = "num";
            if (object.has(index)){
                bean.set_num(object.optInt(index));
            }
            index = "list";
            if (object.has(index)){
                String res = object.optString(index);
                ArrayList<TribeDetailListBean> beans = TribeDetailListBean.fromString(res);
                if (beans != null){
                    bean.setDetails(beans);
                }
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
