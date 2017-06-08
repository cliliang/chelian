package com.ceyu.carsteward.maintain.bean;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderList {
    private ArrayList<MaintainOrderListBean> _list;
    private boolean _haveMore;

    public MaintainOrderList() {
    }

    public ArrayList<MaintainOrderListBean> get_list() {
        return _list;
    }

    public void set_list(ArrayList<MaintainOrderListBean> _list) {
        this._list = _list;
    }

    public boolean is_haveMore() {
        return _haveMore;
    }

    public void set_haveMore(boolean _haveMore) {
        this._haveMore = _haveMore;
    }

    public static MaintainOrderList fromJson(JSONObject object){
        MaintainOrderList orderList = new MaintainOrderList();
        String index = "list";
        if (object.has(index)){
            String resource = object.optString(index);
            orderList.set_list(MaintainOrderListBean.fromString(resource));
        }
        index = "more";
        if (object.has(index)){
            orderList.set_haveMore(object.optInt(index) == 1);
        }
        return orderList;
    }
}
