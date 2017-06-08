package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/9/11.
 */
public class SelfSelectMoney {
    private int _material;
    private int _human;

    public SelfSelectMoney() {
    }

    public int get_material() {
        return _material;
    }

    public void set_material(int _material) {
        this._material = _material;
    }

    public int get_human() {
        return _human;
    }

    public void set_human(int _human) {
        this._human = _human;
    }

    public static SelfSelectMoney fromString(String res){
        if (StringUtils.isEmpty(res)){
            return null;
        }
        try {
            SelfSelectMoney money = new SelfSelectMoney();
            JSONObject object = new JSONObject(res);
            String index = "material";
            if (object.has(index)){
                money.set_material(object.optInt(index));
            }
            index = "human";
            if (object.has(index)){
                money.set_human(object.optInt(index));
            }
            return money;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
