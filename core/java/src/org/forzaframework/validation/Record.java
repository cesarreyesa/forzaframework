package org.forzaframework.validation;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.ArrayList;

/**
 * User: cesarreyes
 * Date: 05-nov-2007
 * Time: 17:58:48
 * Description:
 */
public class Record {

    private String entityId;
    private Object entity;
    private List<Field> fields = new ArrayList<Field>();
    private List<Error> errors = new ArrayList<Error>();

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field){
        this.fields.add(field);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void addError(Error error){
        this.errors.add(error);
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("entityId", entityId);
        json.elementOpt("entity", entity);
        if(this.errors.size() > 0){
            JSONArray errors = new JSONArray();
            for(Error error : this.errors){
                errors.add(error.toJSON());
            }
            json.put("errors", errors);
        }
        if(this.fields.size() > 0){
            JSONArray fields = new JSONArray();
            for(Field field : this.fields){
                fields.add(field.toJSON());
            }
            json.put("fields", fields);
        }

        return json;
    }

    public String toJSONString(){
        return toJSON().toString();
    }
}
