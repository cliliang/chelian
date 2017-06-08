/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.common.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ceyu.carsteward.common.ui.BaseActivity;

import java.util.HashMap;

public class RouterBase {
    protected HashMap<Integer, Class<? extends BaseActivity>> maps = new HashMap<>();
    private Context context;

    public RouterBase(Context context) {
        this.context = context;
    }

    public boolean showActivity(int id) {
        return showActivity(id, null);
    }

    public boolean showActivity(int id, Bundle bundle) {
        Class<? extends BaseActivity> lookClass = maps.get(id);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    //Show Activity with flags.
    public boolean showActivity(int id,int flags) {
        return showActivity(id, null,flags);
    }

    public boolean showActivity(int id, Bundle bundle,int flags) {
        Class<? extends BaseActivity> lookClass = maps.get(id);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            intent.setFlags(flags);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean showActivityForResult(Activity parentActivity, int id, int requestCode, Bundle param) {
        Class<? extends BaseActivity> lookClass = maps.get(id);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (param != null) {
                intent.putExtras(param);
            }
            parentActivity.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    public boolean showActivityForResult(Fragment fragment, int id, int requestCode, Bundle param) {
        if (fragment == null) return false;
        Class<? extends BaseActivity> lookClass = maps.get(id);
        if (lookClass != null) {
            Intent intent = new Intent(context, lookClass);
            if (param != null) {
                intent.putExtras(param);
            }
            fragment.startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }
}
