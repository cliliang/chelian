package com.ceyu.carsteward.engineer.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 15/6/5.
 */
public class CommentGood {
    private int _veryGood;
    private int _good;
    private int _notGood;

    public CommentGood() {
    }

    public int get_veryGood() {
        return _veryGood;
    }

    public void set_veryGood(int _veryGood) {
        this._veryGood = _veryGood;
    }

    public int get_good() {
        return _good;
    }

    public void set_good(int _good) {
        this._good = _good;
    }

    public int get_notGood() {
        return _notGood;
    }

    public void set_notGood(int _notGood) {
        this._notGood = _notGood;
    }

    static CommentGood fromFrom(String resource){
        CommentGood commentGood = new CommentGood();
        try {
            JSONObject object = new JSONObject(resource);
            String item = "verygood";
            if (object.has(item)){
                commentGood.set_veryGood(object.optInt(item));
            }
            item = "good";
            if (object.has(item)){
                commentGood.set_good(object.optInt(item));
            }
            item = "notgood";
            if (object.has(item)){
                commentGood.set_notGood(object.optInt(item));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commentGood;
    }
}
