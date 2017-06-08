package com.ceyu.carsteward.engineer.bean;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/11.
 */
public class MyEngineers {

    private ArrayList<EngineerOrderInfo> infos;
    private boolean haveNext;

    public MyEngineers() {
    }

    public ArrayList<EngineerOrderInfo> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<EngineerOrderInfo> infos) {
        this.infos = infos;
    }

    public boolean isHaveNext() {
        return haveNext;
    }

    public void setHaveNext(boolean haveNext) {
        this.haveNext = haveNext;
    }

    public static MyEngineers fromJsonObject(JSONObject object){
        if (object == null){
            return  null;
        }
        MyEngineers myEngineers = new MyEngineers();
        String index = "list";
        if (object.has(index)){
            String infos = object.optString(index);
            myEngineers.setInfos(EngineerOrderInfo.fromString(infos));
        }
        index = "more";
        if (object.has(index)){
            myEngineers.setHaveNext(object.optInt(index) == 1);
        }
        return myEngineers;
    }
}
