/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.user.router;


import com.ceyu.carsteward.common.module.ModuleID;

public class UserUI {
    private final static int MAIN = ModuleID.User;

    public static final int userLogin = MAIN + 1;
    public static final int aboutBang = MAIN + 2;
    public static final int bangSuggest = MAIN + 3;
    public static final int bangConnect = MAIN + 4;
    public static final int bangService = MAIN + 5;
    private UserUI() {

    }
}
