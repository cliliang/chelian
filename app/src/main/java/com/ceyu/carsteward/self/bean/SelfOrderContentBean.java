package com.ceyu.carsteward.self.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/9/22.
 */
public class SelfOrderContentBean {
    private float _money;
    private List<SelfPartBean> partsBean;
    private SelfStoreBean storeBean;
    private SelfCarBean carBean;
    private SelfOrderMechanicBean mechanicBean;

    public float get_money() {
        return _money;
    }

    public void set_money(float _money) {
        this._money = _money;
    }

    public List<SelfPartBean> getPartsBean() {
        return partsBean;
    }

    public void setPartsBean(List<SelfPartBean> partsBean) {
        this.partsBean = partsBean;
    }

    public SelfStoreBean getStoreBean() {
        return storeBean;
    }

    public void setStoreBean(SelfStoreBean storeBean) {
        this.storeBean = storeBean;
    }

    public SelfCarBean getCarBean() {
        return carBean;
    }

    public void setCarBean(SelfCarBean carBean) {
        this.carBean = carBean;
    }

    public SelfOrderMechanicBean getMechanicBean() {
        return mechanicBean;
    }

    public void setMechanicBean(SelfOrderMechanicBean mechanicBean) {
        this.mechanicBean = mechanicBean;
    }

    public static SelfOrderContentBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        SelfOrderContentBean bean = new SelfOrderContentBean();
        String index = "money";
        if (object.has(index)){
            bean.set_money((float) object.optDouble(index));
        }
        index = "parts";
        if (object.has(index)){
            List<SelfPartBean> partBeans = SelfPartBean.fromString(object.optString(index));
            if (partBeans != null){
                bean.setPartsBean(partBeans);
            }
        }
        index = "store";
        if (object.has(index)){
            SelfStoreBean storeBean = SelfStoreBean.fromString(object.optString(index));
            if (storeBean != null){
                bean.setStoreBean(storeBean);
            }
        }
        index = "car";
        if (object.has(index)){
            SelfCarBean carBean = SelfCarBean.fromString(object.optString(index));
            if (carBean != null){
                bean.setCarBean(carBean);
            }
        }
        index = "mechanic";
        if (object.has(index)){
            SelfOrderMechanicBean mechanicBean = SelfOrderMechanicBean.fromString(object.optString(index));
            if (mechanicBean != null){
                bean.setMechanicBean(mechanicBean);
            }
        }
        return bean;
    }
}

