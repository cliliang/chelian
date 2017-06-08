package com.ceyu.carsteward.maintain.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.maintain.main.MaintainComboActivity;
import com.ceyu.carsteward.maintain.main.MaintainMainActivity;
import com.ceyu.carsteward.maintain.main.MaintainOrderActivity;
import com.ceyu.carsteward.maintain.main.MaintainOrderListActivity;
import com.ceyu.carsteward.maintain.main.MaintainReserveActivity;
import com.ceyu.carsteward.maintain.main.MaintainShopListActivity;
import com.ceyu.carsteward.maintain.main.MaintainTakeCarActivity;
import com.ceyu.carsteward.maintain.main.ReservePaySuccessActivity;
import com.ceyu.carsteward.maintain.main.ShopInfoActivity;
import com.ceyu.carsteward.maintain.main.ShopPhotoActivity;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainRouter extends RouterBase {
    private static MaintainRouter instance;
    private MaintainRouter(Context context) {
        super(context);
        maps.put(MaintainUI.getMaintain, MaintainMainActivity.class);
        maps.put(MaintainUI.getMaintainList, MaintainShopListActivity.class);
        maps.put(MaintainUI.getMaintainCombo, MaintainComboActivity.class);
        maps.put(MaintainUI.maintainReserve, MaintainReserveActivity.class);
        maps.put(MaintainUI.takeCatAtHome, MaintainTakeCarActivity.class);
        maps.put(MaintainUI.paySuccess, ReservePaySuccessActivity.class);
        maps.put(MaintainUI.getOrderList, MaintainOrderListActivity.class);
        maps.put(MaintainUI.getOrderInfo, MaintainOrderActivity.class);
        maps.put(MaintainUI.getShopInfo, ShopInfoActivity.class);
        maps.put(MaintainUI.getShopPhoto, ShopPhotoActivity.class);
    }

    public static MaintainRouter getInstance(Context context){
        if (instance == null){
            synchronized (MaintainRouter.class){
                if (instance == null){
                    instance = new MaintainRouter(context);
                }
            }
        }
        return instance;
    }
}
