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

package org.forzaframework.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.forzaframework.core.persistance.EntityManager;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:22:35
 */
public class CustomEntityIdCollectionEditor extends CustomCollectionEditor {

    private Class itemType;
    private EntityManager entityManager;

    public CustomEntityIdCollectionEditor(Class collectionType, Class itemType, EntityManager entityManager) {
        super(collectionType);
        this.entityManager = entityManager;
        this.itemType = itemType;
    }

    public CustomEntityIdCollectionEditor(Class collectionType, boolean nullAsEmptyCollection, Class itemType, EntityManager entityManager) {
        super(collectionType, nullAsEmptyCollection);
        this.entityManager = entityManager;
        this.itemType = itemType;
    }

    protected Object convertElement(Object element) {
        if(element != null){
            return  entityManager.get(itemType, Long.valueOf(element.toString()));
        }
        else{
            return null;
        }
    }

}
