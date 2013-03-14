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
import org.forzaframework.core.persistance.Restrictions;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.ObjectRetrievalFailureException;

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
    private Class pkType = Long.class;
    private EntityManager entityManager;
    private String property = "id";

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

    public CustomClassEditor(String property, Class pkType, Class requiredType, EntityManager entityManager) {
        this.requiredType = requiredType;
        this.pkType = pkType;
        this.entityManager = entityManager;
        this.property = property;
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
            Object propertyValue = null;

            if(pkType.equals(Long.class)) {
                propertyValue = Long.valueOf(text);
            } else {
                propertyValue = text;
            }

            if(entityManager == null){
                try {
                    PropertyUtils.setSimpleProperty(command, property, propertyValue);
                } catch (Exception e) {

                }
            }
            else {
                command = entityManager.getByProperty(requiredType, property, propertyValue);
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
            key = PropertyUtils.getSimpleProperty(command, property);
        } catch (Exception e) {
            key = null;

        }
        if (command == null || key == null) {
			return "";
		}
		return key.toString();
	}

}
