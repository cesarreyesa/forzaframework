/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.test.utils;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.forzaframework.util.MapUtils;

/**
 * @author cesarreyes
 *         Date: 30-nov-2008
 *         Time: 12:34:09
 */
public class MapUtilsTests extends TestCase {

    public void testGetEntries(){
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("phones.1.nada", "1");
        testMap.put("phones.1.test", "2");
        testMap.put("phones.1.test2", "3");
        testMap.put("phone.3.test2", "1");

        List<Map.Entry<String,String>> entries = MapUtils.getEntries(testMap, "phones.*");
        assertEquals(3, entries.size());
    }

    public void testFilterMap(){
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("phones.1.nada", "1");
        testMap.put("phones.1.test", "2");
        testMap.put("phones.1.test2", "3");
        testMap.put("phone.3.test2", "1");

        Map<String,String> filteredMap = MapUtils.filterMap(testMap, "phones.*");
        assertEquals(3, filteredMap.entrySet().size());
        assertEquals("1", filteredMap.get("phones.1.nada"));
        assertEquals("2", filteredMap.get("phones.1.test"));
        assertEquals("3", filteredMap.get("phones.1.test2"));
    }
}
