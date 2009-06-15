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

package org.forzaframework.security.service.impl;

import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.forzaframework.security.service.SmartGroupManager;
import org.forzaframework.security.SmartGroup;
import org.forzaframework.security.User;
import org.forzaframework.core.persistance.EntityManager;
import org.apache.commons.lang.ClassUtils;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:28:30
 */
@SuppressWarnings("unchecked")
public class SmartGroupManagerImpl implements SmartGroupManager {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(SmartGroup smartGroup){
    	entityManager.save(smartGroup);
    }

    @Deprecated
    public void applySmartGroup(SmartGroup smartGroup){
//        List entities = getEntities(smartGroup);
//        for(Object entity : entities){
//            smartGroup.addMap(((BaseEntity) entity).getId());
//        }
//        this.save(smartGroup);
//
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        for(Object entity : entities){
//            securityManager.addAclPermission(entity, user);
//        }
    }


    public Boolean isInSmartGroup(ObjectIdentity objectIdentity, List<SmartGroup> smartGroups) {

        for(SmartGroup smartGroup : smartGroups){
            Boolean isInGroup = isInSmartGroup(objectIdentity, smartGroup);
            if(isInGroup) return true;
        }
        return false;
    }

//    public List getActualEntities(SmartGroup smartGroup) {
//        DetachedCriteria crit = DetachedCriteria.forClass(Employee.class);
//        for(SmartGroupFilter filter : smartGroup.getFilters()){
//            List<Long> list = new ArrayList<Long>();
//            for(String item : filter.getEntityIds()){
//                list.add(Long.valueOf(item));
//            }
//            crit.createCriteria(filter.getType()).add(Restrictions.in("id", list));
//        }
//        return getHibernateTemplate().findByCriteria(crit);
//    }

//    public List getEntities(SmartGroup smartGroup){
//        String hql = "from " + ClassUtils.getShortClassName(smartGroup.getEntity()) + " as entity";
//        if(smartGroup.getFilters().size() > 0){
//            hql += " where entity.id in (";
//            Boolean first = true;
//            for(SmartGroupMap map : smartGroup.getMaps()){
//                if(first)
//                    first = false;
//                else
//                    hql += ",";
//                hql += map.getKey();
//            }
//            hql += ") ";
//        }
//        log.debug("executing hql: " + hql);
//        return getHibernateTemplate().find(hql);
//    }

    public List<SmartGroup> getSmartGroupsForUser(User user) {
//        DetachedCriteria crit = DetachedCriteria.forClass(SmartGroup.class);
//
//        crit.add(Restrictions.eq("user", user));
//        crit.add(Restrictions.eq("clazz", clazz));

//        return getHibernateTemplate().findByCriteria(crit);
        return entityManager.find("select user.smartGroups from User as user where user.username = ?", user.getUsername());
    }

    public List<User> getSmartGroupUsers(SmartGroup smartGroup){
        String hql = "from User as user where user.smartGroups.id = ?";
        return entityManager.find(hql, smartGroup.getId());
    }

    public List<User> getUsersNotInSmartGroup(SmartGroup smartGroup){
//        String hql = "from EUser as user where size(user.smartGroups) = 0 or user.smartGroups.id <> ?";
        String hql = "from User as user where user.id not in (select user.id from EUser as user where user.smartGroups.id = ?) ";
        return entityManager.find(hql, smartGroup.getId());
    }

    public List getEntities(SmartGroup smartGroup, Class clazz) {
        String hql = "from " + ClassUtils.getShortClassName(clazz) + " as entity";
//        if(smartGroup.getFilters().size() > 0){
//            hql += " where ";
//            Boolean first1 = true;
//            for(SmartGroupFilter filter : smartGroup.getFilters()){
//                if(first1)
//                    first1 = false;
//                else
//                    hql += " and ";
//
//                hql += " entity." + filter.getEntity() + ".id in (";
//                Boolean first = true;
//                for(String id : filter.getEntityIds()){
//                    if(first)
//                        first = false;
//                    else
//                        hql += ",";
//                    hql += id;
//                }
//                hql += ") ";
//            }
//        }
//        log.debug("executing hql: " + hql);
        return entityManager.find(hql);
    }

    public Boolean isInSmartGroup(ObjectIdentity objectIdentity, SmartGroup smartGroup) {
        String hql = "from " + ClassUtils.getShortClassName(objectIdentity.getJavaType()) + " as entity";
//        if(smartGroup.getFilters().size() > 0){
//            hql += " where ";
//            Boolean first1 = true;
//            for(SmartGroupFilter filter : smartGroup.getFilters()){
//                if(first1)
//                    first1 = false;
//                else
//                    hql += " and ";
//
//                hql += " entity." + filter.getEntity() + ".id in (";
//                Boolean first = true;
//                for(String id : filter.getEntityIds()){
//                    if(first)
//                        first = false;
//                    else
//                        hql += ",";
//                    hql += id;
//                }
//                hql += ") ";
//            }
//        }
        hql += " and entity.id = " + objectIdentity.getIdentifier().toString();
        return entityManager.find(hql).size() > 0;
    }
}
