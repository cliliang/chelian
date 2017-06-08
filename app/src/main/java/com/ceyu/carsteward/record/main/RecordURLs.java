package com.ceyu.carsteward.record.main;

import com.ceyu.carsteward.common.net.BaseURLs;

/**
 * Created by Administrator on 2015/6/30.
 */
public class RecordURLs extends BaseURLs {

    /*
    http://app3.cheliantime.com/app/?act=record,list&token=9618f5d8e6f51516a077512fd5a96f76&page=1
     */
    public static final String getList = URL_MAIN_HOST + "app/?act=record,list";

    /*
    {
        info: "上传养车记录",
        url: "app?act=record,save&token=UID:4811&info=xx&pic1=",
        err: [
        "A0003",       文件格式错误
        "B0013",       内容为空
        ],
        value: {
            token: "浏览者ID",
            info: "备注信息",
            pic1: "图片",
            ext1: "扩展名",
            pic2: "图片",
            ext2: "扩展名",
            pic3: "图片",
            ext3: "扩展名"
        }
    }
    */
    public static final String uploadImage = URL_MAIN_HOST + "app/?act=record,save";

    /*
    删除养车记录
    app?act=record,del&token=UID:4767&id=1
     */
    public static final String deleteRecord = URL_MAIN_HOST + "app/?act=record,del";

}
