package com.ceyu.carsteward.engineer.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/4.
 */
public class EngineerBean{
    private static final String TOKEN = "token";
    private static final String NAME = "name";
    private static final String CITY = "city";
    private static final String YEAR = "year";
    private static final String LEVEL = "level";
    private static final String MODEL = "model";
    private static final String PIC = "pic";
    private static final String MONEY = "money";
    private static final String RECOMMEND = "recommend";
    private static final String STATE = "callState";
    private static final String COMPANY = "company";
    private static final String EXP = "exp";
    private static final String PHOTO = "photo";
    private static final String COMMENT = "comment";
    private static final String ONLINE = "online";
    private static final String ORDERLONG = "orderLong"; //订单通话时长
    private static final String ORDERSN = "orderSn";
    private static final String FUNCTION = "function";
    private static final String NUM = "num";
    private static final String CHECK = "check";

    private String _token;
    private String _name;
    private String _city;
    private String _year;
    private String _level;
    private String _model;
    private String _pic;
    private float _money;
    private boolean _recommend;
    private int _state;
    private String _company;
    private String _exp;
    private String[] _photo;
    private EngineerComment _comment;
    private boolean _onLine;
    private int _orderLong;
    private String _orderSn;
    private String _function;
    private int _num;
    private boolean _check;

    public EngineerBean() {
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_city() {
        return _city;
    }

    public void set_city(String _city) {
        this._city = _city;
    }

    public String get_year() {
        return _year;
    }

    public void set_year(String _year) {
        this._year = _year;
    }

    public String get_level() {
        return _level;
    }

    public void set_level(String _level) {
        this._level = _level;
    }

    public String get_model() {
        return _model;
    }

    public void set_model(String _model) {
        this._model = _model;
    }

    public String get_pic() {
        return _pic;
    }

    public void set_pic(String _pic) {
        this._pic = _pic;
    }

    public float get_money() {
        return _money;
    }

    public void set_money(float _money) {
        this._money = _money;
    }

    public boolean is_recommend() {
        return _recommend;
    }

    public void set_recommend(boolean _recommend) {
        this._recommend = _recommend;
    }

    public int get_state() {
        return _state;
    }

    public void set_state(int _state) {
        this._state = _state;
    }

    public String get_company() {
        return _company;
    }

    public void set_company(String _company) {
        this._company = _company;
    }

    public String get_exp() {
        return _exp;
    }

    public void set_exp(String _exp) {
        this._exp = _exp;
    }

    public String[] get_photo() {
        return _photo;
    }

    public void set_photo(String[] _photo) {
        this._photo = _photo;
    }

    public EngineerComment get_comment() {
        return _comment;
    }

    public void set_comment(EngineerComment _comment) {
        this._comment = _comment;
    }

    public boolean is_onLine() {
        return _onLine;
    }

    public void set_onLine(boolean _onLine) {
        this._onLine = _onLine;
    }

    public int get_orderLong() {
        return _orderLong;
    }

    public void set_orderLong(int _orderLong) {
        this._orderLong = _orderLong;
    }

    public String get_orderSn() {
        return _orderSn;
    }

    public void set_orderSn(String _orderSn) {
        this._orderSn = _orderSn;
    }

    public String get_function() {
        return _function;
    }

    public void set_function(String _function) {
        this._function = _function;
    }

    public int get_num() {
        return _num;
    }

    public void set_num(int _num) {
        this._num = _num;
    }

    public boolean is_check() {
        return _check;
    }

    public void set_check(boolean _check) {
        this._check = _check;
    }

    public static ArrayList<EngineerBean> fromEngineerArrayString(String resource){
        ArrayList<EngineerBean> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(resource);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                EngineerBean bean = EngineerBean.fromJsonObject(object);
                if (bean != null){
                    list.add(bean);
                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static EngineerBean fromString(String res){
        if (StringUtils.isEmpty(res)){
            return null;
        }
        try {
            JSONObject object = new JSONObject(res);
            return fromJsonObject(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EngineerBean fromJsonObject(JSONObject object){
        if (object != null){
            EngineerBean bean = new EngineerBean();
            if (object.has(TOKEN)){
                bean.set_token(object.optString(TOKEN));
            }
            if (object.has(NAME)){
                bean.set_name(object.optString(NAME));
            }
            if (object.has(CITY)){
                bean.set_city(object.optString(CITY));
            }
            if (object.has(YEAR)){
                bean.set_year(object.optString(YEAR));
            }
            if (object.has(LEVEL)){
                bean.set_level(object.optString(LEVEL));
            }
            if (object.has(MODEL)){
                bean.set_model(object.optString(MODEL));
            }
            if (object.has(PIC)){
                bean.set_pic(object.optString(PIC));
            }
            if (object.has(MONEY)){
                bean.set_money((float) object.optDouble(MONEY));
            }
            if (object.has(RECOMMEND)){
                bean.set_recommend(object.optInt(RECOMMEND) == 1);
            }
            if (object.has(STATE)){
                bean.set_state(object.optInt(STATE));
            }
            if (object.has(COMPANY)){
                bean.set_company(object.optString(COMPANY));
            }
            if (object.has(EXP)){
                bean.set_exp(object.optString(EXP));
            }
            if (object.has(PHOTO)){
                String photoRes = object.optString(PHOTO);
                String[] photos = photoRes.split("\\|");
                bean.set_photo(photos);
            }
            if (object.has(COMMENT)){
                bean.set_comment(EngineerComment.fromString(object.optString(COMMENT)));
            }
            if (object.has(ONLINE)){
                bean.set_onLine(object.optInt(ONLINE) == 1);
            }
            if (object.has(ORDERLONG)){
                bean.set_orderLong(object.optInt(ORDERLONG));
            }
            if (object.has(ORDERSN)){
                bean.set_orderSn(object.optString(ORDERSN));
            }
            if (object.has(FUNCTION)){
                bean.set_function(object.optString(FUNCTION));
            }
            if (object.has(NUM)){
                bean.set_num(object.optInt(NUM));
            }
            if (object.has(CHECK)){
                bean.set_check(object.optInt(CHECK) == 1);
            }
            return bean;
        }
        return null;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(_token);
//        dest.writeString(_name);
//        dest.writeString(_city);
//        dest.writeString(_year);
//        dest.writeString(_level);
//        dest.writeString(_model);
//        dest.writeString(_pic);
//        dest.writeFloat(_money);
//        dest.writeInt(_recommend ? 1 : 0);
//        dest.writeInt(_state);
//    }
//
//    public static final Parcelable.Creator<EngineerBean> CREATOR = new Parcelable.Creator<EngineerBean>()
//    {
//        public EngineerBean createFromParcel(Parcel in)
//        {
//            return new EngineerBean(in);
//        }
//
//        public EngineerBean[] newArray(int size)
//        {
//            return new EngineerBean[size];
//        }
//    };
//
//    private EngineerBean(Parcel in)
//    {
//        _token = in.readString();
//        _name = in.readString();
//        _city = in.readString();
//        _year = in.readString();
//        _level = in.readString();
//        _model = in.readString();
//        _pic = in.readString();
//        _money = in.readFloat();
//        _recommend = in.readInt() == 1;
//        _state = in.readInt();
//    }
}
