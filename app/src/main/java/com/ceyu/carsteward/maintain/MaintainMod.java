package com.ceyu.carsteward.maintain;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.maintain.router.MaintainRouter;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainMod extends ModBase {

    private static MaintainMod singleton;
    private MaintainMod() {
        super(ModuleNames.Maintain);
    }

    public static MaintainMod getInstance(){
        if (singleton == null){
            singleton = new MaintainMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return MaintainRouter.getInstance(context);
    }
}
