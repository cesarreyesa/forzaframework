package org.forzaframework.web.servlet.tags;

/**
 * User: Cesar Reyes
 * Date: 26/01/2007
 * Time: 04:54:24 PM
 * Description:
 */
public class UpdateField {

    private String id;
    private String field;
    private String mapping;

    public UpdateField(String id, String field, String mapping) {
        this.id = id;
        this.field = field;
        this.mapping = mapping;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
