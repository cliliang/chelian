package com.ceyu.carsteward.record.bean;

import com.ceyu.carsteward.common.tools.Utility;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/7/1.
 */
public class RecordList {

    private List<RecordBean> list;
    private boolean hasMore;

    public List<RecordBean> getList() {
        return list;
    }

    public void setList(List<RecordBean> list) {
        this.list = list;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public static RecordList parse(JSONObject jsonObject){
        RecordList recordList = new RecordList();
        recordList.setList(RecordBean.parse(jsonObject));
        recordList.setHasMore(Utility.hasMore(jsonObject));
        return recordList;
    }

    @Override
    public String toString() {
        return "RecordList{" +
                "list=" + list +
                ", hasMore=" + hasMore +
                '}';
    }
}
