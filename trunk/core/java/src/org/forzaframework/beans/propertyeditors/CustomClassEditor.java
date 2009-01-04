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

import org.springframework.util.StringUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:20:02
 */
public class CustomClassEditor<T extends BaseEntity> extends PropertyEditorSupport {

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
        T command;
        try {
            command = (T) requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return ;
        }

        if (StringUtils.hasText(text)) {
            Serializable id;

            if(pkType == null || !pkType.equals(String.class)){
                id = Long.valueOf(text);
            }
            else {
                id = text;
            }

            if(entityManager == null){
                command.setKey(id);
            }
            else{
                command = (T) entityManager.get(requiredType, id);
            }
			setValue(command);
		}
		else {
			setValue(null);
		}
	}

	public String getAsText() {
        T command;
        try {
            command = (T) requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return "";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }

        Object value = getValue();
        command = (T) value;
		if (command == null || command.getKey() == null) {
			return "";
		}
		return command.getKey().toString();
	}

}
