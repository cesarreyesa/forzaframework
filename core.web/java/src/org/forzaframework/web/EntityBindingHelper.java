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

package org.forzaframework.web;

import org.springframework.validation.DataBinder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.apache.commons.lang.ClassUtils;
import org.forzaframework.metadata.SystemEntity;
import org.forzaframework.metadata.Attribute;
import org.forzaframework.metadata.SystemConfiguration;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.beans.propertyeditors.CustomClassEditor;
import org.forzaframework.beans.propertyeditors.CustomEntityIdCollectionEditor;

import javax.persistence.Embeddable;
import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:18:38
 */
public class EntityBindingHelper {

    public static void configureBinder(EntityManager em, DataBinder binder) {

    }

    public static void configureBinder(EntityManager em, SystemConfiguration configuration, DataBinder binder) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(binder.getTarget().getClass());
        for(PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()){
            //Primero busca si es un BaseEntity para registrar un CustomClassEditor
            if(ClassUtils.isAssignable(pd.getPropertyType(), BaseEntity.class)){
                binder.registerCustomEditor(pd.getPropertyType(), pd.getName(), new CustomClassEditor(pd.getPropertyType(), em));

            }else if(ClassUtils.isAssignable(pd.getPropertyType(), List.class)){
                // Si es una Lista entonces busca el SystemEntity
                SystemEntity entity = configuration.getSystemEntity(binder.getTarget().getClass());
                if(entity != null){
                    // Y encuentra la definicion del atributo
                    Attribute attribute = entity.findAttribute(pd.getName());
                    if(attribute != null){
                        SystemEntity listType = configuration.getSystemEntity(attribute.getEntity());
                        binder.registerCustomEditor(List.class, pd.getName(), new CustomEntityIdCollectionEditor(List.class, listType.getType(), em));
                    }
                }

            }else if(pd.getPropertyType().isAnnotationPresent(Embeddable.class)){
                // Si la propiedad es un Componente entonces busca los attributos de este, para declarar los customClassEditor, TODO: aqui deberia haber recursividad.
                BeanWrapper innerWrapper = new BeanWrapperImpl(pd.getPropertyType());
                for(PropertyDescriptor innerPd : innerWrapper.getPropertyDescriptors()){
                    if(ClassUtils.isAssignable(innerPd.getPropertyType(), BaseEntity.class)){
                        binder.registerCustomEditor(innerPd.getPropertyType(), pd.getName() + "." + innerPd.getName(), new CustomClassEditor(innerPd.getPropertyType(), em));
                    }
                }
            }
        }
    }

}
