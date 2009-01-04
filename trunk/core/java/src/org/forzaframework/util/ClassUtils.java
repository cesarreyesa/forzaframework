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

package org.forzaframework.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.beans.PropertyDescriptor;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:27:54
 */
public abstract class ClassUtils extends org.apache.commons.lang.ClassUtils {

    public static boolean hasAnnotation(Class clazz, Class toAnnotation){
        for(Annotation annotation : clazz.getAnnotations()){
            if(annotation.annotationType().equals(toAnnotation)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasSuperclass(Class clazz, Class toClass){
        List superclasses = getAllSuperclasses(clazz);
        for (Object superclass : superclasses) {
            if (superclass.equals(toClass)) {
                return true;
            }
        }
        return false;
    }

    public static Class getPropertyClass(Class clazz, String property){
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if(propertyDescriptor.getName().equals(property)){
                return propertyDescriptor.getPropertyType();
            }
        }
        return null;
    }
}
