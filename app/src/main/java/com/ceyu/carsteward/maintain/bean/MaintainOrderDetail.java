package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderDetail {
    private String _orderState;
    private int _orderStateCode;
    private CarBrandInfoBean _car;
    private String _carPic;
    private String _carPlate;
    private String _storeName;
    private String _storeAddress;
    private ArrayList<MaintainContent> _item;
    private ArrayList<MaintainContent> _optional;
    private String _free;
    private int _freeCount;
    private String _time;
    private String _name;
    private String _phone;
    private String _money;
    private boolean _doorState;
    private String _doorDate;
    private String _doorAddA;
    private String _doorAddB;
    private String _doorMoney;
    private String _payClass;
    private float _payMoney;
    private String _end_service;
    private ArrayList<MaintainDiscount> _discount;

    public MaintainOrderDetail() {
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

    public String get_carPic() {
        return _carPic;
    }

    public void set_carPic(String _carPic) {
        this._carPic = _carPic;
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

    public ArrayList<MaintainContent> get_item() {
        return _item;
    }

    public void set_item(ArrayList<MaintainContent> _item) {
        this._item = _item;
    }

    public String get_free() {
        return _free;
    }

    public void set_free(String _free) {
        this._free = _free;
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

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public boolean is_doorState() {
        return _doorState;
    }

    public void set_doorState(boolean _doorState) {
        this._doorState = _doorState;
    }

    public String get_doorDate() {
        return _doorDate;
    }

    public void set_doorDate(String _doorDate) {
        this._doorDate = _doorDate;
    }

    public String get_doorAddA() {
        return _doorAddA;
    }

    public void set_doorAddA(String _doorAddA) {
        this._doorAddA = _doorAddA;
    }

    public String get_doorAddB() {
        return _doorAddB;
    }

    public void set_doorAddB(String _doorAddB) {
        this._doorAddB = _doorAddB;
    }

    public String get_doorMoney() {
        return _doorMoney;
    }

    public void set_doorMoney(String _doorMoney) {
        this._doorMoney = _doorMoney;
    }

    public int get_orderStateCode() {
        return _orderStateCode;
    }

    public void set_orderStateCode(int _orderStateCode) {
        this._orderStateCode = _orderStateCode;
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

    public int get_freeCount() {
        return _freeCount;
    }

    public void set_freeCount(int _freeCount) {
        this._freeCount = _freeCount;
    }

    public String get_end_service() {
        return _end_service;
    }

    public void set_end_service(String _end_service) {
        this._end_service = _end_service;
    }

    public ArrayList<MaintainContent> get_optional() {
        return _optional;
    }

    public void set_optional(ArrayList<MaintainContent> _optional) {
        this._optional = _optional;
    }

    public ArrayList<MaintainDiscount> get_discount() {
        return _discount;
    }

    public void set_discount(ArrayList<MaintainDiscount> _discount) {
        this._discount = _discount;
    }

    public static MaintainOrderDetail fromJson(JSONObject object){
        MaintainOrderDetail detail = new MaintainOrderDetail();
        String index = "orderState";
        if (object.has(index)){
            detail.set_orderState(object.optString(index));
        }
        index = "orderStateCode";
        if (object.has(index)){
            detail.set_orderStateCode(object.optInt(index));
        }
        index = "carName";
        if (object.has(index)){
            detail.set_car(CarBrandInfoBean.fromString(object.optString(index)));
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
            detail.set_storeAddress(object.optString(index).replace("*", ""));
        }
        index = "item";
        if (object.has(index)){
            detail.set_item(MaintainContent.fromString(object.optString(index)));
        }
        index = "optional";
        if (object.has(index)){
            detail.set_optional(MaintainContent.fromString(object.optString(index)));
        }
        index = "free";
        if (object.has(index)){
            String free = object.optString(index);
            String[] rulesSub = free.replace("[", "").replace("]", "").replaceAll("\"", "").split(",");
            detail.set_freeCount(rulesSub.length);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rulesSub.length; i++){
                builder.append(rulesSub[i]);
                builder.append("\n");
            }
            String result = builder.toString();
            if (result.length() > 1){
                result = result.substring(0, result.length() - 1);
            }
            detail.set_free(result);
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
            detail.set_money(object.optString(index));
        }
        index = "doorState";
        if (object.has(index)){
            detail.set_doorState(object.optInt(index) == 1);
        }
        index = "doorDate";
        if (object.has(index)){
            detail.set_doorDate(object.optString(index));
        }
        index = "doorAddA";
        if (object.has(index)){
            detail.set_doorAddA(object.optString(index));
        }
        index = "doorAddB";
        if (object.has(index)){
            detail.set_doorAddB(object.optString(index));
        }
        index = "doorMoney";
        if (object.has(index)){
            detail.set_doorMoney(object.optString(index));
        }
        index = "payClass";
        if (object.has(index)){
            detail.set_payClass(object.optString(index));
        }
        index = "payMoney";
        if (object.has(index)){
            detail.set_payMoney((float) object.optDouble(index));
        }
        index = "end_service";
        if (object.has(index)){
            detail.set_end_service(object.optString(index));
        }
        index = "discount";
        if (object.has(index)){
            detail.set_discount(MaintainDiscount.fromString(object.optString(index)));
        }
        return detail;
    }
}
