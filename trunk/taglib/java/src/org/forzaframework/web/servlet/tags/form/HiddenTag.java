package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;

/**
 * User: Cesar Reyes
 * Date: 13/06/2007
 * Time: 05:27:19 PM
 * Description:
 */
public class HiddenTag extends FieldTag {

    public String getType() {
        return "hidden";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("name", getField());
        json.elementOpt("value", getValue());
        json.elementOpt("anchor", anchor);
        json.put("inputType", "hidden");
        json.put("fieldLabel", "");
        json.put("labelSeparator", "");
        json.put("boxLabel", "");
        json.put("hidden", true);
        json.put("style", "visibility:hidden;");
        json.put("xtype", getType());

        return json;
    }

    public String getHtmlDeclaration(){
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"hidden\">");

        return sb.toString();
    }

}
