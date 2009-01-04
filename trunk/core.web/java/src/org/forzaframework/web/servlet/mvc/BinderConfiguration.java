package org.forzaframework.web.servlet.mvc;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.BeanUtils;
import org.forzaframework.beans.propertyeditors.CustomEntityIdCollectionEditor;
import org.forzaframework.beans.propertyeditors.CustomJsonCollectionEditor;

import java.util.Map;
import java.util.List;
import java.beans.PropertyEditor;
import java.lang.reflect.Constructor;

/**
 * @author cesarreyes
 * Date: 09-sep-2008
 * Time: 15:14:36
 */
public class BinderConfiguration {

    /**
     * Configure the given {@link org.springframework.web.bind.ServletRequestDataBinder}.
     *
     * @param applicationContext The {@link org.springframework.context.ApplicationContext} to use
     * for retrieving configured {@link java.beans.PropertyEditor}s.
     * @param customEditors The map of custom editors to configure (as described in class javadoc).
     * @param binder The {@link org.springframework.web.bind.ServletRequestDataBinder} to configure.
     */
    public void configureBinder(ApplicationContext applicationContext, List<Map> customEditors, ServletRequestDataBinder binder) throws Exception {
        // Configured editors:
        for (Map config : customEditors) {
            Class editorType = Class.forName(config.get("editorType").toString());
            Class propertyType = Class.forName(config.get("propertyType").toString());
            Class collectionType = Class.forName(config.get("collectionType").toString());

            PropertyEditor editor;

            if(editorType.equals(CustomJsonCollectionEditor.class)){
                Constructor[] candidates = editorType.getDeclaredConstructors();
                Constructor contructorToUse = null;
                for(Constructor candidate : candidates){
                    Class[] classes = candidate.getParameterTypes();
                    if(classes != null && classes.length == 2){
                        contructorToUse = candidate;
                        break;
                    }

                }
                editor = (PropertyEditor) BeanUtils.instantiateClass(contructorToUse, new Object[]{ collectionType, propertyType });
            }
            else if(editorType.equals(CustomEntityIdCollectionEditor.class)){
                Constructor[] candidates = editorType.getDeclaredConstructors();
                Constructor contructorToUse = null;
                for(Constructor candidate : candidates){
                    Class[] classes = candidate.getParameterTypes();
                    if(classes != null && classes.length == 3){
                        contructorToUse = candidate;
                        break;
                    }

                }
                Object manager = applicationContext.getBean("entityManager");
                editor = (PropertyEditor) BeanUtils.instantiateClass(contructorToUse, new Object[]{ collectionType, propertyType, manager });

            }else{
                editor = (PropertyEditor) BeanUtils.instantiateClass(editorType);
            }

            if(collectionType != null){
                binder.registerCustomEditor(collectionType, config.get("property").toString(), editor);
            }
            else{
                binder.registerCustomEditor(propertyType, editor);
            }
        }
    }
}
