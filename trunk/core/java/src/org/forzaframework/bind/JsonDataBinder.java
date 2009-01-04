package org.forzaframework.bind;

import org.springframework.validation.DataBinder;
import org.springframework.beans.MutablePropertyValues;
import net.sf.json.JSONObject;

import java.util.Iterator;

/**
 * @author cesarreyes
 *         Date: 19-ago-2008
 *         Time: 13:12:55
 */
public class JsonDataBinder extends DataBinder {

    public JsonDataBinder(Object target) {
        super(target);
    }

    public JsonDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    public void bind(JSONObject json) {
        MutablePropertyValues mpvs = convertToPropertyValues(json);
        doBind(mpvs);
    }

    public void bind(String json) {
        MutablePropertyValues mpvs = convertToPropertyValues(json);
        doBind(mpvs);
    }

    public static MutablePropertyValues convertToPropertyValues(String json){
        return convertToPropertyValues(JSONObject.fromObject(json));
    }

    public static MutablePropertyValues convertToPropertyValues(JSONObject json){
        MutablePropertyValues mpvs = new MutablePropertyValues();
        for(Iterator i = json.keys(); i.hasNext(); ){
            String key = (String) i.next();
            mpvs.addPropertyValue(key, json.get(key));
        }
        return mpvs;
    }
}
