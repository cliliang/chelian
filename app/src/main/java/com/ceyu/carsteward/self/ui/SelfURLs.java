package com.ceyu.carsteward.self.ui;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/9/11.
 */
public class SelfURLs extends BaseURLs{
    public static final String getMileageList = URL_MAIN_HOST + "app/?act=self,mileage";
    public static final String getSelfMaintain = URL_MAIN_HOST + "app/?act=self,parts";
    public static final String getAllShops = URL_MAIN_HOST + "app/?act=self,store";
    public static final String getMechanic = URL_MAIN_HOST + "app/?act=self,mechanic";
    public static final String getOrderContent = URL_MAIN_HOST + "app/?act=self,order";
    public static final String payShopOrder = URL_MAIN_HOST + "app/?act=self,orderAdd";
    public static final String getOrderInfo = URL_MAIN_HOST + "app/?act=order,selfInfo";
    public static final String uploadComment = URL_MAIN_HOST + "app/?act=mechanic,commentAdd";
}
