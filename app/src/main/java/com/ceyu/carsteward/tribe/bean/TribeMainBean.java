package com.ceyu.carsteward.tribe.bean;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/28.
 */
public class TribeMainBean {
    private ArrayList<TribeHotBean> hotBeans;
    private ArrayList<TribeHomeBean> homeBeans;
    private int _tome;
    private boolean _more;

    public TribeMainBean() {
    }

    public ArrayList<TribeHotBean> getHotBeans() {
        return hotBeans;
    }

    public void setHotBeans(ArrayList<TribeHotBean> hotBeans) {
        this.hotBeans = hotBeans;
    }

    public ArrayList<TribeHomeBean> getHomeBeans() {
        return homeBeans;
    }

    public void setHomeBeans(ArrayList<TribeHomeBean> homeBeans) {
        this.homeBeans = homeBeans;
    }

    public int get_tome() {
        return _tome;
    }

    public void set_tome(int _tome) {
        this._tome = _tome;
    }

    public boolean is_more() {
        return _more;
    }

    public void set_more(boolean _more) {
        this._more = _more;
    }

    public static TribeMainBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        TribeMainBean bean = new TribeMainBean();
        String index = "list1";
        if (object.has(index)){
            String l1 = object.optString(index);
            ArrayList<TribeHotBean> beans = TribeHotBean.fromString(l1);
            if (beans != null){
                bean.setHotBeans(beans);
            }
        }
        index = "list2";
        if (object.has(index)){
            String l2 = object.optString(index);
            ArrayList<TribeHomeBean> beans = TribeHomeBean.fromString(l2);
            if (beans != null){
                bean.setHomeBeans(beans);
            }
        }
        index = "tome";
        if (object.has(index)){
            bean.set_tome(object.optInt(index));
        }
        index = "more";
        if (object.has(index)){
            bean.set_more(object.optInt(index) == 1);
        }
        return bean;
    }
}
