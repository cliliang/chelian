package com.ceyu.carsteward.maintain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainParts implements Parcelable{
    private ArrayList<MaintainSubContent> subContents;

    public MaintainParts() {
    }

    public ArrayList<MaintainSubContent> getSubContents() {
        for (int i = 0; i < subContents.size(); i ++){
            MaintainSubContent subContent = subContents.get(i);
            int select = subContent.get_sel();
            if (!subContent.isSelect()){
                select = Math.abs(select);
            }
            ArrayList<MaintainDetailContent> detailContents = subContent.get_sub();
            if (detailContents != null && detailContents.size() > 0){
                for (int j = 0; j < detailContents.size(); j++){
                    MaintainDetailContent detailContent = detailContents.get(j);
                    if (detailContent.get_id() == select){
                        subContent.set_price(detailContent.get_moneyTxt());
                        subContent.set_content(detailContent.get_info());
                        detailContents.remove(detailContent);
                    }
                }
            }
        }
        return subContents;
    }

    public void setSubContents(ArrayList<MaintainSubContent> subContents) {
        this.subContents = subContents;
    }

    public static MaintainParts fromString(String resource){
        MaintainParts parts = new MaintainParts();
        ArrayList<MaintainSubContent> subContents = MaintainSubContent.fromString(resource);
        if (subContents != null){
            parts.setSubContents(subContents);
        }
        return parts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MaintainParts> CREATOR = new Parcelable.Creator<MaintainParts>(){

        @Override
        public MaintainParts createFromParcel(Parcel source) {
            return new MaintainParts(source);
        }

        @Override
        public MaintainParts[] newArray(int size) {
            return new MaintainParts[size];
        }
    };

    private MaintainParts(Parcel parcel){
        Parcelable[] array = parcel.readParcelableArray(MaintainSubContent.class.getClassLoader());
        subContents = new ArrayList<>();
        for (int i = 0; i < array.length; i++){
            subContents.add((MaintainSubContent) array[i]);
        }
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        MaintainSubContent[] array = new MaintainSubContent[subContents.size()];
        for (int i = 0; i < subContents.size(); i++){
            array[i] = subContents.get(i);
        }
        dest.writeParcelableArray(array, flags);
    }
}
