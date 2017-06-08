package com.ceyu.carsteward.main.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.main.guard.GuardActivity;
import com.ceyu.carsteward.main.ui.FlashActivity;
import com.ceyu.carsteward.main.ui.MainActivity;

/**
 * Created by chen on 15/6/2.
 */
public class MajorRouter extends RouterBase{
    private static MajorRouter singleInstance;
    private MajorRouter(Context context) {
        super(context);
        maps.put(MainUI.MainActivity, MainActivity.class);
        maps.put(MainUI.Flash, FlashActivity.class);
        maps.put(MainUI.GuardActivity, GuardActivity.class);
    }

    public static MajorRouter getInstance(Context context){
        if (singleInstance == null){
            synchronized (MajorRouter.class){
                singleInstance = new MajorRouter(context);
            }
        }
        return singleInstance;
    }
}
