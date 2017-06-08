package com.ceyu.carsteward.packet.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/8.
 */
public class MyPackets {
    private ArrayList<PacketInfo> packetInfos;
    private boolean haveMorePage;
    private float sum;
    public MyPackets() {
    }

    public ArrayList<PacketInfo> getPacketInfos() {
        return packetInfos;
    }

    public void setPacketInfos(ArrayList<PacketInfo> packetInfos) {
        this.packetInfos = packetInfos;
    }

    public boolean isHaveMorePage() {
        return haveMorePage;
    }

    public void setHaveMorePage(boolean haveMorePage) {
        this.haveMorePage = haveMorePage;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public static MyPackets fromJsonObject(JSONObject object){
        MyPackets packets = new MyPackets();
        String index = "list";
        if (object.has(index)){
            String infos = object.optString(index);
            packets.setPacketInfos(PacketInfo.fromString(infos));
        }
        index = "more";
        if (object.has(index)){
            packets.setHaveMorePage(object.optInt(index) == 1);
        }
        index = "sum";
        if (object.has(index)){
            packets.setSum((float) object.optDouble(index));
        }
        return packets;
    }
}
