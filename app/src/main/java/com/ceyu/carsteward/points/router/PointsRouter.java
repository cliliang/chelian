package com.ceyu.carsteward.points.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.points.main.PointBuyActivity;
import com.ceyu.carsteward.points.main.PointStaticActivity;

/**
 * Created by Administrator on 2015/6/30.
 */
public class PointsRouter extends RouterBase{

    private static PointsRouter instance;
    private PointsRouter(Context context) {
        super(context);
//        maps.put(RecordUI.recordList, RecordListActivity.class);
//        maps.put(RecordUI.uploadRecord, RecordUploadActivity.class);
        maps.put(PointsUI.firstPage, PointStaticActivity.class);
        maps.put(PointsUI.pointsBuy, PointBuyActivity.class);
    }

    public static PointsRouter getInstance(Context context){
        if (instance == null){
            instance = new PointsRouter(context);
        }
        return instance;
    }
}
