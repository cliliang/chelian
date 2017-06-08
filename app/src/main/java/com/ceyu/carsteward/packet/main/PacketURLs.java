package com.ceyu.carsteward.packet.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by chen on 15/6/8.
 */
public class PacketURLs extends BaseURLs {
    public static final String showPacket = URL_MAIN_HOST + "view/coupons.php?token=%s";
    public static final String myPacket = URL_MAIN_HOST + "app/?act=coupons,list";
    public static final String myPacket_v2 = URL_MAIN_HOST + "app/?act=coupons,list,2";
    public static final String homePacket = URL_MAIN_HOST + "app/?act=coupons,home";
    public static final String getPacket = URL_MAIN_HOST + "app/?act=coupons,get";
    public static final String exchangeCode = URL_MAIN_HOST + "app/?act=coupons,exchange";
}
