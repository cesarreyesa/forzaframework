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
