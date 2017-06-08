package com.ceyu.carsteward.tuan.bean;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONObject;

/**
 * Created by chen on 15/7/22.
 */
public class TuanOrderBean {
    private int _orderStateCode;
    private String _orderState;
    private CarBrandInfoBean _car;
    private String _carPlate;
    private String _storeName;
    private String _storeAddress;
    private String _item;
    private String _storeTime;
    private String _name;
    private String _phone;
    private float _money;
    private String _end_service;
    private String _txt;

    public TuanOrderBean() {
    }

    public int get_orderStateCode() {
        return _orderStateCode;
    }

    public void set_orderStateCode(int _orderStateCode) {
        this._orderStateCode = _orderStateCode;
    }

    public String get_orderState() {
        return _orderState;
    }

    public void set_orderState(String _orderState) {
        this._orderState = _orderState;
    }

    public CarBrandInfoBean get_car() {
        return _car;
    }

    public void set_car(CarBrandInfoBean _car) {
        this._car = _car;
    }

    public String get_carPlate() {
        return _carPlate;
    }

    public void set_carPlate(String _carPlate) {
        this._carPlate = _carPlate;
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

    public String get_item() {
        return _item;
    }

    public void set_item(String _item) {
        this._item = _item;
    }

    public String get_storeTime() {
        return _storeTime;
    }

    public void set_storeTime(String _storeTime) {
        this._storeTime = _storeTime;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_phone() {
        return _phone;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public float get_money() {
        return _money;
    }

    public void set_money(float _money) {
        this._money = _money;
    }

    public String get_end_service() {
        return _end_service;
    }

    public void set_end_service(String _end_service) {
        this._end_service = _end_service;
    }

    public String get_txt() {
        return _txt;
    }

    public void set_txt(String _txt) {
        this._txt = _txt;
    }

    public static TuanOrderBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        TuanOrderBean bean = new TuanOrderBean();
        String index = "orderStateCode";
        if (object.has(index)){
            bean.set_orderStateCode(object.optInt(index));
        }
        index = "orderState";
        if (object.has(index)){
            bean.set_orderState(object.optString(index));
        }
        index = "carName";
        if (object.has(index)){
            bean.set_car(CarBrandInfoBean.fromString(object.optString(index)));
        }
        index = "carPlate";
        if (object.has(index)){
            bean.set_carPlate(object.optString(index));
        }
        index = "storeName";
        if (object.has(index)){
            bean.set_storeName(object.optString(index));
        }
        index = "storeAddress";
        if (object.has(index)){
            bean.set_storeAddress(object.optString(index));
        }
        index = "item";
        if (object.has(index)){
            bean.set_item(StringUtils.formatRes(object.optString(index)));
        }
        index = "time";
        if (object.has(index)){
            bean.set_storeTime(object.optString(index));
        }
        index = "name";
        if (object.has(index)){
            bean.set_name(object.optString(index));
        }
        index = "phone";
        if (object.has(index)){
            bean.set_phone(object.optString(index));
        }
        index = "money";
        if (object.has(index)){
            bean.set_money((float)object.optDouble(index));
        }
        index = "end_service";
        if (object.has(index)){
            bean.set_end_service(object.optString(index));
        }
        index = "txt";
        if (object.has(index)){
            bean.set_txt(object.optString(index));
        }
        return bean;
    }
}
