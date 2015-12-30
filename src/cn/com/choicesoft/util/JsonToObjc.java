package cn.com.choicesoft.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarException;

/**
 * Created by chensen on 15/9/14.
 */
public class JsonToObjc {
    public Map<String,Object> JsonToMap(String result) throws JSONException{
        JSONObject jsonObject=new JSONObject(result);
        Map<String,Object>map= new HashMap<String, Object>();
//        for (int i=0;i<jsonObject.length();i++){
//
//            map.put(jsonObject.keys())
//        }


        return map;
    }
}
