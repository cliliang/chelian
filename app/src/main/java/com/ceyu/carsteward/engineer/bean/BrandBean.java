package com.ceyu.carsteward.engineer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class BrandBean {

    private static final String BRAND_ID = "id";
    private static final String BRAND_NAME = "name";
    private static final String BRAND_INDEX = "index";
    private static final String BRAND_PIC = "pic";

    private int brandId;
    private String brandName;
    private String brandIndex;
    private String brandPic;

    public BrandBean() {
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandIndex() {
        return brandIndex;
    }

    public void setBrandIndex(String brandIndex) {
        this.brandIndex = brandIndex;
    }

    public String getBrandPic() {
        return brandPic;
    }

    public void setBrandPic(String brandPic) {
        this.brandPic = brandPic;
    }

    public static ArrayList<BrandBean> fromJSONArrayString(String resource){
        ArrayList<BrandBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                BrandBean bean = new BrandBean();
                if (object.has(BRAND_ID)){
                    bean.setBrandId(object.optInt(BRAND_ID));
                }
                if (object.has(BRAND_NAME)){
                    bean.setBrandName(object.optString(BRAND_NAME));
                }
                if (object.has(BRAND_INDEX)){
                    bean.setBrandIndex(object.optString(BRAND_INDEX));
                }
                if (object.has(BRAND_PIC)){
                    bean.setBrandPic(object.optString(BRAND_PIC));
                }
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
