package com.ceyu.carsteward.tribe;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.tribe.router.TribeRouter;

/**
 * Created by chen on 15/8/26.
 */
public class TribeMod extends ModBase {
    public static TribeMod singleton;
    private TribeMod() {
        super(ModuleNames.Tribe);
    }
    public static TribeMod getInstance(){
        if (singleton == null){
            singleton = new TribeMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return TribeRouter.getInstance(context);
    }
}
