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

package org.forzaframework.core.persistance;

import org.springframework.context.support.MessageSourceAccessor;
import org.hibernate.Session;

import java.util.List;

/**
 * @author Cesar Reyes
 *         Date: 29-jul-2008
 *         Time: 11:14:21
 */
public interface EntityManager {

    public Session getHibernateSession();

    /**
     * Generic method used to get a all objects of a particular type.
     * @param entityClass the type of objects
     * @return List of populated objects
     */
    <T> List<T> getAll(Class entityClass);

    <T> List<T> getAll(Class clazz, MessageSourceAccessor messageSource);

//    List getAll(Class clazz, String query);

//    List getAll(Class clazz, String field, String criteria);

    <T> List<T> find(String queryString);

    <T> List<T> find(String queryString, Object value);

    <T> List<T> find(String queryString, Object[] values);

    <T> List<T> find(Class entityClass, Criteria criteria);

    /**
     * Obtiene una entidad y valida que exista o sea unica.
     * @param entityClass
     * @param primaryKey
     * @param <T>
     * @return
     */
    <T> T get(Class entityClass, Object primaryKey);

    <T> T get(String entityName, Object primaryKey);

    /**
     * Obtiene una entidad de tipo entityClass, y de acuerdo al criteria pasado.
     * @param entityClass
     * @param criteria
     * @param <T>
     * @return
     */
    <T> T get(Class entityClass, Criteria criteria);

    <T> T get(Class entityClass, Object primaryKey, Boolean requireNewSession);

    <T> T get(Class entityClass, String hql, Object param);

    <T> T get(Class entityClass, String hql, Object[] params);

    <T> T getByCode(Class entityClass, String code);

    <T> T getByCode(Class clazz, String externalSystem, String code);

    /**
     * Generic method to save an object.
     * @param entity
     */
    void save(Object entity);

    void save(String entityName, Object entity);

    /**
     * Generic method to delete an object based on class and id
     * @param entity
     */
    void delete(Object entity);

    void delete(String entityName, Object primaryKey);

    void delete(Class entityClass, Object primaryKey);

    int execute(String query);

    int execute(String query, Object value);

    int execute(String query, Object[] values);

    void execute(String procedureName, String paramName, Object param);

    Integer executeInteger(String hql);

    Integer executeInteger(String hql, Object value);

    Integer executeInteger(String hql, Object[] values);

    Double executeDouble(String hql, Object[] values);
}
