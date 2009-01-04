package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.JSONFunction;
import org.forzaframework.web.servlet.tags.Item;

/**
 * User: Cesar Reyes
 * Date: 12/06/2007
 * Time: 04:04:37 PM
 * Description:
 */
public class RadioButtonTag extends FieldTag {

    public String onCheck;
    private Boolean checked;

	public String getOnCheck() {
        return onCheck;
    }

    public void setOnCheck(String onCheck) {
        this.onCheck = onCheck;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return "radio";
    }

    public int doEndTag() throws JspException {
        RadioGroupTag radioGroupTag = (RadioGroupTag) findParent(RadioGroupTag.class);
        if(radioGroupTag != null){
            JSONObject json = new JSONObject();

            json.elementOpt("id", this.getId());
            json.put("boxLabel", getTitleKey() != null ? getText(getTitleKey()) : getTitle());
            RadioGroupTag parent = (RadioGroupTag) findParent(RadioGroupTag.class);
            if(parent != null){
                json.put("name", parent.getField());
            }else{
                json.put("name", this.getField());
            }
            json.elementOpt("inputValue", this.getValue());
            json.elementOpt("disabled", this.getDisabled());
            json.elementOpt("width", getWidth());
            json.elementOpt("checked", getChecked());
            
            if(StringUtils.isNotBlank(onCheck)){
                JSONObject listeners = new JSONObject();
                listeners.put("check", new JSONFunction(onCheck));
                json.put("listeners", listeners);
            }

            radioGroupTag.addItem(new Item(json));

            doFinally();
            return EVAL_PAGE;
        }else{
            return super.doEndTag();
        }
    }

    public JSON toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", this.getId());
        json.put("boxLabel", getTitleKey() != null ? getText(getTitleKey()) : getTitle());
        json.put("hideLabel", true);
        json.put("labelSeparator", "");
        RadioGroupTag parent = (RadioGroupTag) findParent(RadioGroupTag.class);
        if(parent != null){
            json.put("name", parent.getField());
        }else{
            json.put("name", this.getField());
        }
        json.elementOpt("checked", getChecked());
        json.elementOpt("disabled", this.getDisabled());
        json.elementOpt("width", getWidth());
        json.put("validateOnBlur", false);
        json.put("xtype", getType());
        json.elementOpt("inputValue", this.getValue());

        if(StringUtils.isNotBlank(onCheck)){
            JSONObject listeners = new JSONObject();
            listeners.put("check", new JSONFunction(onCheck));
            json.put("listeners", listeners);
        }

        return json;
    }

    public String getHtmlDeclaration(){
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"radio\">");

        return sb.toString();
    }

}
