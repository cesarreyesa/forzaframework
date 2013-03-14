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

package org.forzaframework.util;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.forzaframework.query.PageInfo;

import java.util.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 15:29:37
 */
public class CollectionUtils {

    private static Log logger = LogFactory.getLog(CollectionUtils.class);

    public static <T> List<T> convertSetToList(Set<T> collection){
        List<T> list = new ArrayList<T>();
        for(T item : collection){
            list.add(item);
        }
        return list;
    }

    public static <T> List<T> paginate(List<T> list, Map model){
    	return paginate(list, model, true);
    }

    public static <T> List<T> paginate(List<T> list, Map model, Boolean sort){
        PageInfo pageInfo = new PageInfo();
        try {
            org.apache.commons.beanutils.BeanUtils.populate(pageInfo, model);
        } catch (Exception e) {
            logger.warn("Error populating object page info: " + e.getMessage());
        }
        return paginate(list, pageInfo, sort);
    }

    public static <T> List<T> paginate(List<T> list, PageInfo pageInfo) {
    	return paginate(list, pageInfo, true);
    }

    public static <T> List<T> paginate(List<T> list, PageInfo pageInfo, Boolean sort) {
        if (sort && StringUtils.isNotBlank(pageInfo.getSort())) {
            Collections.sort(list, new BeanComparator(pageInfo.getSort(), pageInfo.getDir()));
        }
        List<T> paginatedList;
        if (pageInfo.getLimit() != null) {
            int start = pageInfo.getStart();

            int end;
            if (start + pageInfo.getLimit() > list.size())
                end = list.size();
            else
                end = start + pageInfo.getLimit();

            paginatedList = list.subList(start, end);
        } else {
            paginatedList = list;
        }
        return paginatedList;
    }

    public static <T> List<T> sort(List<T> list, Map model){
        PageInfo pageInfo = new PageInfo();
        try {
            org.apache.commons.beanutils.BeanUtils.populate(pageInfo, model);

            if (StringUtils.isNotBlank(pageInfo.getSort())) {
                Collections.sort(list, new BeanComparator(pageInfo.getSort(), pageInfo.getDir()));
            }
        } catch (Exception e) {
            logger.warn("Error populating object page info: " + e.getMessage());
        }

        return list;
    }

    public static String join(List items){
        return join(items, ",");
    }

    public static String join(List items, String separator){
        StringBuilder joinList = new StringBuilder();
        for(Object item : items){
            joinList.append(item.toString()).append(separator);
        }
        return joinList.substring(0, joinList.length() - 1);
    }

    public static String join(Object[] items){
        return join(items, ",");
    }

    public static String join(Object[] items, String separator){
        StringBuilder joinList = new StringBuilder();
        for(Object item : items){
            joinList.append(item.toString()).append(separator);
        }
        return joinList.substring(0, joinList.length() - 1);
    }

    /**
     * Compara la propiedad especificada en "bean" de cada elemento de la lista con el valor "valueSearched" y devuele
     * una lista de los elmentos iguales a "valueSearched"
     * Regresa un list de objects en donde la propiedad "bean" es igual a "value"
     *
     * @param list              Lista de objects de donde se obtendra la lista
     * @param propertyName      Propiedad que debe tener cada elemento de la lista
     * @param valueSearched     Valor a comparar
     * 
     * @return Lista de objectos que son iguales al parametro value
     * @throws Exception lanzada por PropertyUtils
     */
    public static List subList(List list, String propertyName, Object valueSearched) throws Exception {
        List subList = new ArrayList();
        for (Object object : list){
            try {
                //Obtenemos el valor del tributo especificado en el parametro bean
                Object objectValue = PropertyUtils.getProperty(object, propertyName);
                //Si es igual lo agregamos a la lista
                if (objectValue.equals(valueSearched)){
                    subList.add(object);
                }
            } catch (org.apache.commons.beanutils.NestedNullException e) {
                //Si el valor del atriburo es nulo y si el valor buscado tambien es nulo agregamos el objeto a la lista
                    if (valueSearched == null){
                        subList.add(object);
                    }
                //No hacemos nada
            }

        }

        return subList;
    }

    public static List subList(List list, BeanPropertyValueEqualsPredicate[] predicates) {
        List subList = new ArrayList(list);
        org.apache.commons.collections.CollectionUtils.filter(subList, PredicateUtils.allPredicate(predicates));

        return subList;
    }

}
