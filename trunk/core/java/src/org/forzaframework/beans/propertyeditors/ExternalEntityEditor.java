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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;

import java.beans.PropertyEditorSupport;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 9:18:03
 */
public class ExternalEntityEditor <T extends BaseEntity> extends PropertyEditorSupport {

    private static final Log logger = LogFactory.getLog(ExternalEntityEditor.class);
    private Class requiredType;
    private EntityManager entityManager;
    private String externalSystem;

    public ExternalEntityEditor(Class requiredType, String externalSystem) {
        this.externalSystem = externalSystem;
        this.requiredType = requiredType;
    }

    public ExternalEntityEditor(Class requiredType, String externalSystem, EntityManager entityManager) {
        this.externalSystem = externalSystem;
        this.requiredType = requiredType;
        this.entityManager = entityManager;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try{
                T command;
                //TODO: Cambiar, hacer otra clase.
                if(StringUtils.hasText(externalSystem)){
                    command = (T) entityManager.getByCode(requiredType, externalSystem, text);
                }
                else{
                    command = (T) entityManager.getByCode(requiredType, text);
                }
                setValue(command);
            }catch(Exception e){
                logger.info(e.getMessage());
                throw new IllegalArgumentException("Object of type [" + requiredType.getSimpleName() + "] not found on External System [" + externalSystem + "] with code:" + text);
            }
		}
		else {
			setValue(null);
		}
	}

	public String getAsText() {
        Object value = getValue();
        T command = (T) value;
        if (command == null) {
			return "";
		}
		return command.getKey().toString();
	}
}
