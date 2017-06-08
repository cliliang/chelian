package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/9/22.
 */
public class SelfCarBean {
    private CarBrandInfoBean _brandInfo;
    private String _plate;

    public CarBrandInfoBean get_brandInfo() {
        return _brandInfo;
    }

    public void set_brandInfo(CarBrandInfoBean _brandInfo) {
        this._brandInfo = _brandInfo;
    }

    public String get_plate() {
        return _plate;
    }

    public void set_plate(String _plate) {
        this._plate = _plate;
    }

    public static SelfCarBean fromString(String resource){
        if (StringUtils.isEmpty(resource)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(resource);
            SelfCarBean bean = new SelfCarBean();
            String index = "name";
            if (object.has(index)){
                CarBrandInfoBean brandBean = CarBrandInfoBean.fromString(object.optString(index));
                if (brandBean != null){
                    bean.set_brandInfo(brandBean);
                }
            }
            index = "plate";
            if (object.has(index)){
                bean.set_plate(object.optString(index));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
