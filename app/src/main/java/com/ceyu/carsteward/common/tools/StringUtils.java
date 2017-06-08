package com.ceyu.carsteward.common.tools;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chen on 15/6/1.
 */
public class StringUtils {
    private StringUtils() {

    }
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String MD5(String str) {
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        if(TextUtils.isEmpty(str)){
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = messagedigest.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static String formatArray(String res){
        if (!StringUtils.isEmpty(res)){
            String[] array = res.replace("[", "").replace("]", "").replace("\"", "").split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length; i++){
                builder.append(array[i]);
                builder.append("\n");
            }
            String result = builder.toString();
            if (!StringUtils.isEmpty(result) && result.length() > 1){
                return result.substring(0, result.length() - 1);
            }
        }
        return "";
    }
    //["title1", "title2"] => title1 \n title
    public static String formatRes(String res){
        if (StringUtils.isEmpty(res)){
            return "";
        }
        try {
            JSONArray array = new JSONArray(res);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length(); i++){
                String item = array.getString(i);
                builder.append(item);
                builder.append("\n");
            }
            String result = builder.toString();
            if (result.length() > 1){
                return result.substring(0, result.length() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
