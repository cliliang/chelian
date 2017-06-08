package com.ceyu.carsteward.self.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/9/17.
 */
public class SelfMechanicList {
    private List<SelfMechanicBean> mechanicBeans;
    private boolean more;

    public List<SelfMechanicBean> getMechanicBeans() {
        return mechanicBeans;
    }

    public void setMechanicBeans(List<SelfMechanicBean> mechanicBeans) {
        this.mechanicBeans = mechanicBeans;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public static SelfMechanicList fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        SelfMechanicList mechanicList = new SelfMechanicList();
        String index = "list";
        if (object.has(index)){
            String res = object.optString(index);
            List<SelfMechanicBean> beans = SelfMechanicBean.fromString(res);
            if (beans != null){
                mechanicList.setMechanicBeans(beans);
            }
         }
        index = "more";
        if (object.has(index)){
            mechanicList.setMore(object.optInt(index) == 1);
        }
        return mechanicList;
    }
}
