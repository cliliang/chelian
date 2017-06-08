package com.ceyu.carsteward.engineer.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.engineer.main.EngineerCommentActivity;
import com.ceyu.carsteward.engineer.main.EngineerInfoActivity;
import com.ceyu.carsteward.engineer.main.EngineerMainActivity;
import com.ceyu.carsteward.engineer.main.EngineerOfMyActivity;
import com.ceyu.carsteward.engineer.main.EngineerPacketPayActivity;
import com.ceyu.carsteward.engineer.main.EngineerPayActivity;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerRouter extends RouterBase{
    private static EngineerRouter singleton = null;
    private EngineerRouter(Context context) {
        super(context);
        maps.put(EngineerUI.engineerMain, EngineerMainActivity.class);
        maps.put(EngineerUI.engineerInfo, EngineerInfoActivity.class);
        maps.put(EngineerUI.engineerPay, EngineerPayActivity.class);
        maps.put(EngineerUI.engineerPacketPay, EngineerPacketPayActivity.class);
        maps.put(EngineerUI.engineerOfMine, EngineerOfMyActivity.class);
        maps.put(EngineerUI.engineerComment, EngineerCommentActivity.class);
    }

    public static EngineerRouter getInstance(Context context) {
        if (singleton == null) {
            synchronized (EngineerRouter.class) {
                if (singleton == null) {
                    singleton = new EngineerRouter(context);
                }
            }
        }
        return singleton;
    }
}
