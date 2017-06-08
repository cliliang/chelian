package com.ceyu.carsteward.common.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.EngineerMainBean;
import com.ceyu.carsteward.user.bean.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by chen on 15/6/2.
 */
public class CheDatabaseOpenHelper extends SQLiteOpenHelper {
    private static CheDatabaseOpenHelper instance;
    private final static String DATABASE_NAME = "chelian.db";
    private final static int DATABASE_VERSION = 2;

    private CheDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static CheDatabaseOpenHelper getInstance(Context context){
        if (instance == null){
            instance = new CheDatabaseOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.genUserDatabaseTable());
        db.execSQL(User.upgradeFrom1To2());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2){
            db.execSQL(User.upgradeFrom1To2());
        }
    }
}
