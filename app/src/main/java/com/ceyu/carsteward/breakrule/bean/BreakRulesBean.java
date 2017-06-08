package com.ceyu.carsteward.breakrule.bean;

import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.tools.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 * 违章详情实体类
 */
public class BreakRulesBean {

    private List<BreakRulesDetailBean> list;  //违章详情列表
    private int points; //扣分
    private int fine;   //罚款
    private int num;    //总违章数

    public boolean hasData(){
        return list!=null && list.size()>0;
    }


    public List<BreakRulesDetailBean> getList() {
        return list;
    }

    public void setList(List<BreakRulesDetailBean> list) {
        this.list = list;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public static class BreakRulesDetailBean{
        private String state;
        private String date;
        private String area;
        private String event;
        private String points;
        private String fine;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getFine() {
            return fine;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        @Override
        public String toString() {
            return "BreakRulesDetailBean{" +
                    "state='" + state + '\'' +
                    ", date='" + date + '\'' +
                    ", area='" + area + '\'' +
                    ", event='" + event + '\'' +
                    ", points='" + points + '\'' +
                    ", fine='" + fine + '\'' +
                    '}';
        }
    }
    public static BreakRulesBean parse0(JSONObject jsonObject){
        try{
            BreakRulesBean bean = new BreakRulesBean();
            if(jsonObject.has("points")) bean.setPoints(jsonObject.getInt("points"));
            if(jsonObject.has("fine")) bean.setFine(jsonObject.getInt("fine"));
            if(jsonObject.has("num")) bean.setNum(jsonObject.getInt("num"));
            if(jsonObject.has("list")){
                JSONArray list = jsonObject.getJSONArray("list");
                bean.setList(new ArrayList<>(parse1(list)));
            }
            return bean;
        }catch (Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
    }
    public static List<BreakRulesDetailBean> parse1(JSONArray jsonArray){
        try{
            List<BreakRulesDetailBean> list = new LinkedList<>();
            for(int i=0; i<jsonArray.length(); i++){
                try{
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    list.add(parse2(jsonObject));
                }catch(Exception e){
                    Utility.LogUtils.ex(e);
                }
            }
            return list;
        }catch (Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
    }

    public static BreakRulesDetailBean parse2(JSONObject jsonObject){
        try{
            BreakRulesDetailBean bean = new BreakRulesDetailBean();
            if(jsonObject.has("state")) bean.setState(jsonObject.getString("state"));
            if(jsonObject.has("date")) bean.setDate(jsonObject.getString("date"));
            if(jsonObject.has("area")) bean.setArea(jsonObject.getString("area"));
            if(jsonObject.has("event")) bean.setEvent(jsonObject.getString("event"));
            if(jsonObject.has("points")) bean.setPoints(jsonObject.getString("points"));
            if(jsonObject.has("fine")) bean.setFine(jsonObject.getString("fine"));
            return bean;
        }catch(Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
    }

    public static ErrorInfoBean parseError(JSONObject jsonObject){
        try{
            ErrorInfoBean error = new ErrorInfoBean();
            if(jsonObject.has("state")) error.setState(jsonObject.getString("state"));
            if(jsonObject.has("info")) error.setInfo(jsonObject.getString("info"));
            if(error.getState()!=null&& ResponseCode.ResponseError.equals(error.getState())) return error;
            else return null;
        }catch(Exception e){
            Utility.LogUtils.ex(e);
            return null;
        }
    }

    public static class ErrorInfoBean{
        private String state;
        private String info;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
