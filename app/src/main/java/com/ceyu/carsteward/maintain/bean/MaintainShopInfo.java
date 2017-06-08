package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainShopInfo implements Parcelable{
    private int _sid;
    private String _name;
    private String _pic;
    private String _address;
    private MaintainRule _rule;
    private String _free;
    private int _freeCount;
    private String _onlinePay;
    private boolean _onlinePayState;
    private MaintainParts _parts;
    private MaintainPayBean _money;
    private MaintainParts _optional;
    private String _notice;
    private String _distance;
    private MaintainCarInfo _carInfo;
    private String _door;
    private boolean hasDetail;

    public MaintainShopInfo() {
    }

    public int get_sid() {
        return _sid;
    }

    public void set_sid(int _sid) {
        this._sid = _sid;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public MaintainRule get_rule() {
        return _rule;
    }

    public void set_rule(MaintainRule _rule) {
        this._rule = _rule;
    }

    public String get_free() {
        return _free;
    }

    public void set_free(String _free) {
        this._free = _free;
    }

    public String get_onlinePay() {
        return _onlinePay;
    }

    public void set_onlinePay(String _onlinePay) {
        this._onlinePay = _onlinePay;
    }

    public MaintainParts get_parts() {
        return _parts;
    }

    public void set_parts(MaintainParts _parts) {
        this._parts = _parts;
    }

    public MaintainParts get_optional() {
        return _optional;
    }

    public void set_optional(MaintainParts _optional) {
        this._optional = _optional;
    }

    public String get_notice() {
        return _notice;
    }

    public void set_notice(String _notice) {
        this._notice = _notice;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String _distance) {
        this._distance = _distance;
    }

    public MaintainCarInfo get_carInfo() {
        return _carInfo;
    }

    public void set_carInfo(MaintainCarInfo _carInfo) {
        this._carInfo = _carInfo;
    }

    public MaintainPayBean get_money() {
        return _money;
    }

    public void set_money(MaintainPayBean _money) {
        this._money = _money;
    }

    public String get_door() {
        return _door;
    }

    public void set_door(String _door) {
        this._door = _door;
    }

    public int get_freeCount() {
        return _freeCount;
    }

    public void set_freeCount(int _freeCount) {
        this._freeCount = _freeCount;
    }

    public boolean is_onlinePayState() {
        return _onlinePayState;
    }

    public void set_onlinePayState(boolean _onlinePayState) {
        this._onlinePayState = _onlinePayState;
    }

    public boolean isHasDetail() {
        return hasDetail;
    }

    public void setHasDetail(boolean hasDetail) {
        this.hasDetail = hasDetail;
    }

    public static MaintainShopInfo fromJsonObject(JSONObject object){
        MaintainShopInfo shopInfo = new MaintainShopInfo();
        String index = "sid";
        if (object.has(index)){
            shopInfo.set_sid(object.optInt(index));
        }
        index = "name";
        if (object.has(index)){
            shopInfo.set_name(object.optString(index));
        }
        index = "pic";
        if (object.has(index)){
            shopInfo.set_pic(object.optString(index));
        }
        index = "address";
        if (object.has(index)){
            shopInfo.set_address(object.optString(index));
        }
        index = "rule";
        if (object.has(index)){
            shopInfo.set_rule(MaintainRule.fromString(object.optString(index)));
        }
        index = "free";
        if (object.has(index)){
            String free = object.optString(index);
            String[] rulesSub = free.replace("[", "").replace("]", "").replaceAll("\"", "").split(",");
            shopInfo.set_freeCount(rulesSub.length);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rulesSub.length; i++){
                builder.append(rulesSub[i]);
                builder.append("\n");
            }
            String result = builder.toString();
            if (result.length() > 1){
                result = result.substring(0, result.length() - 1);
            }
            shopInfo.set_free(result);
        }
        index = "onlinePay";
        if (object.has(index)){
            shopInfo.set_onlinePay(object.optString(index));
        }
        index = "onlineState";
        if (object.has(index)){
            shopInfo.set_onlinePayState(object.optInt(index) == 1);
        }
        index = "parts";
        if (object.has(index)){
            shopInfo.set_parts(MaintainParts.fromString(object.optString(index)));
        }
        index = "money";
        if (object.has(index)){
            shopInfo.set_money(MaintainPayBean.fromString(object.optString(index)));
        }
        index = "optional";
        if (object.has(index)){
            shopInfo.set_optional(MaintainParts.fromString(object.optString(index)));
        }
        index = "notice";
        if (object.has(index)){
            shopInfo.set_notice(object.optString(index));
        }
        index = "distance";
        if (object.has(index)){
            shopInfo.set_distance(object.optString(index));
        }
        index = "car";
        if (object.has(index)){
            shopInfo.set_carInfo(MaintainCarInfo.fromString(object.optString(index)));
        }
        index = "door";
        if (object.has(index)){
            shopInfo.set_door(object.optString(index));
        }
        index = "detail";
        if (object.has(index)){
            shopInfo.setHasDetail(object.optInt(index) == 1);
        }
        return shopInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MaintainShopInfo> CREATOR = new Parcelable.Creator<MaintainShopInfo>(){
        @Override
        public MaintainShopInfo createFromParcel(Parcel source) {
            return new MaintainShopInfo(source);
        }

        @Override
        public MaintainShopInfo[] newArray(int size) {
            return new MaintainShopInfo[size];
        }
    };

    private MaintainShopInfo(Parcel source){
        _sid = source.readInt();
        _name = source.readString();
        _pic = source.readString();
        _address = source.readString();
        _rule = source.readParcelable(MaintainRule.class.getClassLoader());
        _free = source.readString();
        _freeCount = source.readInt();
        _onlinePay = source.readParcelable(MaintainOnlinePay.class.getClassLoader());
        _onlinePayState = source.readInt() == 1;
        _parts = source.readParcelable(MaintainParts.class.getClassLoader());
        _optional = source.readParcelable(MaintainParts.class.getClassLoader());
        _money = source.readParcelable(MaintainPayBean.class.getClassLoader());
        _notice = source.readString();
        _distance = source.readString();
        _carInfo = source.readParcelable(MaintainCarInfo.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_sid);
        dest.writeString(_name);
        dest.writeString(_pic);
        dest.writeString(_address);
        dest.writeParcelable(_rule, flags);
        dest.writeString(_free);
        dest.writeInt(_freeCount);
        dest.writeString(_onlinePay);
        dest.writeInt(_onlinePayState ? 1 : 0);
        dest.writeParcelable(_parts, flags);
        dest.writeParcelable(_optional, flags);
        dest.writeParcelable(_money, flags);
        dest.writeString(_notice);
        dest.writeString(_distance);
        dest.writeParcelable(_carInfo, flags);
    }
}
