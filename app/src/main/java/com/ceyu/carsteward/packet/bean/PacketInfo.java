package com.ceyu.carsteward.packet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/8.
 */
public class PacketInfo implements Parcelable{
    private int _id;
    private int _num;
    private String _ticketType;
    private String _ticketDescribe;
    private String _limit;
    private boolean _usable;

    public PacketInfo() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getNum() {
        return _num;
    }

    public void set_num(int num) {
        this._num = num;
    }

    public String get_ticketType() {
        return _ticketType;
    }

    public void set_ticketType(String _ticketType) {
        this._ticketType = _ticketType;
    }

    public String get_ticketDescribe() {
        return _ticketDescribe;
    }

    public void set_ticketDescribe(String _ticketDescribe) {
        this._ticketDescribe = _ticketDescribe;
    }

    public String get_limit() {
        return _limit;
    }

    public void set_limit(String _limit) {
        this._limit = _limit;
    }

    public boolean is_usable() {
        return _usable;
    }

    public void set_usable(boolean _usable) {
        this._usable = _usable;
    }

    public static ArrayList<PacketInfo> fromString(String resource){
        ArrayList<PacketInfo> infos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                PacketInfo info = new PacketInfo();
                String index = "id";
                if (object.has(index)) {
                    info.set_id(object.optInt(index));
                }
                index = "num";
                if (object.has(index)){
                    info.set_num(object.optInt(index));
                }
                index = "class";
                if (object.has(index)){
                    String res = object.optString(index);
                    String[] classArray = res.split(",");
                    if (classArray.length == 2){
                        info.set_ticketType(classArray[0]);
                        info.set_ticketDescribe(classArray[1]);
                    }
                }
                index = "limit";
                if (object.has(index)){
                    info.set_limit(object.optString(index));
                }
                index = "usable";
                if (object.has(index)){
                    info.set_usable(object.optInt(index) == 1);
                }
                infos.add(info);
            }
            return infos;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PacketInfo> CREATOR = new Parcelable.Creator<PacketInfo>(){

        @Override
        public PacketInfo createFromParcel(Parcel source) {
            return new PacketInfo(source);
        }

        @Override
        public PacketInfo[] newArray(int size) {
            return new PacketInfo[size];
        }
    };

    private PacketInfo(Parcel source){
        _id = source.readInt();
        _num = source.readInt();
        _ticketType = source.readString();
        _ticketDescribe = source.readString();
        _limit = source.readString();
        _usable = source.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_num);
        dest.writeString(_ticketType);
        dest.writeString(_ticketDescribe);
        dest.writeString(_limit);
        dest.writeInt(_usable ? 1 : 0);
    }
}
