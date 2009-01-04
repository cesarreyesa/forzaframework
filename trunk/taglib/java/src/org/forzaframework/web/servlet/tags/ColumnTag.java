package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.form.Field;
import org.forzaframework.web.servlet.tags.form.LovFieldTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 24-dic-2006
 * Time: 12:00:58
 * Description:
 */
public class ColumnTag extends BaseTag {

//    protected String defaultValue;
    protected String title;
    protected String titleKey;
    protected String field;
    protected String mapping;
    protected Boolean hidden;
    protected Boolean locked;
    protected Integer width;
    protected Boolean disabled;
    protected String rendererFunction;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getRendererFunction() {
        return rendererFunction;
    }

    public void setRendererFunction(String rendererFunction) {
        this.rendererFunction = rendererFunction;
    }

    public int doEndTag() throws JspException {

    	Field field = new Field();
        field.setField(this.field);
        field.setTitle(title != null ? title : getText(titleKey));
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setWidth(width);
        field.setLocked(locked);
        field.setRendererFunction(rendererFunction);

        Tag parent = findParent(GridTag.class);
        if(parent != null){
        	((GridTag) parent).addField(field);	
        }
        else{
        	parent = findParent(EditableGridTag.class);
        	if(parent != null){
            	((EditableGridTag) parent).addField(field);
        	}else{
        		parent = findParent(LovFieldTag.class);
        		if(parent != null){
        			((LovFieldTag) parent).addField(field);
        		}
        	}        	
        }
        
        return Tag.EVAL_PAGE;
    }

}
