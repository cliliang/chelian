package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/24.
 */
public class MaintainListBean {
    private int _num;
    private ArrayList<ListShopBean> shopBeans;

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public ArrayList<ListShopBean> getShopBeans() {
        return shopBeans;
    }

    public void setShopBeans(ArrayList<ListShopBean> shopBeans) {
        this.shopBeans = shopBeans;
    }

    public static MaintainListBean fromJson(JSONObject object){
        MaintainListBean bean = new MaintainListBean();
        String index = "num";
        if (object.has(index)){
            bean.set_num(object.optInt(index));
        }
        index = "list";
        if (object.has(index)){
            String beansString = object.optString(index);
            if (!StringUtils.isEmpty(beansString)){
                bean.setShopBeans(ListShopBean.fromJsonString(beansString));
            }
        }
        return bean;
    }
}
