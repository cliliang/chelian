package com.ceyu.carsteward.packet.facade;

import android.content.Context;
import android.util.AttributeSet;

import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.packet.views.PacketFacadeView;

/**
 * Created by chen on 15/6/8.
 */
public class PacketFacade  extends ModFacadeView{


    public PacketFacade(Context context) {
        super(context);
        init(context);
    }

    public PacketFacade(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context){
        PacketFacadeView facadeView = new PacketFacadeView(context);
        facadeView.setOnBreadRulesClickedListener(new PacketFacadeView.OnBreakRulesOnClick() {
            @Override
            public void onBreakRulesClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.BreakRuls, PacketUI.breakRulesRecord);
            }
        });
        facadeView.setOnRedPacketClickedListener(new PacketFacadeView.OnRedPacketOnClick() {
            @Override
            public void onRedPacketClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.Packet, PacketUI.takePacket);
            }
        });
        insertView(facadeView, null);
    }
}
