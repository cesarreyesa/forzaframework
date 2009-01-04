package org.forzaframework.web.servlet.tags;

import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;

import net.sf.json.JSONObject;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 0:59:27
 * Description:
 */
public class NumberColumnTag extends ColumnTag {

    private Integer decimalPrecision;
    private Boolean allowDecimals;

    public Integer getDecimalPrecision() {
        return decimalPrecision;
    }

    public void setDecimalPrecision(Integer decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    public Boolean getAllowDecimals() {
        return allowDecimals;
    }

    public void setAllowDecimals(Boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }

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

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
    private Object toJSON() {
        JSONObject config = new JSONObject();
        config.elementOpt("decimalPrecision", decimalPrecision);
        config.elementOpt("allowDecimals", allowDecimals);
        return new JSONFunction("new Ext.form.NumberField(" + config.toString() + ")");
    }

}
