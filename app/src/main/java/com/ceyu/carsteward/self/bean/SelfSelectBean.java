package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/11.
 */
public class SelfSelectBean {
    private String _parts;
    private String _partsName;
    private List<SelfCommodityBean> selectBeans;
    private int _select;

    public SelfSelectBean() {
    }

    public String get_parts() {
        return _parts;
    }

    public void set_parts(String _parts) {
        this._parts = _parts;
    }

    public String get_partsName() {
        return _partsName;
    }

    public void set_partsName(String _partsName) {
        this._partsName = _partsName;
    }

    public List<SelfCommodityBean> getSelectBeans() {
        return selectBeans;
    }

    public void setSelectBeans(List<SelfCommodityBean> selectBeans) {
        this.selectBeans = selectBeans;
    }

    public int get_select() {
        return _select;
    }

    public void set_select(int _select) {
        this._select = _select;
    }

    public static List<SelfSelectBean> fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONArray array = new JSONArray(resource);
            List<SelfSelectBean> beans = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                SelfSelectBean bean = new SelfSelectBean();
                String index = "parts";
                if (object.has(index)){
                    bean.set_parts(object.optString(index));
                }
                index = "partsName";
                if (object.has(index)){
                    bean.set_partsName(object.optString(index));
                }
                index = "sub";
                if (object.has(index)){
                    String res = object.optString(index);
                    List<SelfCommodityBean> commodityBeans = SelfCommodityBean.fromString(res);
                    if (commodityBeans != null){
                        bean.setSelectBeans(commodityBeans);
                    }
                }
                index = "select";
                if (object.has(index)){
                    bean.set_select(object.optInt(index));
                }
                beans.add(bean);
            }
            return beans;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
