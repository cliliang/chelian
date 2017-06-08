package com.ceyu.carsteward.self.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.self.ui.SelfChoiceTimeActivity;
import com.ceyu.carsteward.self.ui.SelfCommentActivity;
import com.ceyu.carsteward.self.ui.SelfMainActivity;
import com.ceyu.carsteward.self.ui.SelfMechanicActivity;
import com.ceyu.carsteward.self.ui.SelfOrderActivity;
import com.ceyu.carsteward.self.ui.SelfOrderReserveActivity;
import com.ceyu.carsteward.self.ui.SelfShopMapActivity;

/**
 * Created by chen on 15/8/26.
 */
public class SelfRouter extends RouterBase {
    private static SelfRouter singleton = null;
    private SelfRouter(Context context){
        super(context);
        maps.put(SelfUI.selfMain, SelfMainActivity.class);
        maps.put(SelfUI.selfMap, SelfShopMapActivity.class);
        maps.put(SelfUI.selfMechanic, SelfMechanicActivity.class);
        maps.put(SelfUI.selfTime, SelfChoiceTimeActivity.class);
        maps.put(SelfUI.selfReserve, SelfOrderReserveActivity.class);
        maps.put(SelfUI.selfOrder, SelfOrderActivity.class);
        maps.put(SelfUI.selfComment, SelfCommentActivity.class);
    }

    public static SelfRouter getInstance(Context context) {
        if (singleton == null) {
            synchronized (SelfRouter.class){
                if (singleton == null){
                    singleton = new SelfRouter(context);
                }
            }
        }
        return singleton;
    }
}
