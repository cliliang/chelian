package com.ceyu.carsteward.packet;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.packet.facade.PacketFacade;
import com.ceyu.carsteward.packet.router.PacketRouter;

/**
 * Created by chen on 15/6/8.
 */
public class PacketMod extends ModBase {
    private static PacketMod instance;
    private PacketMod() {
        super(ModuleNames.Packet);
    }

    public static PacketMod getInstance(){
        if (instance == null){
            instance = new PacketMod();
        }
        return instance;
    }

    @Override
    public ModFacadeView getFacadeView(Context context) {
        //return new PacketFacade(context);
        return null;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return PacketRouter.getInstance(context);
    }
}
