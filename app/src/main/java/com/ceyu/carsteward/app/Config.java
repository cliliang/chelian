/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.app;

import android.content.Context;

public class Config {
    public static boolean DevelopeMode = true;

    public static boolean isDevelopeMode () {
        return DevelopeMode;
    }

    public static void init(Context context) {
//        String serverMode = Utils.getMetaValue(context, "SERVER_MODE");
//        if (serverMode.equalsIgnoreCase("release")) {
//            DevelopeMode = false;
//        }
    }
}
