package com.ceyu.carsteward.ad.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;

/**
 * Created by Administrator on 2015/7/2.
 */
public class AdRouter  extends RouterBase {

    private static AdRouter instance;
    private AdRouter(Context context) {
        super(context);
        //maps.put(AdUI.???, ???.class);
    }

    public static AdRouter getInstance(Context context){
        if (instance == null){
            synchronized (AdRouter.class){
                instance = new AdRouter(context);
            }
        }
        return instance;
    }


}
