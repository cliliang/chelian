package com.ceyu.carsteward.packet.router;

import android.content.Context;

import com.ceyu.carsteward.breakrule.main.CarListActivity;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.packet.main.MyRedPacketsActivity;
import com.ceyu.carsteward.packet.main.TakeRedPacketActivity;

/**
 * Created by chen on 15/6/8.
 */
public class PacketRouter extends RouterBase{
    private static PacketRouter instance;
    private PacketRouter(Context context) {
        super(context);
        maps.put(PacketUI.takePacket, TakeRedPacketActivity.class);
        maps.put(PacketUI.myPacket, MyRedPacketsActivity.class);
        maps.put(PacketUI.breakRulesRecord, CarListActivity.class);
    }

    public static PacketRouter getInstance(Context context){
        if (instance == null){
            synchronized (PacketRouter.class){
                instance = new PacketRouter(context);
            }
        }
        return instance;
    }
}
