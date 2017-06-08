/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.common.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
        int length = activityStack.size();
        for (int i = 0;i < length; i++){
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
                i--;
                length--;
            }
        }
    }

    public void PopToActivity(Class<?> cls) {
        boolean bFind = false;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
               bFind = true;
               break;
            }
        }

        if (bFind) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                Activity act = activityStack.peek();
                if (null != act) {
                    if (!act.getClass().equals(cls)) {
                        activityStack.pop();
                        act.finish();
                    } else {
                        break;
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            Activity act = activityStack.pop();
            if (null != act) {
                act.finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    /**
     * 退出应用程序
     */
    public void AppTerminate(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getActivityCount() {
        if (activityStack == null) {
            return 0;
        }
        return activityStack.size();
    }
}