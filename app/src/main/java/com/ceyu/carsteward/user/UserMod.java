package com.ceyu.carsteward.user;

import android.content.Context;

import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.user.facade.UserFacadeView;
import com.ceyu.carsteward.user.router.UserRouter;

/**
 * Created by chen on 15/6/1.
 */
public class UserMod extends ModBase {

    private static UserMod singleton;
    private UserMod() {
        super(ModuleNames.User);
    }

    public static UserMod getInstance(){
        if (singleton == null){
            singleton = new UserMod();
        }
        return singleton;
    }

    @Override
    public RouterBase getRouter(Context context) {
        return UserRouter.getInstance(context);
    }
}
