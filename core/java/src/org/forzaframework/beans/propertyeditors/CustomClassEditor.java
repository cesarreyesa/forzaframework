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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:20:02
 */
public class CustomClassEditor extends PropertyEditorSupport {

    private Class requiredType;
    private Class pkType;
    private EntityManager entityManager;

    /**
	 * Create a default ClassEditor, using the thread context ClassLoader.
	 */
	public CustomClassEditor(Class requiredType) {
        this.requiredType = requiredType;
    }

    public CustomClassEditor(Class requiredType, Class pkType) {
        this.requiredType = requiredType;
        this.pkType = pkType;
    }

    public CustomClassEditor(Class requiredType, EntityManager entityManager) {
        this.requiredType = requiredType;
        this.entityManager = entityManager;
    }

    public CustomClassEditor(Class requiredType, Class pkType, EntityManager entityManager) {
        this.requiredType = requiredType;
        this.pkType = pkType;
        this.entityManager = entityManager;
    }

    public void setManager(EntityManager em) {
        this.entityManager = em;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        Object command;
        try {
            command = requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return ;
        }

        if (StringUtils.isNotBlank(text)) {
            Serializable id = null;

            if (NumberUtils.isNumber(text)) {
                if(pkType == null || !pkType.equals(String.class)){
                    id = Long.valueOf(text);
                }
                else {
                    id = text;
                }
            }

            if(entityManager == null){
                try {
                    PropertyUtils.setSimpleProperty(command, "id", id);
                } catch (Exception e) {

                }
            }
            else{
                if (id != null) {
                    command = entityManager.get(requiredType, id);
                } else {
                    command = entityManager.getByCode(requiredType, text);
                }
            }
			setValue(command);
		}
		else {
			setValue(null);
		}
	}

	public String getAsText() {
        Object command;
        try {
            command = requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return "";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }

        Object value = getValue();
        command = value;
        Object key;
        try {
            key = PropertyUtils.getSimpleProperty(command, "id");
        } catch (Exception e) {
            key = null;

        }
        if (command == null || key == null) {
			return "";
		}
		return key.toString();
	}

}
