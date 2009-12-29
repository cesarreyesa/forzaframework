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

package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.JSONFunction;
import org.forzaframework.web.servlet.tags.Listener;
import org.forzaframework.web.servlet.tags.form.FieldTag;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 03:11:23 PM
 * Description:
 */
public class DateTag extends FieldTag {

    private String plugins;
    private String format;

    public String getPlugins() {
        return plugins;
    }

    public void setPlugins(String plugins) {
        this.plugins = plugins;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getType() {
        return "datefield";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);
        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.put("name", getField());
        json.elementOpt("description", getDescription());
        json.elementOpt("value", getValue());
        json.elementOpt("width", getWidth());
        json.put("validateOnBlur", false);
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("disabled", disabled);
        json.elementOpt("plugins", plugins);
        json.elementOpt("renderHidden", hidden == null ? false : hidden);
        String s = getText("date.format.js");
        if(StringUtils.isNotBlank(format)){
            json.put("format", format);
        }else if(!s.equals("??date.format.js??")){
            json.put("format", s);
        }
        json.put("xtype", getType());

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