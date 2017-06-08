package com.ceyu.carsteward.points.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/10/14.
 */
public class PointGoodsBean implements Parcelable{
    private int _id;
    private String _subject;
    private String _pic;
    private String _content;
    private String _money;
    private int _integral;
    private boolean _state;

    public PointGoodsBean() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public int get_integral() {
        return _integral;
    }

    public void set_integral(int _integral) {
        this._integral = _integral;
    }

    public boolean is_state() {
        return _state;
    }

    public void set_state(boolean _state) {
        this._state = _state;
    }

    public static List<PointGoodsBean> fromString(String res){
        if (StringUtils.isEmpty(res)){
            return null;
        }
        List<PointGoodsBean> beans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(res);
            for (int i = 0; i < array.length(); i++){
                PointGoodsBean bean = new PointGoodsBean();
                JSONObject object = array.getJSONObject(i);
                String index = "id";
                if (object.has(index)){
                    bean.set_id(object.optInt(index));
                }
                index = "subject";
                if (object.has(index)){
                    bean.set_subject(object.optString(index));
                }
                index = "pic";
                if (object.has(index)){
                    bean.set_pic(object.optString(index));
                }
                index = "content";
                if (object.has(index)){
                    bean.set_content(object.optString(index));
                }
                index = "money";
                if (object.has(index)){
                    bean.set_money(object.optString(index));
                }
                index = "integral";
                if (object.has(index)){
                    bean.set_integral(object.optInt(index));
                }
                index = "state";
                if (object.has(index)){
                    bean.set_state(object.optInt(index) == 1);
                }
                beans.add(bean);
            }
            return beans;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PointGoodsBean> CREATOR = new Parcelable.Creator<PointGoodsBean>(){

        @Override
        public PointGoodsBean createFromParcel(Parcel source) {
            return new PointGoodsBean(source);
        }

        @Override
        public PointGoodsBean[] newArray(int size) {
            return new PointGoodsBean[size];
        }
    };

    public PointGoodsBean(Parcel source){
        _content = source.readString();
        _id = source.readInt();
        _integral = source.readInt();
        _money = source.readString();
        _pic = source.readString();
        _subject = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_content);
        dest.writeInt(_id);
        dest.writeInt(_integral);
        dest.writeString(_money);
        dest.writeString(_pic);
        dest.writeString(_subject);
    }
}
