package com.ceyu.carsteward.record.router;

import android.content.Context;

import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.record.main.RecordListActivity;
import com.ceyu.carsteward.record.main.RecordUploadActivity;

/**
 * Created by Administrator on 2015/6/30.
 */
public class RecordRouter extends RouterBase{

    private static RecordRouter instance;
    private RecordRouter(Context context) {
        super(context);
        maps.put(RecordUI.recordList, RecordListActivity.class);
        maps.put(RecordUI.uploadRecord, RecordUploadActivity.class);
    }

    public static RecordRouter getInstance(Context context){
        if (instance == null){
            instance = new RecordRouter(context);
        }
        return instance;
    }
}
