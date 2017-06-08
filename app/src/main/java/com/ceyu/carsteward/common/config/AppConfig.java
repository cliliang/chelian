package com.ceyu.carsteward.common.config;

import android.content.Context;

public class AppConfig {
    private static SharePreferenceManager shareManager;
    private static AppConfig singleton;
    //新版本如果要显示引导页，HOME_GUIDE_VERSION值+1;
    public final int HOME_GUIDE_VERSION = 1;
    //新版本是否需要显示滑动引导页
    public static boolean Show_Guard = true;
    public static final int engineerOrder = 1; //用于付款成功后标识是哪种订单  咨询技师订单
    public static final int reserveOrder = 2; //保养订单
    public static final int tuanOrder = 3; //团购订单
    public static final int selfOrder = 4;

    private static final String VersionCode = "Version_Code";
    private static final String currentOrder = "currentOrder";
    public static final String orderType = "orderType";
    private static final String refreshCar = "refreshCar";
    private final String showHand = "showHand";
    private final String currentCarModelId = "carModelId";

    public AppConfig(Context context) {
        shareManager = SharePreferenceManager.newInstance(context);
    }

    public static AppConfig getInstance(Context context) {
        if (singleton == null) {
            synchronized (AppConfig.class) {
                if (singleton == null) {
                    singleton = new AppConfig(context);
                }
            }
        }
        return singleton;
    }

    public void setVersionCode(int versionCode){
        shareManager.setValue(VersionCode, versionCode);
    }

    public int getVersionCode(){
        return shareManager.getIntValue(VersionCode, 0);
    }

    public void setCurrentOrder(String orderSn){
        shareManager.setValue(currentOrder, orderSn);
    }

    public String getCurrentOrder(){
        return shareManager.getStringValue(currentOrder);
    }

    public void setOrderType(int type){
        shareManager.setValue(orderType, type);
    }

    public int getOrderType(){
        return shareManager.getIntValue(orderType);
    }

    public void setRefreshCar(boolean refresh){
        shareManager.setValue(refreshCar, refresh);
    }

    public boolean getRefreshCar(){
        return shareManager.getBooeanValue(refreshCar);
    }

    public void setShowedHand(boolean show){
        shareManager.setValue(showHand, show);
    }

    public boolean getShowedHand(){
        return shareManager.getBooeanValue(showHand);
    }

    public void setModelId(int modelId){
        shareManager.setValue(currentCarModelId, modelId);
    }

    public int getModelId(){
        return shareManager.getIntValue(currentCarModelId);
    }

}
