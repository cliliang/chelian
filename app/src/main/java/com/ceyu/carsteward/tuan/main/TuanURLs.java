package com.ceyu.carsteward.tuan.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/7/20.
 */
public class TuanURLs extends BaseURLs{
    public static final String getHomeTuan = URL_MAIN_HOST + "app/?act=group,home";
    public static final String getMainTuan = URL_MAIN_HOST + "app/?act=group,list";
    public static final String getTuanDetail = URL_MAIN_HOST + "app/?act=group,info";
    public static final String addTuanOrder = URL_MAIN_HOST + "app/?act=group,orderAdd";
    public static final String getOrderDetail = URL_MAIN_HOST + "app/?act=order,groupInfo";
    public static final String modifyTime = URL_MAIN_HOST + "app/?act=order,groupModif";


}
