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

package org.forzaframework.core.persistance.hibernate;

import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.core.persistance.Criterion;
import org.forzaframework.util.CollectionUtils;
import org.forzaframework.util.ClassUtils;
import org.forzaframework.metadata.Catalog;
import org.forzaframework.metadata.TranslatableCatalog;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.WordUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 12:50:53
 */
@SuppressWarnings({"unchecked"})
public class EntityManagerImpl extends HibernateDaoSupport implements EntityManager {

    protected final Log logger = LogFactory.getLog(getClass());

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Session getHibernateSession(){
        return this.getSession();
    }

    public <T> List<T> getAll(Class entityClass) {
        return getHibernateTemplate().loadAll(entityClass);
    }

    public <T> List<T> getAll(Class entityClass, MessageSourceAccessor messageSource) {
        List<T> list = getAll(entityClass);
        for(T t : list){
        	Catalog catalog = (Catalog) t;
        	String className = ClassUtils.getShortClassName(entityClass);
            String name = messageSource.getMessage(WordUtils.uncapitalize(className) + "." + catalog.getCode());
            catalog.setTranslatedName(name);
        }
		return list;
    }

    public List find(Class entityClass, Criteria criteria){
    	DetachedCriteria crit = DetachedCriteria.forClass(entityClass);
        CriteriaTranslatorImpl translator = new CriteriaTranslatorImpl();

    	for(Criterion criterion : criteria.getRestrictions()){
            translator.addCriteria(entityClass, crit, criterion);
        }
        return getHibernateTemplate().findByCriteria(crit);
    }

    public <T> List<T> find(String queryString){
        return getHibernateTemplate().find(queryString);
    }

    public <T> List<T> find(String queryString, Object value){
        if(value instanceof List){
            return getHibernateTemplate().find(queryString, value);
        }
        return getHibernateTemplate().find(queryString, value);
    }

    public <T> List<T> find(String queryString, Object[] values){
        return getHibernateTemplate().find(queryString, values);
    }

    public <T> List<T> find(String queryString, List values){
        return getHibernateTemplate().find(queryString, values.toArray());
    }

    public <T> T get(Class entityClass, Object primaryKey) {
        T entity = (T) getHibernateTemplate().get(entityClass, (Serializable) primaryKey);

        if (entity == null) {
            throw new ObjectRetrievalFailureException(entityClass, primaryKey);
        }

        return entity;
    }

    public <T> T get(Class entityClass, Object primaryKey, Boolean requireNewSession) {
        if(requireNewSession){
            Session session = getHibernateTemplate().getSessionFactory().openSession();
            T o;
            try{
                o = (T) session.get(entityClass, (Serializable) primaryKey);

                if (o == null) {
                    throw new ObjectRetrievalFailureException(entityClass, primaryKey);
                }
                return o;
            }catch(Exception ex){
                throw new ObjectRetrievalFailureException(entityClass, primaryKey, "Error con la session", ex);
            }
            finally {
                session.close();                
            }
        }else{
            T entity = (T) getHibernateTemplate().get(entityClass, (Serializable) primaryKey);

            if (entity == null) {
                throw new ObjectRetrievalFailureException(entityClass, primaryKey);
            }

            return entity;
        }
    }

    public <T> T get(String entityName, Object primaryKey) {
        T o = (T) getHibernateTemplate().get(entityName, (Serializable) primaryKey);

        if (o == null) {
            throw new ObjectRetrievalFailureException(entityName, primaryKey);
        }

        return o;
    }

