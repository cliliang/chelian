package com.ceyu.carsteward.main;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.main.router.MajorRouter;

/**
 * Created by chen on 15/6/2.
 */
public class MainMod extends ModBase {

    private static MainMod singleton;

    private MainMod(){
        super(ModuleNames.Main);
    }

    public static MainMod getInstance(){
        if (singleton == null){
            singleton = new MainMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return MajorRouter.getInstance(context);
    }
}
