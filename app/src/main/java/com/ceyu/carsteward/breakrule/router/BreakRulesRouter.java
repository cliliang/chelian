package com.ceyu.carsteward.breakrule.router;

import android.content.Context;

import com.ceyu.carsteward.breakrule.main.BreakRulesListActivity;
import com.ceyu.carsteward.common.module.RouterBase;

/**
 * Created by Administrator on 2015/6/30.
 */
public class BreakRulesRouter extends RouterBase{

    private static BreakRulesRouter instance;
    private BreakRulesRouter(Context context) {
        super(context);
        maps.put(BreakRulesUI.recordList, BreakRulesListActivity.class);
    }

    public static BreakRulesRouter getInstance(Context context){
        if (instance == null){
            instance = new BreakRulesRouter(context);
        }
        return instance;
    }
}
