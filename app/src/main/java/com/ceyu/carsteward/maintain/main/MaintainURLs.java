package com.ceyu.carsteward.maintain.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainURLs extends BaseURLs {
    public static final String getMileages = URL_MAIN_HOST + "app/?act=car,mileage";
    public static final String getMaintainList = URL_MAIN_HOST + "app/?act=store,list";
    public static final String getMaintainInfo = URL_MAIN_HOST + "app/?act=store,detail";
    public static final String getMaintainInfo_v2 = URL_MAIN_HOST + "app/?act=store,detail,2";
    public static final String getMaintainOrder = URL_MAIN_HOST + "app/?act=store,orderAdd";
    public static final String getOrderList = URL_MAIN_HOST + "app/?act=order,list";
    public static final String getOrderDetail = URL_MAIN_HOST + "app/?act=order,storeInfo";
    public static final String modifyOrderDate = URL_MAIN_HOST + "app/?act=order,storeModif";
    public static final String cancelOrder = URL_MAIN_HOST + "app/?act=order,storeCancel";
    public static final String getShopInfo = URL_MAIN_HOST + "app/?act=store,more";
}
