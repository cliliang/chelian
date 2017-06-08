package com.ceyu.carsteward.engineer;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.engineer.facade.EngineerFacade;
import com.ceyu.carsteward.engineer.router.EngineerRouter;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerMod extends ModBase {

    private static EngineerMod instance;
    private EngineerMod(){
        super(ModuleNames.Engineer);
    }

    public static EngineerMod getInstance(){
        if (instance == null){
            instance = new EngineerMod();
        }
        return instance;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return EngineerRouter.getInstance(context);
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        return new EngineerFacade(context);
    }
}
