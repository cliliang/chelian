package com.ceyu.carsteward.tuan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/7/21.
 */
public class TuanContentBean implements Parcelable {
    private String _title;
    private TuanModBean _store;
    private String _txt;
    private String _pic;
    private String _end_order;
    private String _end_service;
    private String _item;
    private String _notice;
    private String _money;
    private String _market;
    private int _num;

    public TuanContentBean() {
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public TuanModBean get_store() {
        return _store;
    }

    public void set_store(TuanModBean _store) {
        this._store = _store;
    }

    public String get_txt() {
        return _txt;
    }

    public void set_txt(String _txt) {
        this._txt = _txt;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public String get_end_order() {
        return _end_order;
    }

    public void set_end_order(String _end_order) {
        this._end_order = _end_order;
    }

    public String get_end_service() {
        return _end_service;
    }

    public void set_end_service(String _end_service) {
        this._end_service = _end_service;
    }

    public String get_item() {
        return _item;
    }

    public void set_item(String _item) {
        this._item = _item;
    }

    public String get_notice() {
        return _notice;
    }

    public void set_notice(String _notice) {
        this._notice = _notice;
    }

    public String get_money() {
        return _money;
    }

    public void set_money(String _money) {
        this._money = _money;
    }

    public String get_market() {
        return _market;
    }

    public void set_market(String _market) {
        this._market = _market;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public static TuanContentBean fromJson(JSONObject object){
        if (object == null){
            return null;
        }
        TuanContentBean bean = new TuanContentBean();
        String index = "title";
        if (object.has(index)){
            bean.set_title(object.optString(index));
        }
        index = "store";
        if (object.has(index)){
            String res = object.optString(index);
            TuanModBean modBean = TuanModBean.fromString(res);
            if (modBean != null){
                bean.set_store(modBean);
            }
        }
        index = "txt";
        if (object.has(index)){
            bean.set_txt(object.optString(index));
        }
        index = "pic";
        if (object.has(index)){
            bean.set_pic(object.optString(index));
        }
        index = "end_order";
        if (object.has(index)){
            bean.set_end_order(object.optString(index));
        }
        index = "end_service";
        if (object.has(index)){
            bean.set_end_service(object.optString(index));
        }
        index = "item";
        if (object.has(index)){
            bean.set_item(StringUtils.formatRes(object.optString(index)));
        }
        index = "notice";
        if (object.has(index)){
            bean.set_notice(StringUtils.formatRes(object.optString(index)));
        }
        index = "money";
        if (object.has(index)){
            bean.set_money(object.optString(index));
        }
        index = "market";
        if (object.has(index)){
            bean.set_market(object.optString(index));
        }
        index = "num";
        if (object.has(index)){
            bean.set_num(object.optInt(index));
        }
        return bean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TuanContentBean> CREATOR = new Parcelable.Creator<TuanContentBean>(){

        @Override
        public TuanContentBean createFromParcel(Parcel source) {
            return new TuanContentBean(source);
        }

        @Override
        public TuanContentBean[] newArray(int size) {
            return new TuanContentBean[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_title);
        dest.writeParcelable(_store, flags);
        dest.writeString(_txt);
        dest.writeString(_pic);
        dest.writeString(_end_order);
        dest.writeString(_end_service);
        dest.writeString(_item);
        dest.writeString(_notice);
        dest.writeString(_money);
        dest.writeString(_market);
        dest.writeInt(_num);
    }

    private TuanContentBean(Parcel source){
        _title = source.readString();
        _store = source.readParcelable(TuanModBean.class.getClassLoader());
        _txt = source.readString();
        _pic = source.readString();
        _end_order = source.readString();
        _end_service = source.readString();
        _item = source.readString();
        _notice = source.readString();
        _money = source.readString();
        _market = source.readString();
        _num = source.readInt();
    }

}
