package com.ceyu.carsteward.engineer.bean;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerMainBean {
    public static final String TABLE = "choice_engineer";

    private static final String LEVEL = "level";
    private static final String BRAND = "mcarmodel";
    private static final String PROVINCE = "province";
    private static final String CODE = "code";

    private String _level;
    private String _brand;
    private String _province;
    private long _code;

    public String get_level() {
        return _level;
    }

    public void set_level(String _level) {
        this._level = _level;
    }

    public String get_brand() {
        return _brand;
    }

    public void set_brand(String _brand) {
        this._brand = _brand;
    }

    public String get_province() {
        return _province;
    }

    public void set_province(String _province) {
        this._province = _province;
    }

    public long get_code() {
        return _code;
    }

    public void set_code(long _code) {
        this._code = _code;
    }

    public static EngineerMainBean fromJSONObjectString(String resource){
        try {
            JSONObject object = new JSONObject(resource);
            EngineerMainBean bean = new EngineerMainBean();
            if (object.has(LEVEL)){
                bean.set_level(object.optString(LEVEL));
            }
            if (object.has(BRAND)){
                bean.set_brand(object.optString(BRAND));
            }
            if (object.has(PROVINCE)){
                bean.set_province(object.optString(PROVINCE));
            }
            if (object.has(CODE)){
                bean.set_code(object.optLong(CODE));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String genEngineerInfo(){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(TABLE);
        builder.append("(").append("_id ");
        builder.append(" INTEGER primary key autoincrement,");
        builder.append(LEVEL).append(" TEXT,");
        builder.append(BRAND).append(" TEXT,");
        builder.append(PROVINCE).append(" TEXT,");
        builder.append(CODE).append(" DOUBLE);");
        return builder.toString();
    }

    public ContentValues toValues(){
        ContentValues values = new ContentValues();
        values.put(LEVEL, get_level());
        values.put(BRAND, get_brand());
        values.put(PROVINCE, get_province());
        values.put(CODE, System.currentTimeMillis());
        return values;
    }

    public static EngineerMainBean fromCursor(Cursor cursor){
        if (cursor == null){
            return  null;
        }
        EngineerMainBean engineerMainBean = new EngineerMainBean();
        int index = cursor.getColumnIndex(LEVEL);
        if (index > -1){
            engineerMainBean.set_level(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BRAND);
        if (index > -1){
            engineerMainBean.set_brand(cursor.getString(index));
        }

        index = cursor.getColumnIndex(PROVINCE);
        if (index > -1){
            engineerMainBean.set_province(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CODE);
        if (index > -1){
            engineerMainBean.set_code(cursor.getLong(index));
        }
        return engineerMainBean;
    }
}
