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
