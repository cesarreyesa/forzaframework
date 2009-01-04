package org.forzaframework.layout.impl;

import org.forzaframework.layout.FileDefinitionManager;
import org.forzaframework.layout.FileDefinition;
import org.forzaframework.core.persistance.EntityManager;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:57:00
 */
public class FileDefinitionManagerImpl implements FileDefinitionManager {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<FileDefinition> getFileDefinitionsByEntityCode(String code) {
        String hql = "from FileDefinition where entity = ? or entity is null";
        return entityManager.find(hql, code);
    }
}
