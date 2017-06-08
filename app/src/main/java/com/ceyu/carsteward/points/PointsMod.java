package com.ceyu.carsteward.points;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.points.router.PointsRouter;
import com.ceyu.carsteward.record.router.RecordRouter;

/**
 * Created by Administrator on 2015/6/30.
 */
public class PointsMod extends ModBase {
    private static PointsMod instance;
    private PointsMod() {
        super(ModuleNames.Points);
    }

    public static PointsMod getInstance(){
        if (instance == null){
            synchronized (PointsMod.class){
                if (instance == null){
                    instance = new PointsMod();
                }
            }
        }
        return instance;
    }


    @Override
    public RouterBase getRouter(Context context) {
        return PointsRouter.getInstance(context);
    }
}
