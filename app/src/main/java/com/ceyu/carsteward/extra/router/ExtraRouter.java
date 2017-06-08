package com.ceyu.carsteward.extra.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.extra.main.ViolationMainActivity;

/**
 * Created by chen on 15/6/1.
 */
public class ExtraRouter extends RouterBase {

    private static ExtraRouter singleInstance;
    private ExtraRouter(Context context) {
        super(context);
        maps.put(ExtraUI.Violation, ViolationMainActivity.class);
    }

    public static ExtraRouter getInstance(Context context){
        if (singleInstance == null){
            synchronized (ExtraRouter.class){
                singleInstance = new ExtraRouter(context);
            }
        }
        return singleInstance;
    }
}
