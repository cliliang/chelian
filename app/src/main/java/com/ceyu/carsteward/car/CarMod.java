package com.ceyu.carsteward.car;

import android.content.Context;

import com.ceyu.carsteward.car.facade.CarFacade;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;

/**
 * Created by chen on 15/6/15.
 */
public class CarMod extends ModBase {
    private static CarMod instance;
    private CarMod() {
        super(ModuleNames.Car);
    }

    public static CarMod getInstance(){
        if (instance == null){
            synchronized (CarMod.class){
                if (instance == null){
                    instance = new CarMod();
                }
            }
        }
        return instance;
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        return new CarFacade(context);
    }

    @Override
    public RouterBase getRouter(Context context) {
        return CarRouter.getInstance(context);
    }
}
