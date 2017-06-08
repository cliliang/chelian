package com.ceyu.carsteward.self.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/9/16.
 */
public class SelfMapBean {
    private List<SelfShopLocalBean> shopLocal;
    private SelfMyLocalBean myLocal;

    public SelfMapBean() {
    }

    public List<SelfShopLocalBean> getShopLocal() {
        return shopLocal;
    }

    public void setShopLocal(List<SelfShopLocalBean> shopLocal) {
        this.shopLocal = shopLocal;
    }

    public SelfMyLocalBean getMyLocal() {
        return myLocal;
    }

    public void setMyLocal(SelfMyLocalBean myLocal) {
        this.myLocal = myLocal;
    }

    public static SelfMapBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        SelfMapBean bean = new SelfMapBean();
        String index = "list";
        if (object.has(index)){
            String res = object.optString(index);
            List<SelfShopLocalBean> shopBeans = SelfShopLocalBean.fromString(res);
            if (shopBeans != null){
                bean.setShopLocal(shopBeans);
            }
        }
        index = "my";
        if (object.has(index)){
            String res = object.optString(index);
            SelfMyLocalBean myBean = SelfMyLocalBean.fromString(res);
            if (myBean != null){
                bean.setMyLocal(myBean);
            }
        }
        return bean;
    }
}
