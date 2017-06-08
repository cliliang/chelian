package com.ceyu.carsteward.engineer.bean;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/12.
 */
public class OnlineEngineers {

    private ArrayList<OnlineEngineerInfo> engineerInfos;

    public ArrayList<OnlineEngineerInfo> getEngineerInfos() {
        return engineerInfos;
    }

    public void setEngineerInfos(ArrayList<OnlineEngineerInfo> engineerInfos) {
        this.engineerInfos = engineerInfos;
    }

    public static OnlineEngineers fromJsonObject(JSONObject object){
        if (object == null){
            return null;
        }
        OnlineEngineers onlineEngineers = new OnlineEngineers();
        if (object.has("list")){
            String res = object.optString("list");
            onlineEngineers.setEngineerInfos(OnlineEngineerInfo.fromString(res));
        }

        return onlineEngineers;
    }
}
