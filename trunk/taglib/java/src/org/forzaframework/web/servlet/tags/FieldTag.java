package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.form.ComboboxTag;
import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 29-nov-2007
 * Time: 23:14:52
 * Description:
 */
public class FieldTag extends BaseTag {

    protected String field;
    protected String mapping;

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

    public int doEndTag() throws JspException {

        Tag parent = getParent();

        Field field = new Field();
        field.setField(this.field);
        field.setMapping(mapping);

        if(parent instanceof DataViewTag)
            ((DataViewTag) parent).addField(field);
        else if(parent instanceof ComboboxTag)
            ((ComboboxTag) parent).addField(field);
        //DataViewTag parent = (DataViewTag) findParent(DataViewTag.class);
        //parent.addField(field);

        return Tag.EVAL_PAGE;
    }

}
