package org.forzaframework.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.forzaframework.core.persistance.EntityManager;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:22:35
 */
public class CustomEntityIdCollectionEditor extends CustomCollectionEditor {

    private Class itemType;
    private EntityManager entityManager;

    public CustomEntityIdCollectionEditor(Class collectionType, Class itemType, EntityManager entityManager) {
        super(collectionType);
        this.entityManager = entityManager;
        this.itemType = itemType;
    }

    public CustomEntityIdCollectionEditor(Class collectionType, boolean nullAsEmptyCollection, Class itemType, EntityManager entityManager) {
        super(collectionType, nullAsEmptyCollection);
        this.entityManager = entityManager;
        this.itemType = itemType;
    }

    protected Object convertElement(Object element) {
        if(element != null){
            return  entityManager.get(itemType, Long.valueOf(element.toString()));
        }
        else{
            return null;
        }
    }

}
