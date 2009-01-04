package org.forzaframework.util;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author cesarreyes
 *         Date: 30-nov-2008
 *         Time: 11:26:23
 */
public class MapUtils {

    public static <T> List<Map.Entry<String, T>> getEntries(Map<String, T> map, String str){
        List<Map.Entry<String, T>> entries = new ArrayList<Map.Entry<String, T>>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String s = str.substring(0, str.length() - 1);
            if(str.endsWith("*") && entry.getKey().startsWith(s)){
                entries.add(entry);
            }
        }
        return entries;
    }

    public static <T> Map<String, T> filterMap(Map<String, T> map, String str){
        Map<String, T> filteredMap = new HashMap<String, T>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String s = str.substring(0, str.length() - 1);
            if(str.endsWith("*") && entry.getKey().startsWith(s)){
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }
}
