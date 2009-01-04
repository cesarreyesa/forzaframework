package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;
import org.forzaframework.web.servlet.tags.Listener;
import org.forzaframework.web.servlet.tags.JSONFunction;

/**
 * @author Cesar Reyes
 * Date: 11/06/2007
 * Time: 03:11:23 PM
 */
public class InputTag extends FieldTag {

    private Boolean allowBlank;
    private Integer maxLength;
    private Integer minLength;
    private Boolean enableKeyEvents;
    private String inputType;

    public Boolean getAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean getEnableKeyEvents() {
        return enableKeyEvents;
    }

    public void setEnableKeyEvents(Boolean enableKeyEvents) {
        this.enableKeyEvents = enableKeyEvents;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getType() {
        return "textfield";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        if(getTitle() != null || getTitleKey() != null){
            json.put("fieldLabel", title != null ? title : getText(titleKey));
        }
        json.put("name", this.getField());
//        json.elementOpt("mapping", this.getMapping());
        json.elementOpt("value", this.getValue());
        json.elementOpt("disabled", this.getDisabled());
        json.elementOpt("width", getWidth());
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("anchor", anchor);
        json.elementOpt("description", getDescription());
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("renderHidden", hidden);
        json.elementOpt("hideLabel", getHideLabel());
        json.elementOpt("minLength", minLength);
        json.elementOpt("maxLength", maxLength);
        json.elementOpt("labelSeparator", getLabelSeparator());
        json.elementOpt("inputType", getInputType());
//        json.put("validateOnBlur", false);
        json.put("xtype", getType());
        
        json.put("enableKeyEvents", enableKeyEvents);

        if(this.listeners.size() > 0){
            JSONObject listeners = new JSONObject();
            for (Listener listener : this.listeners) {
                listeners.put(listener.getEventName(), new JSONFunction(listener.getHandler()));
            }
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
        sb.append("\" type=\"text\">");

        return sb.toString();
    }
}
