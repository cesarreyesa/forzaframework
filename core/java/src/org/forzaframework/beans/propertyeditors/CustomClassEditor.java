package org.forzaframework.beans.propertyeditors;

import org.springframework.util.StringUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.core.persistance.EntityManager;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

/**
 * @author cesarreyes
 *         Date: 14-ago-2008
 *         Time: 16:20:02
 */
/**
 * Editor for <code>java.lang.Class</code>, to directly populate a Class property
 * instead of using a String class name property as bridge.
 *
 * <p>Also supports "java.lang.String[]"-style array class names,
 * in contrast to the standard <code>Class.forName</code> method.
 * Delegates to ClassUtils for actual class name resolution.
 *
 * @author Cesar Reyes
 * @since 11.12.2005
 * @see java.lang.Class#forName
 * @see org.springframework.util.ClassUtils#forName
 */
public class CustomClassEditor<T extends BaseEntity> extends PropertyEditorSupport {

    private Class requiredType;
    private Class pkType;
    private EntityManager entityManager;

    /**
	 * Create a default ClassEditor, using the thread context ClassLoader.
	 */
	public CustomClassEditor(Class requiredType) {
        this.requiredType = requiredType;
    }

    public CustomClassEditor(Class requiredType, Class pkType) {
        this.requiredType = requiredType;
        this.pkType = pkType;
    }

    public CustomClassEditor(Class requiredType, EntityManager entityManager) {
        this.requiredType = requiredType;
        this.entityManager = entityManager;
    }

    public CustomClassEditor(Class requiredType, Class pkType, EntityManager entityManager) {
        this.requiredType = requiredType;
        this.pkType = pkType;
        this.entityManager = entityManager;
    }

    public void setManager(EntityManager em) {
        this.entityManager = em;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        T command;
        try {
            command = (T) requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return ;
        }

        if (StringUtils.hasText(text)) {
            Serializable id;

            if(pkType == null || !pkType.equals(String.class)){
                id = Long.valueOf(text);
            }
            else {
                id = text;
            }

            if(entityManager == null){
                command.setKey(id);
            }
            else{
                command = (T) entityManager.get(requiredType, id);
            }
			setValue(command);
		}
		else {
			setValue(null);
		}
	}

	public String getAsText() {
        T command;
        try {
            command = (T) requiredType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return "";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }

        Object value = getValue();
        command = (T) value;
		if (command == null || command.getKey() == null) {
			return "";
		}
		return command.getKey().toString();
	}

}
