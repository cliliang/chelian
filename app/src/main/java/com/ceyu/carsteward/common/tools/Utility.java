package com.ceyu.carsteward.common.tools;

/**
 * Created by Administrator on 2015/6/25.
 */

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ceyu. carsteward.app.Config.isDevelopeMode;

public final class Utility {

    public static final class LogUtils{
        public static void i(String msg){
            if(isDevelopeMode()) i("test",msg);
        }
        public static void i(String tag, String msg){
            if(isDevelopeMode()) Log.i(tag, msg);
        }
        public static void w(String msg){
            if(isDevelopeMode()) w("test", msg);
        }
        public static void w(String tag, String msg){
            if(isDevelopeMode()) Log.w(tag, msg);
        }
        public static void e(String msg){
            if(isDevelopeMode()) e("test", msg);
        }
        public static void e(String tag, String msg){
            if(isDevelopeMode()) Log.e(tag, msg);
        }
        public static void ex(Throwable tr){
            if(isDevelopeMode() && tr!=null) tr.printStackTrace();
        }
    }

    public static boolean errorCodeOk(String jsonString){
        boolean result = true;
        try {
            jsonString = jsonString.substring(
                    jsonString.indexOf("{"),
                    jsonString.indexOf("}") + 1
            );
            result = errorCodeOk(new JSONObject(jsonString));
        }catch (Exception e){
            LogUtils.ex(e);
        }
        return result;
    }

    public static boolean errorCodeOk(JSONObject jsonObject){
        boolean result = true;
        try {
            if (jsonObject.has(ResponseCode.ResponseState)) {
                result = ResponseCode.ResponseOK.equals(
                        jsonObject.getString(ResponseCode.ResponseState)
                );

            }
        }catch(JSONException e){
            LogUtils.ex(e);
        }
        return result;
    }

    public static void errorCodeToaster(JSONObject jsonObject, Context context){
        try {
            if (jsonObject.has(ResponseCode.ResponseState)) {
               String state = jsonObject.getString(ResponseCode.ResponseState);
               String toastContent = ErrorCode.getInstance(context).getErrorCode(state);
               UIHelper.ToastMessage(context, toastContent);
            }
        }catch(JSONException e){
            LogUtils.ex(e);
            UIHelper.ToastMessage(context, context.getString(R.string.network_not_connected));
        }
    }

    public static class ParamsBuilder{

        Map<String, String> params;

        public ParamsBuilder(){
            params = new HashMap<>();
        }

        public ParamsBuilder(AppContext appContext){
            this();
            params.put("token", appContext.getActiveUser().getToken());
        }

        public ParamsBuilder set(String key, String value){
            params.put(key, value);
            return this;
        }

        public ParamsBuilder set(String key, int value){
            params.put(key, String.valueOf(value));
            return this;
        }

        public Map<String, String> build(){
            return this.params;
        }
    }

    //分页加载，判断是否还有下一页
    public static boolean hasMore(String jsonString){
        boolean result = true;
        try{
            result = hasMore(new JSONObject(jsonString));
        }catch (Exception e){
            LogUtils.ex(e);
        }
        return result;
    }
    public static boolean hasMore(JSONObject jsonObject){
        boolean result = true;
        try{
            if(jsonObject.has("more")){
                int more = jsonObject.getInt("more");
                result = more>0;
            }
        }catch (Exception e){
            LogUtils.ex(e);
        }
        return result;
    }
}