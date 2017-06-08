/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.user.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.user.main.AboutBangActivity;
import com.ceyu.carsteward.user.main.BangConnectActivity;
import com.ceyu.carsteward.user.main.BangServiceActivity;
import com.ceyu.carsteward.user.main.BangSuggestActivity;
import com.ceyu.carsteward.user.main.UserLoginActivity;

public class UserRouter extends RouterBase {
    private static UserRouter singleton = null;
    private UserRouter(Context context) {
        super(context);
        maps.put(UserUI.userLogin, UserLoginActivity.class);
        maps.put(UserUI.aboutBang, AboutBangActivity.class);
        maps.put(UserUI.bangSuggest, BangSuggestActivity.class);
        maps.put(UserUI.bangConnect, BangConnectActivity.class);
        maps.put(UserUI.bangService, BangServiceActivity.class);
    }

    public static UserRouter getInstance(Context context) {
        if (singleton == null) {
            synchronized (UserRouter.class) {
                if (singleton == null) {
                    singleton = new UserRouter(context);
                }
            }
        }
        return singleton;
    }
}
