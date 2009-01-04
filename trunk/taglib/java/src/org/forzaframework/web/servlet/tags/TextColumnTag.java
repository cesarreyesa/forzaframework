package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 0:27:18
 * Description:
 */
public class TextColumnTag extends ColumnTag{

    public int doEndTag() throws JspException {
        EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
        Field field = new Field();

        field.setField(this.getField());
        field.setTitle(title != null ? title : getText(titleKey));
        field.setWidth(width);
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setType("string");
        field.setEditorJson(this.toJSON());

        tag.addField(field);
        return EVAL_PAGE;
    }

    private Object toJSON() {
        return new JSONFunction("new Ext.form.TextField()");        
    }
}
