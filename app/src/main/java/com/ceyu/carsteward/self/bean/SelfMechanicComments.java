package com.ceyu.carsteward.self.bean;

import com.ceyu.carsteward.common.tools.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chen on 15/9/17.
 */
public class SelfMechanicComments {
    private List<MechanicCommentDetail> details;
    private String _sum;

    public List<MechanicCommentDetail> getDetails() {
        return details;
    }

    public void setDetails(List<MechanicCommentDetail> details) {
        this.details = details;
    }

    public String get_sum() {
        return _sum;
    }

    public void set_sum(String _sum) {
        this._sum = _sum;
    }

    public static SelfMechanicComments fromString(String resource) {
        if (StringUtils.isEmpty(resource)) {
            return null;
        }
        try {
            JSONObject object = new JSONObject(resource);
            SelfMechanicComments bean = new SelfMechanicComments();
            String index = "list";
            if (object.has(index)) {
                String res = object.optString(index);
                List<MechanicCommentDetail> details = MechanicCommentDetail.fromString(res);
                if (details != null){
                    bean.setDetails(details);
                }
            }
            index = "sum";
            if (object.has(index)){
                bean.set_sum(object.optString(index));
            }
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
