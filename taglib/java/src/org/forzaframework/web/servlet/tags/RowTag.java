package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.EditableGridTag;
import org.forzaframework.web.servlet.tags.BaseTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: Cesar Reyes
 * Date: 14/03/2007
 * Time: 04:59:29 PM
 * Description:
 */
public class RowTag extends BaseTag {

    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int doEndTag() throws JspException {
        Tag tag = getParent().getParent();
        EditableGridTag parent;
        if(tag instanceof EditableGridTag){
            parent = (EditableGridTag) tag;
        }else{
            parent = (EditableGridTag) tag.getParent();
        }
        parent.addRow(json);

        return EVAL_PAGE;
    }

}
