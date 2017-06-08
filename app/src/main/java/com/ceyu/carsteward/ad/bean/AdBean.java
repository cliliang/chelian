package com.ceyu.carsteward.ad.bean;

import android.text.TextUtils;

import com.ceyu.carsteward.common.tools.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 * 首页广告实体类
 * 混淆注意：如需混淆此类请重构parse方法
 */
public class AdBean {

    private List<AdBean> list;
    private String pic;
    private String act;

    /*
    [
        {
            pic: "http://img1.cache.netease.com/auto/2015/7/2/20150702094634a5f8b.jpg",
            act: ""
        },
        {
            pic: "http://img1.cache.netease.com/catchpic/0/0D/0DDCF13E7798F35D12CF9A4E4BAEAC82.jpg",
            act: ""
        }
    ]
     */

//    public static AdBean parse(JSONArray jsonArray){
//        if(jsonArray == null) return null;
//        try {
//            return new AdBean(JSONParser.parse(jsonArray, AdBean.class));
//        } catch (Exception e) {
//            Utility.LogUtils.ex(e);
//            return null;
//        }
//    }

    public static AdBean parse(JSONArray jsonArray){
        //AdBean bean = new AdBean();
        LinkedList<AdBean> list = new LinkedList<>();
        try{
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AdBean ad = parse(jsonObject);
                list.add(ad);
            }
        }catch(Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
        return new AdBean(new ArrayList<>(list));
    }

    public static AdBean parse(JSONObject jsonObject){
        try{
            AdBean ad = new AdBean();
            if(jsonObject.has("pic")) ad.setPic(jsonObject.getString("pic"));
            if(jsonObject.has("act")) ad.setAct(jsonObject.getString("act"));
            return ad;
        }catch (Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }

    }

    public boolean noAd(){
        if(this.list==null) return true;    //无数据
        else if(this.list.size()==0) return true;   //列表长度为0
        else if(this.list.size()==1 && TextUtils.isEmpty(this.list.get(0).getPic())) return true;   //列表长度为1，唯一一项的广告地址为空（防止服务器异常）
        else return false;
    }

    public AdBean() {
    }

    public AdBean(List<AdBean> list) {
        this.list = list;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public List<AdBean> getList() {
        return list;
    }

    public void setList(List<AdBean> list) {
        this.list = list;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "pic='" + pic + '\'' +
                ", act='" + act + '\'' +
                '}';
    }
}
