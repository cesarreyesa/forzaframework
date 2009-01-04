package org.forzaframework.web.servlet.tags.form;

import org.forzaframework.web.servlet.tags.BaseBodyTag;
import org.forzaframework.web.servlet.tags.ComboboxColumnTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 25-oct-2007
 * Time: 14:39:42
 * Description:
 */
public class OptionTag extends BaseBodyTag {

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int doEndTag() throws JspException {
        Tag tag = findParent(ComboboxTag.class);
        if(tag != null){
            ((ComboboxTag) tag).addOption(new Option(value, text));            
        }else{
            tag = findParent(ComboboxColumnTag.class);
            ((ComboboxColumnTag) tag).addOption(new Option(value, text));
        }

        return EVAL_PAGE;
    }

}
