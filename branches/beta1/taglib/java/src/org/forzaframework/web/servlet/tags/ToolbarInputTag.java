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
import org.forzaframework.web.servlet.tags.form.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * User: christian.tavera
 * Date: 6/09/11
 * Time: 04:13 PM
 */
public class ToolbarInputTag extends org.forzaframework.web.servlet.tags.form.FieldTag {

    private Integer maxLength;
    private Integer minLength;
    private Boolean enableKeyEvents;

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
        return "textfield";
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        parent.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.elementOpt("value", value);
        json.elementOpt("disabled", disabled);
        json.elementOpt("width", width);
        json.elementOpt("anchor", anchor);
        json.elementOpt("hidden", hidden);
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("minLength", minLength);
        json.elementOpt("maxLength", maxLength);
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
