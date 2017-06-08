package com.ceyu.carsteward.tuan;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.tuan.facade.TuanFacade;
import com.ceyu.carsteward.tuan.router.TuanRouter;

/**
 * Created by chen on 15/7/20.
 */
public class TuanMod extends ModBase {

    private static TuanMod singleton = null;

    private TuanMod(){
        super(ModuleNames.Tuan);
    }

    public static TuanMod getInstance(){
        if (singleton == null){
            singleton = new TuanMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return TuanRouter.getInstance(context);
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        return new TuanFacade(context);
    }
}
