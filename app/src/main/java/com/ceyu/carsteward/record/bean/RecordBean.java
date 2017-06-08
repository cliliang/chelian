package com.ceyu.carsteward.record.bean;

import android.text.TextUtils;

import com.ceyu.carsteward.common.tools.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/1.
 * 养车记录列表实体类
 */
public class RecordBean {

    /*
    {
        pic: "http://api.cheliantime.comupload/record/20150701/1025078453.jpg",
        info: "第28条养车记录",
        date: "2015-07-01 10:25:07"
    },
    //增加了地点
    {
        id: "71",
        pic: "http://app3.cheliantime.com/upload/record/20150709/1220537760.jpg",
        info: "2",
        city: "北京市",
        date: "2015-07-09 12:20:53"
    }
     */

    private String id;
    private String info;    //养车记录内容
    private String date;    //日期时间
    private String[] pic;   //照片（逗号分隔）
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getPic() {
        return pic;
    }

    public void setPic(String[] pic) {
        this.pic = pic;
    }

    public static List<RecordBean> parse(JSONObject jsonObject){
        if(jsonObject == null) return null;
        if(jsonObject.has("list")){
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<RecordBean> list = parse(jsonArray);
                return (list==null||list.size()<1) ? null:new ArrayList<>(list);
            }catch (Exception e){
                Utility.LogUtils.ex(e);
                return null;
            }
        }else{
            return null;
        }
    }

    public static List<RecordBean> parse(JSONArray jsonArray){
        List<RecordBean> list = new LinkedList<>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RecordBean bean = parseBean(jsonObject);
                list.add(bean);
            }catch (Exception e){
                Utility.LogUtils.ex(e);
            }
        }
        return list;
    }

    public static RecordBean parseBean(JSONObject jsonObject){
        try{
            RecordBean bean = new RecordBean();
            if(jsonObject.has("id")){
                bean.setId(jsonObject.getString("id"));
            }
            if(jsonObject.has("info")){
                bean.setInfo(jsonObject.getString("info"));
            }
            if(jsonObject.has("date")){
                bean.setDate(jsonObject.getString("date"));
            }
            if(jsonObject.has("pic")){
                String pic = jsonObject.getString("pic");
                String[] pics = pic.split(",");
                bean.setPic(pics);
            }
            if(jsonObject.has("city")) bean.setCity(jsonObject.getString("city"));
            return bean;
        }catch (Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
    }

}
