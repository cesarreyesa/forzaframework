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

/**
 * @author cesar.reyes
 * Date: 12/11/2008
 * Time: 03:53:28 PM
 */
public class TimeTag extends FieldTag {

    private Integer increment;

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public String getType() {
        return "timefield";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.put("name", getField());
        json.elementOpt("description", getDescription());
        json.elementOpt("value", getValue());
        json.elementOpt("width", Integer.valueOf(width));
        json.elementOpt("increment", increment);
        json.put("validateOnBlur", false);
        json.elementOpt("allowBlank", allowBlank);
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