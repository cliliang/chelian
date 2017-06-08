package com.ceyu.carsteward.car.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

/**
 * Created by chen on 15/6/17.
 */
public class CarBrandInfoBean implements Parcelable{
    private String _brandName;
    private String _subBrandName;
    private String _modelName;
    private String _year;
    private String _capacity;
    private String _auto;

    public CarBrandInfoBean() {
    }

    public String get_brandName() {
        return _brandName;
    }

    public void set_brandName(String _brandName) {
        this._brandName = _brandName;
    }

    public String get_subBrandName() {
        return _subBrandName;
    }

    public void set_subBrandName(String _subBrandName) {
        this._subBrandName = _subBrandName;
    }

    public String get_modelName() {
        return _modelName;
    }

    public void set_modelName(String _modelName) {
        this._modelName = _modelName;
    }

    public String get_year() {
        return _year;
    }

    public void set_year(String _year) {
        this._year = _year;
    }

    public String get_capacity() {
        return _capacity;
    }

    public void set_capacity(String _capacity) {
        this._capacity = _capacity;
    }

    public String get_auto() {
        return _auto;
    }

    public void set_auto(String _auto) {
        this._auto = _auto;
    }

    public static CarBrandInfoBean fromString(String res){
        CarBrandInfoBean bean = new CarBrandInfoBean();
        if (!StringUtils.isEmpty(res)){
            String[] array = res.split(",");
            if (array.length == 6){
                bean.set_brandName(array[0]);
                bean.set_subBrandName(array[1]);
                bean.set_modelName(array[2]);
                bean.set_year(array[3]);
                bean.set_capacity(array[4]);
                bean.set_auto(array[5]);
            }
        }
        return bean;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_brandName);
        dest.writeString(_subBrandName);
        dest.writeString(_modelName);
        dest.writeString(_year);
        dest.writeString(_capacity);
        dest.writeString(_auto);
    }

    public static final Parcelable.Creator<CarBrandInfoBean> CREATOR = new Parcelable.Creator<CarBrandInfoBean>(){

        @Override
        public CarBrandInfoBean createFromParcel(Parcel source) {
            return new CarBrandInfoBean(source);
        }

        @Override
        public CarBrandInfoBean[] newArray(int size) {
            return new CarBrandInfoBean[size];
        }
    };

    private CarBrandInfoBean(Parcel source){
        _brandName = source.readString();
        _subBrandName = source.readString();
        _modelName = source.readString();
        _year = source.readString();
        _capacity = source.readString();
        _auto = source.readString();
    }
}
