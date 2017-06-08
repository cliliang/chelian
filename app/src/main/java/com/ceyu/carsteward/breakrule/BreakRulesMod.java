package com.ceyu.carsteward.breakrule;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.packet.router.PacketRouter;

/**
 * Created by Administrator on 2015/6/29.
 */
public class BreakRulesMod extends ModBase {

    private static BreakRulesMod instance;
    private BreakRulesMod() {
        super(ModuleNames.BreakRuls);
    }

    public static BreakRulesMod getInstance(){
        if (instance == null){
            instance = new BreakRulesMod();
        }
        return instance;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return PacketRouter.getInstance(context);
    }
}
