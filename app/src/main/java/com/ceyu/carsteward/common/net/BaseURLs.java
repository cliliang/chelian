/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.common.net;

import com.ceyu.carsteward.app.Config;

public class BaseURLs {
    public final static String HTTP = "http://";
    protected final static String URL_SPLITTER = "/";
    protected final static String URL_UNDERLINE = "_";
    protected final static String Extra = "3.0";
    private final static String HOST = "";
    private final static String DEV_HOST = "app3.cheliantime.com";

    protected static final String URL_MAIN_HOST = HTTP + getHost() + URL_SPLITTER;
    protected static String getHost() {
        if (Config.isDevelopeMode()) {
            return DEV_HOST;
        } else {
            return HOST;
        }
    }

}
