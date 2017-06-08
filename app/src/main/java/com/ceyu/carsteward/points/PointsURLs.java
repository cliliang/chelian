package com.ceyu.carsteward.points;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/10/13.
 */
public class PointsURLs extends BaseURLs{

    public static final String getPointState = URL_MAIN_HOST + "app/?act=integral,showSign";
    public static final String changePointState = URL_MAIN_HOST + "app/?act=integral,doSign";
    public static final String getPoints = URL_MAIN_HOST + "app/?act=integral,shop";
    public static final String submitInfo = URL_MAIN_HOST + "app/?act=integral,orderAdd";

}
