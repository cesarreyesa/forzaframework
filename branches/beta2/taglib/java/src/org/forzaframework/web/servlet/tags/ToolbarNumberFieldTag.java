/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.web.servlet.tags;

import net.sf.json.JSONObject;

import javax.servlet.jsp.JspException;

/**
 * User: christian.tavera
 * Date: 7/09/11
 * Time: 11:09 AM
 */
public class ToolbarNumberFieldTag extends org.forzaframework.web.servlet.tags.form.FieldTag {
    private Boolean allowDecimals;
    private Boolean allowNegative;
    private String baseChars;
    private Integer decimalPrecision;
    private String decimalSeparator;
    private Double maxValue;
    private Double minValue;
    private Integer maxLength;
    private Integer minLength;
    private Boolean enableKeyEvents;

    public Boolean getAllowDecimals() {
        return allowDecimals;
    }

    public void setAllowDecimals(Boolean allowDecimals) {
        this.allowDecimals = allowDecimals;
    }

    public Boolean getAllowNegative() {
        return allowNegative;
    }

    public void setAllowNegative(Boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    public String getBaseChars() {
        return baseChars;
    }

    public void setBaseChars(String baseChars) {
        this.baseChars = baseChars;
    }

    public Integer getDecimalPrecision() {
        return decimalPrecision;
    }

    public void setDecimalPrecision(Integer decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
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

    public String getType() {
        return "numberfield";
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        parent.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);
        json.elementOpt("value", value);
        json.elementOpt("hidden", hidden);
        json.elementOpt("disabled", disabled);
        json.elementOpt("width", width);
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("minValue", minValue);
        json.elementOpt("maxValue", maxValue);
        json.elementOpt("minLength", minLength);
        json.elementOpt("maxLength", maxLength);
        json.elementOpt("allowDecimals", allowDecimals);
        json.elementOpt("allowNegative", allowNegative);
        json.elementOpt("baseChars", baseChars);
        json.elementOpt("decimalPrecision", decimalPrecision);
        json.elementOpt("decimalSeparator", decimalSeparator);
        json.put("xtype", getType());

        json.put("enableKeyEvents", enableKeyEvents);

        if (this.listeners.size() > 0) {
            JSONObject listeners = new JSONObject();
            for (Listener listener : this.listeners) {
                listeners.put(listener.getEventName(), new JSONFunction(listener.getHandler()));
            }
            json.put("listeners", listeners);
        }

        return json;
    }

    public String getHtmlDeclaration() {
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"text\">");

        return sb.toString();
    }
}
