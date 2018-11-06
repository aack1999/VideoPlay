package com.framework.core.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ParseUtils {

    public static  <T> T parseJson(String json,Class<T> c){
        Gson gson=new Gson();
        T t;
        try {
           t=gson.fromJson(json,c);
        }catch (JsonSyntaxException e){
            return null;
        }
        return t;
    }

    public static <T> T parseJson(String json, Type t){
        return new Gson().fromJson(json,t);
    }

    public static String toJson(Object o){
        Gson gson=new Gson();
        return gson.toJson(o);
    }

    public static Map toMap(Object obj){
        Map<String,String> map=new HashMap<>();
        if (obj != null) {
            String paramestr = ParseUtils.toJson(obj);
            LogUtils.e(paramestr);
            map=jsonToMap(paramestr);
        }
        return map;
    }

    public static Map<String, String> jsonToMap(String jsonStr){
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyIter= jsonObject.keys();
            String key;
            String value ;
            Map<String, String> valueMap = new HashMap<>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = (String) jsonObject.get(key);
                LogUtils.e(key+"="+value);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

}
