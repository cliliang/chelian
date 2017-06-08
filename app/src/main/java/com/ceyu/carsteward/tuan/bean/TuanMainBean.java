package com.ceyu.carsteward.tuan.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/7/20.
 */
public class TuanMainBean {
    private List<TuanListBean> listBeans;
    private Boolean haveNext;

    public TuanMainBean() {
    }

    public List<TuanListBean> getListBeans() {
        return listBeans;
    }

    public void setListBeans(List<TuanListBean> listBeans) {
        this.listBeans = listBeans;
    }

    public Boolean getHaveNext() {
        return haveNext;
    }

    public void setHaveNext(Boolean haveNext) {
        this.haveNext = haveNext;
    }

    public static TuanMainBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        TuanMainBean bean = new TuanMainBean();
        String index = "list";
        if (object.has(index)){
            String res = object.optString(index);
            List<TuanListBean> listBeans = TuanListBean.fromString(res);
            if (listBeans != null){
                bean.setListBeans(listBeans);
            }
        }
        index = "more";
        if (object.has(index)){
            bean.setHaveNext(object.optInt(index) == 1);
        }
        return bean;
    }
}
