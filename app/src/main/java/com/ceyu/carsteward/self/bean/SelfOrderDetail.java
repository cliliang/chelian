package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/9/23.
 */
public class SelfOrderDetail {
    private int _orderStateCode;
    private String _orderState;
    private CarBrandInfoBean carInfoBean;
    private String _carPic;
    private String _carPlate;
    private String _storeName;
    private String _storeAddress;
    private SelfMechanicBean _mechanic;
    private List<SelfPartBean> _item;
    private String _time;
    private String _name;
    private String _phone;
    private float _money;
    private String _payClass;
    private float _payMoney;

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

    public CarBrandInfoBean getCarInfoBean() {
        return carInfoBean;
    }

    public void setCarInfoBean(CarBrandInfoBean carInfoBean) {
        this.carInfoBean = carInfoBean;
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

    public SelfMechanicBean get_mechanic() {
        return _mechanic;
    }

    public void set_mechanic(SelfMechanicBean _mechanic) {
        this._mechanic = _mechanic;
    }

    public List<SelfPartBean> get_item() {
        return _item;
    }

    public void set_item(List<SelfPartBean> _item) {
        this._item = _item;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
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

    public String get_payClass() {
        return _payClass;
    }

    public void set_payClass(String _payClass) {
        this._payClass = _payClass;
    }

    public float get_payMoney() {
        return _payMoney;
    }

    public void set_payMoney(float _payMoney) {
        this._payMoney = _payMoney;
    }

    public String get_carPic() {
        return _carPic;
    }

    public void set_carPic(String _carPic) {
        this._carPic = _carPic;
    }

    public static SelfOrderDetail fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        SelfOrderDetail detail = new SelfOrderDetail();
        String index = "orderStateCode";
        if (object.has(index)){
            detail.set_orderStateCode(object.optInt(index));
        }
        index = "orderState";
        if (object.has(index)){
            detail.set_orderState(object.optString(index));
        }
        index = "carName";
        if (object.has(index)){
            String res = object.optString(index);
            CarBrandInfoBean info = CarBrandInfoBean.fromString(res);
            if (info != null){
                detail.setCarInfoBean(info);
            }
        }
        index = "carPic";
        if (object.has(index)){
            detail.set_carPic(object.optString(index));
        }
        index = "carPlate";
        if (object.has(index)){
            detail.set_carPlate(object.optString(index));
        }
        index = "storeName";
        if (object.has(index)){
            detail.set_storeName(object.optString(index));
        }
        index = "storeAddress";
        if (object.has(index)){
            detail.set_storeAddress(object.optString(index));
        }
        index = "mechanic";
        if (object.has(index)){
            String res = object.optString(index);
            SelfMechanicBean mechanicBean = SelfMechanicBean.fromRes(res);
            if (mechanicBean != null){
                detail.set_mechanic(mechanicBean);
            }
        }
        index = "item";
        if (object.has(index)){
            String res = object.optString(index);
            List<SelfPartBean> partBeans = SelfPartBean.fromString(res);
            if (partBeans != null){
                detail.set_item(partBeans);
            }
        }
        index = "time";
        if (object.has(index)){
            detail.set_time(object.optString(index));
        }
        index = "name";
        if (object.has(index)){
            detail.set_name(object.optString(index));
        }
        index = "phone";
        if (object.has(index)){
            detail.set_phone(object.optString(index));
        }
        index = "money";
        if (object.has(index)){
            detail.set_money((float) object.optDouble(index));
        }
        index = "payClass";
        if (object.has(index)){
            detail.set_payClass(object.optString(index));
        }
        index = "payMoney";
        if (object.has(index)){
            detail.set_payMoney((float) object.optDouble(index));
        }
        return detail;
    }
}
