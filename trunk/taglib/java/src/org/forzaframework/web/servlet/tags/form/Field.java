package org.forzaframework.web.servlet.tags.form;

/**
 * User: cesarreyes
 * Date: 02-sep-2007
 * Time: 22:56:29
 * Description:
 */
public class Field{

    protected String id;
    protected String value;
    protected String field;
    protected String type;
    protected String title;
    protected String mapping;
    protected Boolean hidden;
    protected Integer width;
    protected Object editorJson;
    protected String xtype;
    protected String displayField;
    protected String rendererFunction;
    protected Boolean locked;

    public Field(){
    }
    
    public Field(String id, String field, String mapping) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Object getEditorJson() {
        return editorJson;
    }

    public void setEditorJson(Object editorJson) {
        this.editorJson = editorJson;
    }

    public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getRendererFunction() {
        return rendererFunction;
    }

    public void setRendererFunction(String rendererFunction) {
        this.rendererFunction = rendererFunction;
    }
}
