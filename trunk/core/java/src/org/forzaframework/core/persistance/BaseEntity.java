package org.forzaframework.core.persistance;

import org.dom4j.Element;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:52:18
 */
public abstract class BaseEntity {

    public abstract void setKey(Object id);
    public abstract Object getKey();

    public Element toXml(){
        return null;
    }

    public Element toXml(String elementName){
        return null;
    }


}
