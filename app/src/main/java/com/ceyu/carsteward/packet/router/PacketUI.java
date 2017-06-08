package com.ceyu.carsteward.packet.router;

import com.ceyu.carsteward.common.module.ModuleID;

/**
 * Created by chen on 15/6/8.
 */
public class PacketUI {
    private final static int MAIN = ModuleID.Packet;

    public static final int takePacket = MAIN + 1;
    public static final int myPacket = MAIN + 2;

    //违章查询
    public static final int breakRulesRecord = MAIN+3;
}
