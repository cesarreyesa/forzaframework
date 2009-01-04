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

package org.forzaframework.bind;

import org.apache.commons.lang.StringUtils;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.Attribute;
import org.forzaframework.util.DateUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:48:55
 */
@SuppressWarnings({"unchecked"})
public class DynamicEntityDataBinder {

    public static final String DEFAULT_OBJECT_NAME = "target";

    private final Map target;

    private final SystemEntity entity;

    public DynamicEntityDataBinder(Map target, SystemEntity entity) {
        this.target = target;
        this.entity = entity;
    }

    public void bind(Map values) {
        Set<Map.Entry> entryset = values.entrySet();
        for(Map.Entry entry : entryset){
            String field = entry.getKey().toString();
            target.put(field, getValidatedValue(field, entry.getValue()));
        }
    }

    public void bind(Set<Map.Entry> entrySet) {
        for(Map.Entry entry : entrySet){
            String field = entry.getKey().toString();
            target.put(field, getValidatedValue(field, entry.getValue()));
        }
    }

    public Object getValidatedValue(String field, Object value){
        if(field.equals("id")){
            if(StringUtils.isBlank(String.valueOf(value))) return null;
            return Long.valueOf(String.valueOf(value));
        }
        Attribute att = entity.findAttribute(field);
        if(att.getType() != null){
            if(att.getType().equals("integer")){
                if(StringUtils.isBlank(String.valueOf(value))) return 0;
                return Integer.valueOf(String.valueOf(value));
            }else if(att.getType().equals("double")){
                if(StringUtils.isBlank(String.valueOf(value))) return 0.0;
                return Double.valueOf(String.valueOf(value));
            }else if(att.getType().equals("date")){
                return DateUtils.getDate(String.valueOf(value));
            }else{
                return value.toString();
            }
        }else{
            return value;
        }
    }

}
