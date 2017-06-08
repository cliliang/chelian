package com.ceyu.carsteward.engineer.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/6/4.
 */
public class EngineerURLs extends BaseURLs{
    public static final String getEngineerList = URL_MAIN_HOST + "app/?act=mechanic,list";
    public static final String getEngineerInfo = URL_MAIN_HOST + "app/?act=mechanic,info";
    public static final String getEngineerPay = URL_MAIN_HOST + "app/?act=mechanic,order";
    public static final String getEngineerPayId = URL_MAIN_HOST + "app/?act=mechanic,orderAdd";
    public static final String getEngineerOfMy = URL_MAIN_HOST + "app/?act=order,mechanicList";
    public static final String callToEngineer = URL_MAIN_HOST + "app/?act=mechanic,call";
    public static final String getOnlineEngineer = URL_MAIN_HOST + "app/?act=mechanic,recommend";
    public static final String getMechanicMenu = URL_MAIN_HOST + "app/?act=mechanic,menu";
    public static final String submitComment = URL_MAIN_HOST + "app/?act=mechanic,commentAdd";
}
