package com.ceyu.carsteward.ad;

import android.content.Context;

import com.ceyu.carsteward.ad.facade.AdFacade;
import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.packet.router.PacketRouter;

public class AdMod extends ModBase {
    private static AdMod instance;
    private AdMod() {
        super(ModuleNames.Advertivement);
    }

    public static AdMod getInstance(){
        if (instance == null){
            instance = new AdMod();
        }
        return instance;
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        return new AdFacade(context);
    }

    @Override
    public RouterBase getRouter(Context context) {
        return PacketRouter.getInstance(context);
    }
}
