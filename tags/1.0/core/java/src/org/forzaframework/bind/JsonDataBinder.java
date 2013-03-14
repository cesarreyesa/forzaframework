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

package org.forzaframework.bind;

import org.springframework.validation.DataBinder;
import org.springframework.beans.MutablePropertyValues;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * @author cesarreyes
 *         Date: 19-ago-2008
 *         Time: 13:12:55
 */
public class JsonDataBinder extends DataBinder {

    public JsonDataBinder(Object target) {
        super(target);
    }

    public JsonDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    public void bind(JSONObject json) {
        MutablePropertyValues mpvs = convertToPropertyValues(json);
        doBind(mpvs);
    }

    public void bind(String json) {
        MutablePropertyValues mpvs = convertToPropertyValues(json);
        doBind(mpvs);
    }

    public static MutablePropertyValues convertToPropertyValues(String json){
        return convertToPropertyValues(JSONObject.fromObject(json));
    }

    public static MutablePropertyValues convertToPropertyValues(JSONObject json){
        MutablePropertyValues mpvs = new MutablePropertyValues();
        for(Iterator i = json.keys(); i.hasNext(); ){
            String key = (String) i.next();
            mpvs.addPropertyValue(key, json.get(key));
        }
        return mpvs;
    }
}
