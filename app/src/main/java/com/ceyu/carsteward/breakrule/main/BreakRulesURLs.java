package com.ceyu.carsteward.breakrule.main;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by Administrator on 2015/6/30.
 */
public class BreakRulesURLs extends BaseURLs {

    /*
    http://app3.cheliantime.com/app/?act=act=car,list
    用户车辆列表
     */
    public static final String myCarList = URL_MAIN_HOST + "app/?act=car,list";

    /*
    http://app3.cheliantime.com/app/?act=common,illegal&token=UID:4767&cid=73
    token = 用户ID
    cid = 车辆id
     */
    public static final String breakrulesList = URL_MAIN_HOST + "app/?act=common,illegal";

    //从车列表向违章详情页传递车辆ID用的tag；
    public static final String TAG_CID = "cid";

    //错误信息
    public static final int B0014 = R.string.breakrules_please_add_carinfo;
    public static final int B0015 = R.string.breakrules_please_openloc;
    /*
    去违章列表的forResult
    如果没有定位信息无法查询到违章信息
    需要提示用户打开定位
     */
    public static final int FORRSUTL_BREAKRULES = 0x10;
    //从打开定位界面返回
    public static final int FORRESULT_LOCSETTING = 0X11;
    //什么多不做
    public static final int RESULT_NOTHINGH = 0x20;
    //编辑车辆信息
    public static final int RESULT_TOEDITCAR = 0x21;
    //打开定位设置
    public static final int RESULT_TOSETTINGLOC = 0x22;

}
