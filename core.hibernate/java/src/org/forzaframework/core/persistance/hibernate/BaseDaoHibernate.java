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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.forzaframework.core.persistance.*;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:17:18
 */

@SuppressWarnings("unchecked")
public class BaseDaoHibernate extends HibernateDaoSupport implements Dao {
    protected final Log log = LogFactory.getLog(getClass());
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List getAll(Class clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }

    public List getAll(Class clazz, String query) {
        DetachedCriteria crit = DetachedCriteria.forClass(clazz);
        Disjunction disjunction = Restrictions.disjunction();

        if(StringUtils.isNotBlank(query)) {
            disjunction.add(Restrictions.like("code", query, MatchMode.ANYWHERE).ignoreCase());
            disjunction.add(Restrictions.like("name", query, MatchMode.ANYWHERE).ignoreCase());
        }
        crit.add(disjunction);
        crit.addOrder(Order.asc("id"));

        log.debug("disjunction:" + disjunction.toString());
        return getHibernateTemplate().findByCriteria(crit);
    }

    public List getAll(Class clazz, String field, String criteria) {
        DetachedCriteria crit = DetachedCriteria.forClass(clazz);

        crit.add(Restrictions.like(field, criteria).ignoreCase());

        return getHibernateTemplate().findByCriteria(crit);
    }

    public List find(String hql){
        return getHibernateTemplate().find(hql);
    }

    public List find(String hql, Object param){
        return getHibernateTemplate().find(hql, param);
    }

    public List find(String hql, Object[] params){
        return getHibernateTemplate().find(hql, params);
    }

    public List find(Class entityClass, Criteria criteria){
    	DetachedCriteria crit = DetachedCriteria.forClass(entityClass);
        CriteriaTranslatorImpl translator = new CriteriaTranslatorImpl();

    	for(org.forzaframework.core.persistance.Criterion criterion : criteria.getRestrictions()){
            translator.addCriteria(entityClass, crit, criterion);
        }
        return getHibernateTemplate().findByCriteria(crit);
    }

    public <T> T get(Class clazz, Object primaryKey) {
        T o = (T) getHibernateTemplate().get(clazz, (Serializable) primaryKey);

        if (o == null) {
            throw new ObjectRetrievalFailureException(clazz, primaryKey);
        }

        return o;
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

    public <T> T getByCode(Class clazz, String code) {
        DetachedCriteria crit = DetachedCriteria.forClass(clazz);

        crit.add(Restrictions.eq("code", code));

        List list = getHibernateTemplate().findByCriteria(crit);
        if(list.size() != 1){
            if(list.size() > 1){
                log.debug("Duplicated entity with code '" + code + "'");
            }else{
                log.debug("Object with code '" + code + "' not found...");
            }
            throw new ObjectRetrievalFailureException(clazz, code);
        }
        return (T) list.get(0);
    }

    public <T> T getByCode(String clazz, String code) {
        List list = getHibernateTemplate().find("from " + clazz + " where code = ?", code);
        if(list.size() != 1){
            if(list.size() > 1){
                log.debug("Duplicated entity with code '" + code + "'");
            }else{
                log.debug("Object with code '" + code + "' not found...");
            }
            throw new ObjectRetrievalFailureException(clazz, code);
        }
        return (T) list.get(0);
    }

//	public <T> T retriveByCode(Class clazz, String externalSystem, String code) {
//        DetachedCriteria crit = DetachedCriteria.forClass(clazz);
//
//        if(ClassUtils.hasSuperclass(clazz, TranslatableCatalog.class)){
//            crit.add(Restrictions.eq("translations." + externalSystem, code));
//        }
//        List list = getHibernateTemplate().findByCriteria(crit);
//        if(list.size() != 1){
//            if(list.size() > 1){
//                log.debug("Duplicated entity with code '" + code + "'");
//            }else{
//                log.debug("Object with code '" + code + "' not found...");
//            }
//            throw new ObjectRetrievalFailureException(clazz, code);
//        }
//        return (T) list.get(0);
//    }

    public void save(Object entity) {
        getHibernateTemplate().saveOrUpdate(entity);
    }

    public void save(String entityName, Object entity) {
        getHibernateTemplate().saveOrUpdate(entityName, entity);
    }

    public void delete(Object entity) {
        getHibernateTemplate().delete(entity);
    }

    public void delete(String entityName, Object entity) {
        getHibernateTemplate().delete(entity);
    }

    public <T> T get(Class clazz, Object primaryKey, Boolean requireNewSession) {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        T o;
        try{
            o = (T) session.get(clazz, (Serializable) primaryKey);

            if (o == null) {
                throw new ObjectRetrievalFailureException(clazz, primaryKey);
            }
            return o;
        }catch(Exception ex){
            session.close();
            throw new ObjectRetrievalFailureException(clazz, primaryKey, "Error con la session", ex);
        }
    }

    public void execute(String procedureName, String paramName, Object param){
        SimpleJdbcCall call = new SimpleJdbcCall(this.jdbcTemplate).withProcedureName(procedureName);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(paramName, param);
        call.execute(params);
    }
}
