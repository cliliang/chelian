package com.ceyu.carsteward.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.engineer.bean.EngineerMainBean;
import com.ceyu.carsteward.user.bean.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chen on 15/6/2.
 */
public class CheDBM {
    private static SQLiteDatabase database;
    private static CheDBM dbmanager;
    private Context mContext;

    private CheDBM(Context context){
       this.mContext = context;
        AppContext appContext = (AppContext) context.getApplicationContext();
        database = appContext.getDatabase();
    }

    public static CheDBM getInstance(Context context){
        if (dbmanager == null){
            dbmanager = new CheDBM(context);
        }
        return dbmanager;
    }

    public boolean insertUser(User user){
        deleteAllUsers();
        if (user == null){
            return false;
        }
        long rows = database.insert(User.TABLE, null, user.toValues());
        return rows > 0;
    }

    public User queryUser(){
        Cursor cursor = null;
        try {
            cursor = database.query(User.TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToNext()){
                return User.fromCursor(cursor);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    public boolean updateUser(int cityId, String cityName){
        User user = queryUser();
        if (user != null){
            ContentValues values = new ContentValues();
            values.put(User.iCityId, cityId);
            values.put(User.iCityName, cityName);
            int rows = database.update(User.TABLE, values, User.iPhone + "=?", new String[]{user.getPhoneNumber()});
            return rows > 0;
        }
        return false;
    }

    public void updateUser(User user){
        if (user == null){
            return;
        }
        ContentValues values = user.toValues();
        database.update(User.TABLE, values, null, null);

    }

    public void quitAccount(){
        String selection = User.iActive + "=?";
        String[] args = new String[]{String.valueOf(1)};
        int delete = database.delete(User.TABLE, selection, args);
        Log.i("chen", "delete :" + delete);
    }

    public User queryUser(String phoneNumber){
        String selection = User.iPhone + "=?";
        String[] args = new String[]{phoneNumber};
        Cursor cursor = null;
        try {
            cursor = database.query(User.TABLE, null, selection, args, null, null, null);
            if (cursor != null && cursor.moveToNext()){
                return User.fromCursor(cursor);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    public void deleteAllUsers(){
        database.delete(User.TABLE, null, null);
    }


    public String getEngineerDefaultInfo(){
        String engineerInfo = "";
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.chelian);
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            engineerInfo = sb.toString();

        } catch (java.io.IOException e1) {
            e1.printStackTrace();
        }
        return engineerInfo;
    }

    public EngineerMainBean getDefaultEngineerInfo(){
        String defaultInfo = getEngineerDefaultInfo();
        return EngineerMainBean.fromJSONObjectString(defaultInfo);
    }

}
