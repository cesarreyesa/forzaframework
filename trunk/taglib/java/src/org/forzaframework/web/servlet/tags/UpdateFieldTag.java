package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.form.ComboboxTag;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 26/01/2007
 * Time: 04:45:34 PM
 * Description:
 */
public class UpdateFieldTag extends BaseTag {

    private String field;
    private String mapping;

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
        ((ComboboxTag) parent).addField(id, field, mapping);
        return EVAL_PAGE;
    }

}
