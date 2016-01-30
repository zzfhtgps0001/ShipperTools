package com.zzfht.shippertools.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    /**
     * 将json 数组转换为Map 对象
     * @param jsonString default value type
     * @return map
     */
    public static Map<String, String> getMap(String jsonString)
    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString); @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, String> valueMap = new HashMap<>();
            while (keyIter.hasNext())
            {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value.toString());
            }
            return valueMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把json 转换为ArrayList 形式
     * @return list
     */
    public static List<Map<String, String>> getList(String jsonString)
    {

        List<Map<String, String>> list = null;
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getList(JSONArray json){
        List<String> list = new ArrayList<>();
        for (int i = 0 ;i < json.length() ; i++ ){
            try {
                list.add((String) json.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
