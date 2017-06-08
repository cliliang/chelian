package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class LevelBean {
    public static final String LEVEL_ID = "id";
    public static final String LEVEL_NAME = "name";

    private int levelId;
    private String levelName;

    public LevelBean() {
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public static ArrayList<LevelBean> formJSONArrayString(String resource){
        ArrayList<LevelBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                LevelBean bean = new LevelBean();
                if (object.has(LEVEL_ID)){
                    bean.setLevelId(object.optInt(LEVEL_ID));
                }
                if (object.has(LEVEL_NAME)){
                    bean.setLevelName(object.optString(LEVEL_NAME));
                }
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
