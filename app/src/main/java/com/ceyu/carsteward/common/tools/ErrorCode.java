package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.content.res.Resources;

import com.ceyu.carsteward.R;

import java.util.HashMap;

public class ErrorCode {

    private static ErrorCode errorCode;
    private HashMap<String, String> map;
    private ErrorCode(Context context) {
        map = new HashMap<>();
        Resources resources = context.getResources();
        map.put("A0001", resources.getString(R.string.A0001));
        map.put("A0002", resources.getString(R.string.A0002));
        map.put("B0001", resources.getString(R.string.B0001));
        map.put("B0002", resources.getString(R.string.B0002));
        map.put("B0003", resources.getString(R.string.B0003));
        map.put("B0004", resources.getString(R.string.B0004));
        map.put("B0005", resources.getString(R.string.B0005));
        map.put("B0006", resources.getString(R.string.B0006));
        map.put("B0007", resources.getString(R.string.B0007));
        map.put("B0008", resources.getString(R.string.B0008));
        map.put("B0009", resources.getString(R.string.B0009));
        map.put("B0010", resources.getString(R.string.B0010));
        map.put("B0011", resources.getString(R.string.B0011));
        map.put("B0012", resources.getString(R.string.B0012));
        map.put("B0013", resources.getString(R.string.B0013));
        map.put("B0014", resources.getString(R.string.B0014));
        map.put("B0015", resources.getString(R.string.B0015));
        map.put("B0016", resources.getString(R.string.B0016));
        map.put("B0017", resources.getString(R.string.B0017));
        map.put("B0018", resources.getString(R.string.B0018));
        map.put("B0019", resources.getString(R.string.B0019));


    }

    public static ErrorCode getInstance(Context cnt) {
        if (errorCode == null) {
            errorCode = new ErrorCode(cnt);
        }
        return errorCode;
    }

    public String getErrorCode(String code) {
        if (StringUtils.isEmpty(code))
            return "";
        if (map == null)
            return "";
        String result = map.get(code);
        if (StringUtils.isEmpty(result)){
            return code;
        }
        return result;
    }
}
