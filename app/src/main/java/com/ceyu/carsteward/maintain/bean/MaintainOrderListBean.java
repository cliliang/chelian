package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderListBean {
    private String _class;
    private String _orderState;
    private String _sn;
    private String _name;
    private CarBrandInfoBean _car;
    private String _carPlate;
    private String _carPic;
    private String _time;
    private String _money;
    private String _factory;
    private String _storeName;
    private String _storeAddress;
    private String _storeClass;
    private String _storePic;
    private String _storeDistance;
    private String _title;

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_orderState() {
        return _orderState;
    }

    public void set_orderState(String _orderState) {
        this._orderState = _orderState;
    }

    public String get_sn() {
        return _sn;
    }

    public void set_sn(String _sn) {
        this._sn = _sn;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public CarBrandInfoBean get_car() {
        return _car;
    }

    public void set_car(CarBrandInfoBean _car) {
        this._car = _car;
    }

    public String get_carPic() {
        return _carPic;
    }

    public void set_carPic(String _carPic) {
        this._carPic = _carPic;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public String get_factory() {
        return _factory;
    }

    public void set_factory(String _factory) {
        this._factory = _factory;
    }

    public String get_storeName() {
        return _storeName;
    }

    public void set_storeName(String _storeName) {
        this._storeName = _storeName;
    }

    public String get_storeAddress() {
        return _storeAddress;
    }

    public void set_storeAddress(String _storeAddress) {
        this._storeAddress = _storeAddress;
    }

    public String get_storeClass() {
        return _storeClass;
    }

    public void set_storeClass(String _storeClass) {
        this._storeClass = _storeClass;
    }

    public String get_storePic() {
        return _storePic;
    }

    public void set_storePic(String _storePic) {
        this._storePic = _storePic;
    }

    public String get_storeDistance() {
        return _storeDistance;
    }

    public String get_carPlate() {
        return _carPlate;
    }

    public void set_carPlate(String _carPlate) {
        this._carPlate = _carPlate;
    }

    public void set_storeDistance(String _storeDistance) {
        this._storeDistance = _storeDistance;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public static ArrayList<MaintainOrderListBean> fromString(String resource){
        ArrayList<MaintainOrderListBean> listBeans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                MaintainOrderListBean listBean = new MaintainOrderListBean();
                JSONObject object = array.getJSONObject(i);
                String index = "class";
                if (object.has(index)){
                    listBean.set_class(object.optString(index));
                }
                index = "orderState";
                if (object.has(index)){
                    listBean.set_orderState(object.optString(index));
                }
                index = "sn";
                if (object.has(index)){
                    listBean.set_sn(object.optString(index));
                }
                index = "storeName";
                if (object.has(index)){
                    listBean.set_name(object.optString(index));
                }
                index = "carName";
                if (object.has(index)){
                    listBean.set_car(CarBrandInfoBean.fromString(object.optString(index)));
                }
                index = "carPic";
                if (object.has(index)){
                    listBean.set_carPic(object.optString(index));
                }
                index = "time";
                if (object.has(index)){
                    listBean.set_time(object.optString(index));
                }
                index = "money";
                if (object.has(index)){
                    listBean.set_money(object.optString(index));
                }
                index = "factory";
                if (object.has(index)){
                    listBean.set_factory(object.optString(index));
                }
                index = "storeDistance";
                if (object.has(index)){
                    listBean.set_storeDistance(object.optString(index));
                }
                index = "storeAddress";
                if (object.has(index)){
                    listBean.set_storeAddress(object.optString(index).replace("*", "\n"));
                }
                index = "storeClass";
                if (object.has(index)){
                    listBean.set_storeClass(object.optString(index));
                }
                index = "storePic";
                if (object.has(index)){
                    listBean.set_storePic(object.optString(index));
                }
                index = "storeDistance";
                if (object.has(index)){
                    listBean.set_storeDistance(object.optString(index));
                }
                index = "carPlate";
                if (object.has(index)){
                    listBean.set_carPlate(object.optString(index));
                }
                index = "title";
                if (object.has(index)){
                    listBean.set_title(object.optString(index));
                }
                listBeans.add(listBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listBeans;
    }
}
