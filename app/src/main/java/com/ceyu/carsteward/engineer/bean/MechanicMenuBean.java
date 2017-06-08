package com.ceyu.carsteward.engineer.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/18.
 */
public class MechanicMenuBean {
    private ArrayList<BrandBean> brandBeans;
    private ArrayList<LevelBean> levelBeans;
    private String code;

    public MechanicMenuBean() {
    }

    public ArrayList<BrandBean> getBrandBeans() {
        return brandBeans;
    }

    public void setBrandBeans(ArrayList<BrandBean> brandBeans) {
        this.brandBeans = brandBeans;
    }

    public ArrayList<LevelBean> getLevelBeans() {
        return levelBeans;
    }

    public void setLevelBeans(ArrayList<LevelBean> levelBeans) {
        this.levelBeans = levelBeans;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static MechanicMenuBean fromString(String resource){
        MechanicMenuBean bean = new MechanicMenuBean();
        try {
            JSONObject object = new JSONObject(resource);
            bean = fromJson(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static MechanicMenuBean fromJson(JSONObject object){
        MechanicMenuBean bean = new MechanicMenuBean();
        if (object != null){
            String index = "brand";
            if (object.has(index)){
                String brandString = object.optString(index);
                bean.setBrandBeans(BrandBean.fromJSONArrayString(brandString));
            }
            index = "function";
            if (object.has(index)){
                String levelString = object.optString(index);
                bean.setLevelBeans(LevelBean.formJSONArrayString(levelString));
            }
            index = "code";
            if (object.has(index)){
                bean.setCode(object.optString(index));
            }
        }
        return bean;
    }
}
