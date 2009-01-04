package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 03:11:23 PM
 * Description:
 */
public class TextAreaTag extends FieldTag {

    private Boolean grow;

    public Boolean getGrow() {
        return grow;
    }

    public void setGrow(Boolean grow) {
        this.grow = grow;
    }

    public String getType() {
        return "textarea";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.put("name", getField());
        json.elementOpt("description", getDescription());
        json.elementOpt("value", getValue());
        json.elementOpt("width", getWidth());
        json.elementOpt("height", getHeight());
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("anchor", anchor);
        json.elementOpt("grow", grow);
        json.put("validateOnBlur", false);
        json.elementOpt("disabled", disabled);
        json.put("xtype", getType());

        return json;
    }

    public String getHtmlDeclaration(){
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"text\">");

        return sb.toString();
    }
}