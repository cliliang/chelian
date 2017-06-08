package com.ceyu.carsteward.user.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import org.json.JSONObject;

/**
 * Created by chen on 15/6/2.
 */
public class User {
    public static final String TABLE = "user_table";

    public static final String iPhone = "iphone";
    public static final String iToken = "token";
    public static final String iPic = "pic";
    public static final String newUser = "newUser";
    public static final String iCityId = "cityId";
    public static final String iCityName = "cityName";
    public static final String iActive = "active";
    public static final String iNickName = "nickName";


    private String phoneNumber;
    private String userPic;
    private String token;
    private boolean _isNew;
    private int cityId;
    private String cityName;
    private boolean active;
    private String name;

    public User() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNew() {
        return _isNew;
    }

    public void setIsNew(boolean isNew) {
        this._isNew = isNew;
    }


    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User fromJsonObject(JSONObject jsonObject){
        User user = new User();
        String item = "token";
        if (jsonObject.has(item)){
            user.setToken(jsonObject.optString(item));
        }

        item = "pic";
        if (jsonObject.has(item)){
            user.setUserPic(jsonObject.optString(item));
        }

        item = "isnew";
        if (jsonObject.has(item)){
            user.setIsNew(jsonObject.optInt(item) == 1);
        }

        item = "name";
        if (jsonObject.has(item)){
            user.setName(jsonObject.optString(item));
        }

        return user;
    }

    public static String genUserDatabaseTable(){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(TABLE);
        builder.append("(").append("_id ");
        builder.append(" INTEGER primary key autoincrement,");
        builder.append(iPhone).append(" TEXT,");
        builder.append(iToken).append(" TEXT,");
        builder.append(iPic).append(" TEXT,");
        builder.append(iCityId).append(" INTEGER,");
        builder.append(iCityName).append(" TEXT,");
        builder.append(iActive).append(" INTEGER,");
        builder.append(newUser).append(" INTEGER);");
        return builder.toString();
    }

    public static String upgradeFrom1To2(){
        return "ALTER TABLE " + TABLE + " ADD " + iNickName + " TEXT;";
    }

    public ContentValues toValues(){
        ContentValues values = new ContentValues();
        values.put(iPhone, getPhoneNumber());
        values.put(iPic, getUserPic());
        values.put(iToken, getToken());
        values.put(newUser, isNew() ? 1 : 0);
        values.put(iCityId, getCityId());
        values.put(iCityName, getCityName());
        values.put(iActive, isActive() ? 1 : 0);
        values.put(iNickName, getName());
        return values;
    }

    public static User fromCursor(Cursor cursor){
        if (cursor == null){
            return null;
        }
        User user = new User();
        int index = cursor.getColumnIndex(iPhone);
        if (index > -1){
            user.setPhoneNumber(cursor.getString(index));
        }
        index = cursor.getColumnIndex(iPic);
        if (index > -1){
            user.setUserPic(cursor.getString(index));
        }

        index = cursor.getColumnIndex(iToken);
        if (index > -1){
            user.setToken(cursor.getString(index));
        }

        index = cursor.getColumnIndex(newUser);
        if (index > -1){
            user.setIsNew(cursor.getInt(index) == 1);
        }
        index = cursor.getColumnIndex(iCityId);
        if (index > -1){
            user.setCityId(cursor.getInt(index));
        }
        index = cursor.getColumnIndex(iCityName);
        if (index > -1){
            user.setCityName(cursor.getString(index));
        }
        index = cursor.getColumnIndex(iActive);
        if (index > -1){
            user.setActive(cursor.getInt(index) == 1);
        }
        index = cursor.getColumnIndex(iNickName);
        if (index > -1){
            user.setName(cursor.getString(index));
        }
        return user;

    }
}
