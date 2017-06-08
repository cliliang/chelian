package com.ceyu.carsteward.wiki;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.wiki.router.WikiRouter;

/**
 * Created by chen on 15/8/26.
 */
public class WikiMod extends ModBase {

    private static WikiMod singleton = null;
    private WikiMod(){
        super(ModuleNames.Wiki);
    }

    public static WikiMod getSingleton(){
        if (singleton == null){
            singleton = new WikiMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return WikiRouter.getSingleton(context);
    }
}
