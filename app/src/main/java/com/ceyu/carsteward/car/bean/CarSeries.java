package com.ceyu.carsteward.car.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class CarSeries {
    private int _id;
    private String _name;
    private ArrayList<CarModelBean> modelList;

    public CarSeries() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ArrayList<CarModelBean> getModelList() {
        return modelList;
    }

    public void setModelList(ArrayList<CarModelBean> modelList) {
        this.modelList = modelList;
    }

    public static ArrayList<CarSeries> fromjsonArray(JSONArray array){
        ArrayList<CarSeries> arrayList = new ArrayList<>();
        for (int i = 0;  i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                CarSeries series = new CarSeries();
                String index = "id";
                if (object.has(index)){
                    series.set_id(object.optInt(index));
                }
                index = "name";
                if (object.has(index)){
                    series.set_name(object.optString(index));
                }
                index = "sub";
                if (object.has(index)){
                    series.setModelList(CarModelBean.fromString(object.optString(index)));
                }
                arrayList.add(series);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
