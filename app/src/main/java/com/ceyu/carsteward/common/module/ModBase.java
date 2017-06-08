/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.common.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

public class ModBase {

    private String name = null;

    public ModBase(String name) {
        this.name = name;
    }

    /**
     * @return module
     */
    public String getName() {
        return name;
    }

    /**
     * the module navigate router
     *
     * @param context
     * @return router or null
     */
    public RouterBase getRouter(Context context) {
        return null;
    }


    /**
     * get view to display information on general fragment
     *
     * @param context
     * @return ModFacadeView
     */
    public ModFacadeView getFacadeView(Context context) {
        return null;
    }

}
