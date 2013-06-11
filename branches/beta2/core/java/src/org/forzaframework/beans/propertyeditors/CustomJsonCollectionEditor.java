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

package org.forzaframework.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.BeanUtils;
import org.forzaframework.bind.JsonDataBinder;
import net.sf.json.JSONObject;

/**
 * @author cesarreyes
 * Date: 09-sep-2008
 * Time: 15:15:54
 */
public class CustomJsonCollectionEditor extends CustomCollectionEditor {

    private Class itemType;

    public CustomJsonCollectionEditor(Class collectionType, Class itemType) {
        super(collectionType);
        this.itemType = itemType;
    }

    public CustomJsonCollectionEditor(Class collectionType, boolean nullAsEmptyCollection, Class itemType) {
        super(collectionType, nullAsEmptyCollection);
        this.itemType = itemType;
    }

    protected Object convertElement(Object element) {
        if(element != null){
            JSONObject jsonObject = JSONObject.fromObject(element);
            Object bean = BeanUtils.instantiateClass(itemType);

            JsonDataBinder binder = new JsonDataBinder(bean);
            binder.bind(jsonObject);

            return bean;
        }
        else{
            return null;
        }
    }
}
