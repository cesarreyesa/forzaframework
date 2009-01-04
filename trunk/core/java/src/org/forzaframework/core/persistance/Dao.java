package org.forzaframework.core.persistance;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:18:00
 */
public interface Dao {

    <T> List<T> getAll(Class clazz);

    List getAll(Class clazz, String query);

    List getAll(Class clazz, String field, String criteria);

    <T> T get(Class clazz, Object primaryKey);

    <T> T get(Class clazz, Criteria criteria);

    <T> T get(String entityName, Object primaryKey);

    <T> T get(Class clazz, Object primaryKey, Boolean requireNewSession);

    <T> T getByCode(Class clazz, final String code);

    <T> T getByCode(String clazz, String code);

    void save(Object object);

    void save(String entityName, Object entity);

    void delete(Object entity);

    void delete(String entityName, Object entity);

//    <T> T retriveByCode(Class clazz, String externalSystem, String code);

    List find(String hql);

    <T> List<T> find(Class entityClass, Criteria criteria);

    List find(String hql, Object param);

    List find(String hql, Object[] params);

    void execute(String procedureName, String paramName, Object param);
}
