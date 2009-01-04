package org.forzaframework.beans.propertyeditors;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.BeanUtils;
import org.forzaframework.bind.JsonDataBinder;
import net.sf.json.JSONObject;

/**
 * @author cesarreyes
 * Date: 09-sep-2008
 * Time: 15:15:54
 */
public class CustomJsonCollectionEditor extends CustomCollectionEditor {

    private Class itemType;

    public CustomJsonCollectionEditor(Class collectionType, Class itemType) {
        super(collectionType);
        this.itemType = itemType;
    }

    public CustomJsonCollectionEditor(Class collectionType, boolean nullAsEmptyCollection, Class itemType) {
        super(collectionType, nullAsEmptyCollection);
        this.itemType = itemType;
    }

    protected Object convertElement(Object element) {
        if(element != null){
            JSONObject jsonObject = JSONObject.fromObject(element);
            Object bean = BeanUtils.instantiateClass(itemType);

            JsonDataBinder binder = new JsonDataBinder(bean);
            binder.bind(jsonObject);

            return bean;
        }
        else{
            return null;
        }
    }
}
