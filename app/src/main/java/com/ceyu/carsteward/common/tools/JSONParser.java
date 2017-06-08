package com.ceyu.carsteward.common.tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 * json解析工具
 * 只能解析{"string":"string"}类型的键值对
 */
public class JSONParser {

    public static <T>T parse(JSONObject jsonObject, Class<T> clazz) throws Exception{

        Constructor constructor = clazz.getDeclaredConstructor();
        boolean accessible = constructor.isAccessible();
        constructor.setAccessible(true);
        T t = (T)constructor.newInstance();
        constructor.setAccessible(accessible);
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            try{
                String key = field.getName();
                if(jsonObject.has(key)){
                    boolean accessible2 = field.isAccessible();
                    field.setAccessible(true);
                    field.set(t, jsonObject.get(key));
                    field.setAccessible(accessible2);
                }
            }catch (Exception e){
                Utility.LogUtils.ex(e);
            }
        }

        return t;
   }

    public static <T>List<T> parse(JSONArray jsonArray, Class<T> clazz) throws Exception{

        if(jsonArray == null) return null;

        List<T> list = new LinkedList<T>();

       for(int i=0; i<jsonArray.length(); i++){
           try {
               JSONObject jsonObject = jsonArray.getJSONObject(i);
               list.add(parse(jsonObject, clazz));
           }catch (Exception e){
               Utility.LogUtils.ex(e);
           }
       }
        return new ArrayList<T>(list);
    }

}
