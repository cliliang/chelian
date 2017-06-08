package com.ceyu.carsteward.maintain.router;

import com.ceyu.carsteward.common.module.ModuleID;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainUI {

    private final static int MAIN = ModuleID.Maintain;

    public final static int getMaintain = MAIN + 1;
    public final static int getMaintainList = MAIN + 2;
    public final static int getMaintainCombo = MAIN + 3;
    public final static int maintainReserve = MAIN + 4;
    public final static int takeCatAtHome = MAIN + 5;
    public final static int paySuccess = MAIN + 6;
    public final static int getOrderList = MAIN + 7;
    public final static int getOrderInfo = MAIN + 8;
    public final static int getShopInfo = MAIN + 9;
    public final static int getShopPhoto = MAIN + 10;
}
