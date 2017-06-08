package com.ceyu.carsteward.car.router;

import android.content.Context;

import com.ceyu.carsteward.car.main.AddCarBrandActivity;
import com.ceyu.carsteward.car.main.AddCarDetailActivity;
import com.ceyu.carsteward.car.main.AddCarMileageActivity;
import com.ceyu.carsteward.car.main.AddCarYearActivity;
import com.ceyu.carsteward.car.main.CarOfMineActivity;
import com.ceyu.carsteward.car.main.ShowInsuranceActivity;
import com.ceyu.carsteward.common.module.RouterBase;
import com.ceyu.carsteward.record.main.RecordListActivity;

/**
 * Created by chen on 15/6/15.
 */
public class CarRouter extends RouterBase {
    private static CarRouter instance;
    private CarRouter(Context context) {
        super(context);
        maps.put(CarUI.carOfMine, CarOfMineActivity.class);
        maps.put(CarUI.addCarBrand, AddCarBrandActivity.class);
        maps.put(CarUI.addCarYear, AddCarYearActivity.class);
        maps.put(CarUI.addCarDetail, AddCarDetailActivity.class);
        maps.put(CarUI.addCarMileage, AddCarMileageActivity.class);
        maps.put(CarUI.choiceInsurance, ShowInsuranceActivity.class);
        maps.put(CarUI.maintainRecord, RecordListActivity.class);
    }

    public static CarRouter getInstance(Context context){
        if (instance == null){
            instance = new CarRouter(context);
        }
        return instance;
    }


}
