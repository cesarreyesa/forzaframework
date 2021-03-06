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
        json.elementOpt("allowBlank", getAllowBlank());
        json.elementOpt("value", getValue());
        json.elementOpt("width", getWidth());
        json.elementOpt("height", getHeight());
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("anchor", anchor);
        json.elementOpt("hideLabel", hideLabel);
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