package com.ceyu.carsteward.car.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/6/15.
 */
public class CarURLs extends BaseURLs {
    public static final String getCarBrand = URL_MAIN_HOST + "app/?act=car,brand";
    public static final String getCarModel = URL_MAIN_HOST + "app/?act=car,sub";
    public static final String getCarYear = URL_MAIN_HOST + "app/?act=car,year";
    public static final String getCarDetail = URL_MAIN_HOST + "app/?act=car,detail";
    public static final String getInsurance = URL_MAIN_HOST + "app/?act=common,insurance";
    public static final String saveCarInfo = URL_MAIN_HOST + "app/?act=car,add";
    public static final String getCarList = URL_MAIN_HOST + "app/?act=car,list";
    public static final String deleteCar = URL_MAIN_HOST + "app/?act=car,del";
    public static final String modifyCar = URL_MAIN_HOST + "app/?act=car,update";
}
