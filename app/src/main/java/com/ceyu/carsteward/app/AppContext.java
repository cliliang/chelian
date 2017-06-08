/*
 * Copyright (c) 2014 Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.ceyu.carsteward.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.ceyu.carsteward.ad.AdMod;
import com.ceyu.carsteward.breakrule.BreakRulesMod;
import com.ceyu.carsteward.car.CarMod;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.common.db.CheDBM;
import com.ceyu.carsteward.common.db.CheDatabaseOpenHelper;
import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.tools.BaiduLBS;
import com.ceyu.carsteward.common.tools.CrashHandler;
import com.ceyu.carsteward.engineer.EngineerMod;
import com.ceyu.carsteward.extra.ExtraMod;
import com.ceyu.carsteward.main.MainMod;
import com.ceyu.carsteward.main.router.MainUI;
import com.ceyu.carsteward.maintain.MaintainMod;
import com.ceyu.carsteward.packet.PacketMod;
import com.ceyu.carsteward.points.PointsMod;
import com.ceyu.carsteward.record.RecordMod;
import com.ceyu.carsteward.self.SelfMod;
import com.ceyu.carsteward.tribe.TribeMod;
import com.ceyu.carsteward.tuan.TuanMod;
import com.ceyu.carsteward.user.UserMod;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.wiki.WikiMod;
import com.ceyu.carsteward.wxapi.WXUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.Map;

public class AppContext extends Application {
    private ArrayList<ModBase> mods = new ArrayList<ModBase>();
    private User activeUser;
    private CarInfoBean carInfo;
    private CarInfoBean tempCarInfo;
    private SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        SDKInitializer.initialize(getApplicationContext());
        startBaiduLBS();
        init();
    }


    private void init() {
        Config.init(this);
        //volley任务队列初始化
        mQueue = Volley.newRequestQueue(this);
        CheDatabaseOpenHelper openHelper = CheDatabaseOpenHelper.getInstance(this);
        database = openHelper.getWritableDatabase();
        CheDBM cheDBM = CheDBM.getInstance(this);
        User user = cheDBM.queryUser();
        if (user != null) {
            activeUser = user;
        }

        fillMods();
        regToWX();
        initPush();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CrashHandler.sendCrashReportsToServer(getBaseContext());
            }
        }).start();


    }

    private void fillMods() {
        if (mods.size() == 0) {
            synchronized (AppContext.class) {
                mods.add(CarMod.getInstance());
                mods.add((AdMod.getInstance())); //首页banner广告
                mods.add(MainMod.getInstance());
                mods.add(UserMod.getInstance());
                mods.add(ExtraMod.getInstance());
                mods.add(PacketMod.getInstance());
                mods.add(EngineerMod.getInstance());
                mods.add(MaintainMod.getInstance());
                mods.add(BreakRulesMod.getInstance());  //违章查询
                mods.add(RecordMod.getInstance());  //养车记录
                mods.add(PointsMod.getInstance()); //积分
                mods.add(TuanMod.getInstance());
                mods.add(TribeMod.getInstance());
                mods.add(WikiMod.getSingleton());
                mods.add(SelfMod.getInstance());
            }
        }
    }

    private void regToWX() {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, WXUtils.APP_ID, true);
        iwxapi.registerApp(WXUtils.APP_ID);
    }

    public ArrayList<ModBase> getMods() {
        fillMods();
        return mods;
    }

    private void initPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                if (isRunningForeground(context)) {
                    if (uMessage == null || uMessage.extra == null) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key != null) {
                            bundle.putString(key, value);
                        }
                    }
                    MainRouter.getInstance(context).showActivity(ModuleNames.Main, MainUI.MainActivity, bundle);
                }else {
                    super.launchApp(context, uMessage);
                }
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setCarInfo(CarInfoBean bean) {
        this.carInfo = bean;
    }

    public CarInfoBean getCarInfo() {
        return carInfo;
    }

    public CarInfoBean getTempCarInfo() {
        return tempCarInfo;
    }

    public void setTempCarInfo(CarInfoBean tempCarInfo) {
        this.tempCarInfo = tempCarInfo;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    //全局单例的 volley RequestQueue
    private RequestQueue mQueue;

    //volley任务队列
    public RequestQueue queue() {
        return this.mQueue;
    }

    //开始百度定位
    private void startBaiduLBS() {
        //初始化百度定位工具类
        BaiduLBS baiduLBS = BaiduLBS.getInstanse(this);
        baiduLBS.startDetailLocation();
    }

    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    private boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName())) {
            return true;
        }
        return false;
    }
}