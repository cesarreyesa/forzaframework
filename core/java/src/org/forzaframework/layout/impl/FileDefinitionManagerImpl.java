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

package org.forzaframework.layout.impl;

import org.forzaframework.layout.FileDefinitionManager;
import org.forzaframework.layout.FileDefinition;
import org.forzaframework.core.persistance.EntityManager;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:57:00
 */
@SuppressWarnings("uncheked")
public class FileDefinitionManagerImpl implements FileDefinitionManager {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<FileDefinition> getFileDefinitionsByEntityCode(String code) {
        String hql = "from FileDefinition where entity = :entityCodeParam or entity is null";
        Query query = entityManager.getHibernateSession().createQuery(hql);
        query.setParameter("entityCodeParam", code);

        return query.list();

        //Implementacion de CriteriaBuilder de hibernate 5
//        Session session = entityManager.getHibernateSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<FileDefinition> cr = cb.createQuery(FileDefinition.class);
//        Root<FileDefinition> root = cr.from(FileDefinition.class);
//        Predicate entityCode = cb.like(root.get("entity"), code);
//        Predicate entityNull = cb.isNull(root.get("entity"));
//        cr.select(root).where(cb.or(entityCode, entityNull));
//
//        Query<FileDefinition> query = session.createQuery(cr);
//
//        return query.getResultList();
    }

    public List<FileDefinition> getFileDefinitionsByEntityCodes(List<String> codes) {
        String hql = "from FileDefinition fd where fd.entity in (:codesParam)";
        Query query = entityManager.getHibernateSession().createQuery(hql);
        query.setParameterList("codesParam", codes);

        return query.list();
    }
}
