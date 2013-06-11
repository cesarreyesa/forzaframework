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

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.form.FieldTag;
import org.forzaframework.web.servlet.tags.JSONFunction;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 05:09:25 PM
 * Description:
 */
public class CheckboxTag extends FieldTag {

    private Boolean checked;
    public String onCheck;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getOnCheck() {
        return onCheck;
    }

    public void setOnCheck(String onCheck) {
        this.onCheck = onCheck;
    }

    public String getType() {
        return "checkbox";
    }

    public JSON toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);
        json.put("boxLabel", title != null ? title : getText(titleKey));        
//        json.put("hideLabel", true);
        json.put("labelSeparator", "");
        json.put("name", this.getField());
//        json.elementOpt("mapping", this.getMapping());
        json.elementOpt("description", getDescription());
        json.elementOpt("inputValue", this.getValue());
        json.elementOpt("checked", this.getChecked());
        json.elementOpt("disabled", this.getDisabled());
        json.elementOpt("width", getWidth());
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("anchor", anchor);
        json.elementOpt("hideLabel", getHideLabel());
        json.put("validateOnBlur", false);
        json.put("xtype", getType());

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
        sb.append("\" type=\"checkbox\">");

        return sb.toString();
    }
}
