package com.ceyu.carsteward.record;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.record.router.RecordRouter;

/**
 * Created by Administrator on 2015/6/30.
 */
public class RecordMod extends ModBase {
    private static RecordMod instance;
    private RecordMod() {
        super(ModuleNames.Record);
    }

    public static RecordMod getInstance(){
        if (instance == null){
            synchronized (RecordMod.class){
                if (instance == null){
                    instance = new RecordMod();
                }
            }
        }
        return instance;
    }


    @Override
    public RouterBase getRouter(Context context) {
        return RecordRouter.getInstance(context);
    }
}
