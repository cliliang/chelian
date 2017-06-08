package com.ceyu.carsteward.user.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/6/2.
 */
public class UserURLs extends BaseURLs {
    public static final String sendPhoneCode = URL_MAIN_HOST + "app/?act=user,userCode";
    public static final String userLogin = URL_MAIN_HOST + "app/?act=user,login";
    public static final String suggest = URL_MAIN_HOST + "app/?act=common,feedback";
}
