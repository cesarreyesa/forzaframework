/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
