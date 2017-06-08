package com.ceyu.carsteward.wiki.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;

/**
 * Created by chen on 15/8/26.
 */
public class WikiRouter extends RouterBase {
    private static WikiRouter singleton = null;
    private WikiRouter(Context context){
        super(context);
    }

    public static WikiRouter getSingleton(Context context) {
        if (singleton == null) {
            synchronized (WikiRouter.class){
                if (singleton == null){
                    singleton = new WikiRouter(context);
                }
            }
        }
        return singleton;
    }
}
