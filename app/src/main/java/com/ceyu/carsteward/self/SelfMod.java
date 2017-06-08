package com.ceyu.carsteward.self;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.self.router.SelfRouter;

/**
 * Created by chen on 15/9/9.
 */
public class SelfMod extends ModBase {

    private static SelfMod instance;
    private SelfMod(){
        super(ModuleNames.Self);
    }
    public static SelfMod getInstance(){
        if (instance == null){
            instance = new SelfMod();
        }
        return instance;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return SelfRouter.getInstance(context);
    }
}
