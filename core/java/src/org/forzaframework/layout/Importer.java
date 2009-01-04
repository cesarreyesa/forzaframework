package org.forzaframework.layout;

import org.springframework.validation.BindException;
import org.forzaframework.core.persistance.EntityManager;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 17:06:57
 */
public interface Importer<T> {

    void setEntityManager(EntityManager manager);
    List<T> convert(Class clazz, FileDefinition fileDefinition, String path, List<BindException> errors) throws Exception;
}
