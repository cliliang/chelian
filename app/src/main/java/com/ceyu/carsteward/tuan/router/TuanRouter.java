package com.ceyu.carsteward.tuan.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.tuan.main.TuanContentActivity;
import com.ceyu.carsteward.tuan.main.TuanMainActivity;
import com.ceyu.carsteward.tuan.main.TuanOrderActivity;
import com.ceyu.carsteward.tuan.main.TuanReserveActivity;

/**
 * Created by chen on 15/7/20.
 */
public class TuanRouter extends RouterBase {

    private static TuanRouter instance = null;

    private TuanRouter(Context context){
        super(context);
        maps.put(TuanUI.tuanMain, TuanMainActivity.class);
        maps.put(TuanUI.tuanContent, TuanContentActivity.class);
        maps.put(TuanUI.tuanReserve, TuanReserveActivity.class);
        maps.put(TuanUI.tuanOrderDetail, TuanOrderActivity.class);
    }

    public static TuanRouter getInstance(Context context){
        if (instance == null){
            synchronized (TuanRouter.class){
                if (instance == null){
                    instance = new TuanRouter(context);
                }
            }
        }
        return instance;
    }

}
