package com.ceyu.carsteward.car.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/17.
 */
public class CarInfoBean implements Parcelable{

    private int _id;
    private int _modelId;
    private int _brandId;
    private CarBrandInfoBean brandInfoBean;
    private String _modelPic;
    private int _insurId;
    private String _insurName;
    private String _insurDate;
    private String _buyDate;
    private String _plate;
    private String _frame;
    private String _motor;
    private String _name;
    private String _mileage;

    public CarInfoBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_modelId() {
        return _modelId;
    }

    public void set_modelId(int _modelId) {
        this._modelId = _modelId;
    }

    public CarBrandInfoBean getBrandInfoBean() {
        return brandInfoBean;
    }

    public void setBrandInfoBean(CarBrandInfoBean brandInfoBean) {
        this.brandInfoBean = brandInfoBean;
    }

    public String get_modelPic() {
        return _modelPic;
    }

    public void set_modelPic(String _modelPic) {
        this._modelPic = _modelPic;
    }

    public int get_insurId() {
        return _insurId;
    }

    public void set_insurId(int _insurId) {
        this._insurId = _insurId;
    }

    public String get_insurName() {
        return _insurName;
    }

    public void set_insurName(String _insurName) {
        this._insurName = _insurName;
    }

    public String get_insurDate() {
        return _insurDate;
    }

    public void set_insurDate(String _insurDate) {
        this._insurDate = _insurDate;
    }

    public String get_buyDate() {
        return _buyDate;
    }

    public void set_buyDate(String _buyDate) {
        this._buyDate = _buyDate;
    }

    public String get_plate() {
        return _plate;
    }

    public void set_plate(String _plate) {
        this._plate = _plate;
    }

    public String get_frame() {
        return _frame;
    }

    public void set_frame(String _frame) {
        this._frame = _frame;
    }

    public String get_motor() {
        return _motor;
    }

    public void set_motor(String _motor) {
        this._motor = _motor;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_mileage() {
        return _mileage;
    }

    public void set_mileage(String _mileage) {
        this._mileage = _mileage;
    }

    public int get_brandId() {
        return _brandId;
    }

    public void set_brandId(int _brandId) {
        this._brandId = _brandId;
    }

    public static ArrayList<CarInfoBean> fromJsonArray(JSONArray array){
        ArrayList<CarInfoBean> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                CarInfoBean infoBean = new CarInfoBean();
                String index = "id";
                if (object.has(index)){
                    infoBean.set_id(object.optInt(index));
                }
                index = "modelId";
                if (object.has(index)){
                    infoBean.set_modelId(object.optInt(index));
                }
                index = "modelName";
                if (object.has(index)){
                    String brandInfoString = object.optString(index);
                    CarBrandInfoBean bean = CarBrandInfoBean.fromString(brandInfoString);
                    infoBean.setBrandInfoBean(bean);
                }
                index = "insurId";
                if (object.has(index)){
                    infoBean.set_insurId(object.optInt(index));
                }
                index = "insurName";
                if (object.has(index)){
                    infoBean.set_insurName(object.optString(index));
                }
                index = "modelPic";
                if (object.has(index)){
                    infoBean.set_modelPic(object.optString(index));
                }
                index = "insurDate";
                if (object.has(index)){
                    infoBean.set_insurDate(object.optString(index));
                }
                index = "buyDate";
                if (object.has(index)){
                    infoBean.set_buyDate(object.optString(index));
                }
                index = "plate";
                if (object.has(index)){
                    infoBean.set_plate(object.optString(index));
                }
                index = "frame";
                if (object.has(index)){
                    infoBean.set_frame(object.optString(index));
                }
                index = "motor";
                if (object.has(index)){
                    infoBean.set_motor(object.optString(index));
                }
                index = "name";
                if (object.has(index)){
                    infoBean.set_name(object.optString(index));
                }
                index = "mileage";
                if (object.has(index)){
                    infoBean.set_mileage(object.optString(index));
                }
                index = "brandId";
                if (object.has(index)){
                    infoBean.set_brandId(object.optInt(index));
                }
                list.add(infoBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_modelId);
        dest.writeInt(_brandId);
        dest.writeParcelable(brandInfoBean, flags);
        dest.writeString(_modelPic);
        dest.writeInt(_insurId);
        dest.writeString(_insurName);
        dest.writeString(_insurDate);
        dest.writeString(_buyDate);
        dest.writeString(_plate);
        dest.writeString(_frame);
        dest.writeString(_motor);
        dest.writeString(_name);
        dest.writeString(_mileage);
    }

    public static final Parcelable.Creator<CarInfoBean> CREATOR = new Parcelable.Creator<CarInfoBean>(){

        @Override
        public CarInfoBean createFromParcel(Parcel source) {
            return new CarInfoBean(source);
        }

        @Override
        public CarInfoBean[] newArray(int size) {
            return new CarInfoBean[size];
        }
    };

    private CarInfoBean(Parcel source){
        _id = source.readInt();
        _modelId = source.readInt();
        _brandId = source.readInt();
        brandInfoBean = source.readParcelable(CarBrandInfoBean.class.getClassLoader());
        _modelPic = source.readString();
        _insurId = source.readInt();
        _insurName = source.readString();
        _insurDate = source.readString();
        _buyDate = source.readString();
        _plate = source.readString();
        _frame = source.readString();
        _motor = source.readString();
        _name = source.readString();
        _mileage = source.readString();
    }
}

