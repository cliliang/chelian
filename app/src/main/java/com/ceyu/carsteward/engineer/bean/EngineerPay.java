package com.ceyu.carsteward.engineer.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/9.
 */
public class EngineerPay {
    private EngineerBean engineerBean;
    private float bangMoney;

    public EngineerPay() {
    }

    public EngineerBean getEngineerBean() {
        return engineerBean;
    }

    public void setEngineerBean(EngineerBean engineerBean) {
        this.engineerBean = engineerBean;
    }

    public float getBangMoney() {
        return bangMoney;
    }

    public void setBangMoney(float bangMoney) {
        this.bangMoney = bangMoney;
    }

    public static EngineerPay fromJSONObject(JSONObject object){
        if (object == null){
            return null;
        }
        EngineerPay pay = new EngineerPay();
        String index = "mechanic";
        if (object.has(index)){
            String res = object.optString(index);
            EngineerBean bean = EngineerBean.fromString(res);
            pay.setEngineerBean(bean);
        }
        index = "coupons";
        if (object.has(index)){
            String res = object.optString(index);
            try {
                JSONObject obj = new JSONObject(res);
                if (obj.has("sum")){
                    float money = (float) obj.optDouble("sum");
                    pay.setBangMoney(money);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pay;
    }
}
