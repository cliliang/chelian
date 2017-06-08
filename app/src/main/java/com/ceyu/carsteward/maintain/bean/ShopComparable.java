package com.ceyu.carsteward.maintain.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import java.util.Comparator;

/**
 * Created by chen on 15/6/24.
 */
public class ShopComparable implements Comparator<ListShopBean> {


    public boolean sortByPrice = false;
    public boolean sortByDistance = false;
    @Override
    public int compare(ListShopBean lhs, ListShopBean rhs) {
        int result = 0;
        if (lhs != null && rhs != null){
            if (sortByPrice){
                String price1 = lhs.get_quote();
                String price2 = rhs.get_quote();
                if (StringUtils.isEmpty(price1) || !price1.contains("km")){
                    return -1;
                }
                if (StringUtils.isEmpty(price2) || !price2.contains("km")){
                    return 1;
                }
                if (price1.length() > price2.length()){
                    return 1;
                }else if (price1.length() < price2.length()){
                    return -1;
                }else {
                    result = price1.compareTo(price2);
                }
            }else if (sortByDistance){
                String distance1 = lhs.get_distance();
                String distance2 = rhs.get_distance();
                result = distance1.compareTo(distance2);
            }
        }
        return result;
    }

    public void setComparebyPrice(){
        this.sortByPrice = true;
        this.sortByDistance = false;
    }

    public void setCompareByDistance(){
        this.sortByDistance = true;
        this.sortByPrice = false;
    }
}