    public <T> T get(Class entityClass, Criteria criteria){
        List list = find(entityClass, criteria);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + criteria + "] found more than once.");
            }else{
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + criteria + "] not found.");
            }
            throw new ObjectRetrievalFailureException(entityClass, criteria);
        }
        return (T) list.get(0);
    }

    public <T> T get(Class entityClass, String hql, Object param) {
        List<T> list = this.find(hql, param);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + hql + "] found more than once.");
            }else{
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + hql + "] not found.");
            }
            throw new ObjectRetrievalFailureException(entityClass, hql);
        }
        return list.get(0);
    }

    public <T> T get(Class entityClass, String hql, Object[] params) {
        List<T> list = this.find(hql, params);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + hql + "] found more than once.");
                throw new ObjectRetrievalFailureException(entityClass, hql, "More than one record exists", new Exception());
            }else{
                logger.warn("Object [" + entityClass.getName() + "] with criteria [" + hql + "] not found.");
                throw new ObjectRetrievalFailureException(entityClass, hql);
            }
        }
        return list.get(0);
    }

    public <T> T getByCode(Class entityClass, String code) {
        DetachedCriteria crit = DetachedCriteria.forClass(entityClass);

        crit.add(Restrictions.eq("code", code));

        List list = getHibernateTemplate().findByCriteria(crit);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.warn("Object [" + entityClass.getName() + "] with code [" + code + "] found more than once.");
            }else{
                logger.warn("Object [" + entityClass.getName() + "] with code [" + code + "] not found.");
            }
            throw new ObjectRetrievalFailureException(entityClass, code);
        }
        return (T) list.get(0);
    }

    public <T> T getByCode(Class entityClass, String externalSystem, String code) {
        DetachedCriteria crit = DetachedCriteria.forClass(entityClass);

        if(ClassUtils.hasSuperclass(entityClass, TranslatableCatalog.class)){
            crit.add(Restrictions.eq("translations." + externalSystem, code));
        }
        List list = getHibernateTemplate().findByCriteria(crit);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.debug("Duplicated entity with code '" + code + "'");
            }else{
                logger.debug("Object with code '" + code + "' not found...");
            }
            throw new ObjectRetrievalFailureException(entityClass, code);
        }
        return (T) list.get(0);
    }

    public <T> T getByProperty(Class entityClass, String propertyName, Object propertyValue) {
        DetachedCriteria crit = DetachedCriteria.forClass(entityClass);

        crit.add(Restrictions.eq(propertyName, propertyValue));

        List list = getHibernateTemplate().findByCriteria(crit);
        if(list.size() != 1){
            if(list.size() > 1){
                logger.warn("Object [" + entityClass.getName() + "] with " + propertyName + " [" + propertyValue + "] found more than once.");
            }else{
                logger.warn("Object [" + entityClass.getName() + "] with " + propertyName + " [" + propertyValue + "] not found.");
            }
            throw new ObjectRetrievalFailureException(entityClass, propertyValue);
        }
        return (T) list.get(0);
    }

    public <T> T load(Class entityClass, Object primaryKey) {
        T entity = (T) getHibernateTemplate().load(entityClass, (Serializable) primaryKey);

        if (entity == null) {
            throw new ObjectRetrievalFailureException(entityClass, primaryKey);
        }

        return entity;
    }

    public void save(Object entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public void save(String entityName, Object entity) {
        getHibernateTemplate().saveOrUpdate(entityName, entity);
    }

    public void delete(Object entity) {
        getHibernateTemplate().delete(entity);
    }

    public void delete(Class entityClass, Object primaryKey) {
        getHibernateTemplate().delete(get(entityClass, primaryKey));
    }

    public void delete(String entityName, Object primaryKey) {
    }

    public int execute(String query){
        return getHibernateTemplate().bulkUpdate(query);
    }

    public int execute(String query, Object value){
       return getHibernateTemplate().bulkUpdate(query, value);
    }

    public int execute(String query, Object[] values){
       return getHibernateTemplate().bulkUpdate(query, values);
    }

    public void execute(String procedureName, String paramName, Object param){
        SimpleJdbcCall call = new SimpleJdbcCall(this.jdbcTemplate).withProcedureName(procedureName);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(paramName, param);
        call.execute(params);
    }

    public Integer executeInteger(String hql){
    	return ((Long) getHibernateTemplate().iterate(hql).next()).intValue();
    }

    public Integer executeInteger(String hql, Object value){
    	return ((Long) getHibernateTemplate().iterate(hql, value).next()).intValue();
    }

    public Integer executeInteger(String hql, Object[] values){
    	return ((Long) getHibernateTemplate().iterate(hql, values).next()).intValue();
    }

    public Double executeDouble(String hql, Object[] values){
        return (Double) getHibernateTemplate().iterate(hql, values).next();
    }

}
