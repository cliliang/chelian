package com.ceyu.carsteward.points.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/10/14.
 */
public class PointsMainBean {
    private int integral;
    private List<PointGoodsBean> list;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public List<PointGoodsBean> getList() {
        return list;
    }

    public void setList(List<PointGoodsBean> list) {
        this.list = list;
    }

    public static PointsMainBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        PointsMainBean bean = new PointsMainBean();
        String index = "integral";
        if (object.has(index)){
            bean.setIntegral(object.optInt(index));
        }
        index = "list";
        if (object.has(index)){
            String res = object.optString(index);
            List<PointGoodsBean> beans = PointGoodsBean.fromString(res);
            if (beans != null){
                bean.setList(beans);
            }
        }
        return bean;

    }
}
