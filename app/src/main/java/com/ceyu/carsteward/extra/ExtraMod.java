package com.ceyu.carsteward.extra;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.extra.router.ExtraRouter;

/**
 * Created by chen on 15/6/1.
 */
public class ExtraMod extends ModBase {
    private static ExtraMod singleInstance;
    public ExtraMod() {
        super(ModuleNames.Extra);
    }

    public static ExtraMod getInstance(){
        if (singleInstance == null){
            singleInstance = new ExtraMod();
        }
        return singleInstance;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return ExtraRouter.getInstance(context);
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        return super.getFacadeView(context);
    }
}
