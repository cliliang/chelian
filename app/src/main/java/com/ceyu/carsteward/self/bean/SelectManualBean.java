package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.maintain.bean.MaintainRule;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by chen on 15/9/11.
 */
public class SelectManualBean {
    private List<SelfSelectBean> selectBean;
    private SelfSelectMoney moneyBean;
    private String notice;
    private String free;
    private MaintainRule rule;
    private int freeCount;

    public SelectManualBean() {
    }

    public List<SelfSelectBean> getSelectBean() {
        return selectBean;
    }

    public void setSelectBean(List<SelfSelectBean> selectBean) {
        this.selectBean = selectBean;
    }

    public SelfSelectMoney getMoneyBean() {
        return moneyBean;
    }

    public void setMoneyBean(SelfSelectMoney moneyBean) {
        this.moneyBean = moneyBean;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public MaintainRule getRule() {
        return rule;
    }

    public void setRule(MaintainRule rule) {
        this.rule = rule;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(int freeCount) {
        this.freeCount = freeCount;
    }

    public static SelectManualBean fromJson(JSONObject object){
        if (object ==  null){
            return null;
        }
        SelectManualBean bean = new SelectManualBean();
        String index = "list";
        if (object.has(index)){
            String res = object.optString(index);
            List<SelfSelectBean> selectBeans = SelfSelectBean.fromString(res);
            if (selectBeans != null){
                bean.setSelectBean(selectBeans);
            }
        }
        index = "money";
        if (object.has(index)){
            String res = object.optString(index);
            SelfSelectMoney moneyBean = SelfSelectMoney.fromString(res);
            if (moneyBean != null){
                bean.setMoneyBean(moneyBean);
            }
        }
        index = "notice";
        if (object.has(index)){
            bean.setNotice(object.optString(index));
        }
        index = "free";
        if (object.has(index)){
            String free = object.optString(index);
            String[] rulesSub = free.replace("[", "").replace("]", "").replaceAll("\"", "").split(",");
            bean.setFreeCount(rulesSub.length);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rulesSub.length; i++){
                builder.append(rulesSub[i]);
                builder.append("\n");
            }
            String result = builder.toString();
            if (result.length() > 1){
                result = result.substring(0, result.length() - 1);
            }
            bean.setFree(result);
        }
        index = "rule";
        if (object.has(index)){
            bean.setRule(MaintainRule.fromString(object.optString(index)));
        }
        return bean;
    }
}
