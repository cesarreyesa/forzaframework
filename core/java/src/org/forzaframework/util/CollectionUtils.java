package org.forzaframework.util;

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
     * @param list Lista de objectos de donde se obtendra la lista
     * @param bean propiedad que debe tener cada elemento (object) de la lista
     * @param valueSearched valor a comparar
     * @return Lista de objectos que son iguales al parametro value
     * @throws java.lang.reflect.InvocationTargetException Exception lanzada por PropertyUtils
     * @throws NoSuchMethodException Exception lanzada por PropertyUtils
     * @throws IllegalAccessException Exception lanzada por PropertyUtils
     */
    public static List subList(List list, String bean, Object valueSearched) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List subList = new ArrayList();
        for (Object object : list){
            Object objectValue = PropertyUtils.getProperty(object, bean);

            if (valueSearched.equals(objectValue)){
                subList.add(object);
            }
        }

        return subList;
    }
}
