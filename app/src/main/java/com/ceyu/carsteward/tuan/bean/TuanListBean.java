package com.ceyu.carsteward.tuan.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/7/20.
 */
public class TuanListBean {

    private String _gid;
    private String _title;
    private TuanModBean _store;
    private String _money;
    private String _market;
    private int _num;
    private String _distance;
    private String _type;

    public TuanListBean() {
    }

    public String get_gid() {
        return _gid;
    }

    public void set_gid(String _gid) {
        this._gid = _gid;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public TuanModBean get_store() {
        return _store;
    }

    public void set_store(TuanModBean _store) {
        this._store = _store;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public String get_market() {
        return _market;
    }

    public void set_market(String _market) {
        this._market = _market;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public static List<TuanListBean> fromJsonArray(JSONArray array){
        if (array != null){
            List<TuanListBean> listBeans = new ArrayList<>();
            for (int i = 0; i < array.length(); i++){
                TuanListBean bean;
                try {
                    JSONObject object = array.getJSONObject(i);
                    bean = TuanListBean.fromJson(object);
                    if (bean != null){
                        listBeans.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return listBeans;
        }
        return null;
    }

    public static List<TuanListBean> fromString(String resource){
        List<TuanListBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            beans = fromJsonArray(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beans;
    }

    private static TuanListBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        TuanListBean bean = new TuanListBean();
        String index = "gid";
        if (object.has(index)){
            bean.set_gid(object.optString(index));
        }
        index = "title";
        if (object.has(index)){
            bean.set_title(object.optString(index));
        }
        index = "store";
        if (object.has(index)){
            String res = object.optString(index);
            TuanModBean modBean = TuanModBean.fromString(res);
            if (modBean != null){
                bean.set_store(modBean);
            }
        }
        index = "money";
        if (object.has(index)){
            bean.set_money(object.optString(index));
        }
        index = "market";
        if (object.has(index)){
            String marketString = object.optString(index);
            if (StringUtils.isEmpty(marketString) || marketString.equals("null")){
                bean.set_market("");
            }else {
                float marketFloat;
                try {
                    marketFloat = Float.valueOf(marketString);
                    if (Float.compare(marketFloat, 0) == 0){
                        bean.set_market("");
                    }else {
                        bean.set_market("￥" + marketString);
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
        index = "num";
        if (object.has(index)) {
            bean.set_num(object.optInt(index));
        }
        index = "distance";
        if (object.has(index)){
            bean.set_distance(object.optString(index));
        }

        return bean;
    }
}

