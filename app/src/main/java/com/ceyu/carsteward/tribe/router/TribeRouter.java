package com.ceyu.carsteward.tribe.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.tribe.ui.TribeDetailActivity;
import com.ceyu.carsteward.tribe.ui.TribeMineActivity;
import com.ceyu.carsteward.tribe.ui.TribePublishActivity;

/**
 * Created by chen on 15/8/26.
 */
public class TribeRouter extends RouterBase{
    private TribeRouter(Context context){
        super(context);
        maps.put(TribeUI.tribePublic, TribePublishActivity.class);
        maps.put(TribeUI.tribeMine, TribeMineActivity.class);
        maps.put(TribeUI.tribeDetail, TribeDetailActivity.class);
    }

    private static TribeRouter singleton = null;
    public static TribeRouter getInstance(Context context){
        if (singleton == null){
            synchronized (TribeRouter.class){
                if (singleton == null){
                    singleton = new TribeRouter(context);
                }
            }
        }
        return singleton;
    }
}
