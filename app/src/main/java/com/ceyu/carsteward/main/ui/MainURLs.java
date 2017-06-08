package com.ceyu.carsteward.main.ui;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by Administrator on 2015/6/25.
 */
public class MainURLs extends BaseURLs{

    public static final String sendLocation = URL_MAIN_HOST + "app/?act=user,set";  //上传百度定位坐标
    /*
    首页天气、限行
    http://app3.cheliantime.com/app/?act=common,header&token=3fc4fe4c36153dc2430da3baa8ae7156
     */
    public static final String getWeatherInfo = URL_MAIN_HOST + "app/?act=common,header";
    public static final String sendBug = URL_MAIN_HOST + "app/?act=common,err";
    public static final String modifyPic = URL_MAIN_HOST + "app/?act=user,modifPic";
    public static final String modifyName = URL_MAIN_HOST + "app/?act=user,modif";

}
