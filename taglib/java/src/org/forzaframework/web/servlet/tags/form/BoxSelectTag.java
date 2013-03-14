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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.web.servlet.tags.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: cesar.reyes
 * Date: 8/12/2009
 * Time: 10:40:55 AM
 */
public class BoxSelectTag extends ComboboxTag {

    private String displayFieldTpl;

    public String getDisplayFieldTpl() {
        return displayFieldTpl;
    }

    public void setDisplayFieldTpl(String displayFieldTpl) {
        this.displayFieldTpl = displayFieldTpl;
    }

    public String getType() {
        return "superboxselect";
    }

    public int doStartTag() throws JspException {
        initRequestContext();
        setOptions(new ArrayList<Option>());
        setFields(new ArrayList<Field>());
        setUpdateFields(new ArrayList<UpdateField>());

        int val = IterationTag.EVAL_BODY_AGAIN;
        if (StringUtils.isBlank(getValue()) && bind) {
            this.setValue("");
            this.setPath(getField());
            if (StringUtils.isNotBlank(getCommandName())) {
                this.setPath(getCommandName() + "." + getField());
                if (getBoundValue() != null) {
                    Object boundValue = getBoundValue();
                    if (boundValue instanceof Collection) {
                        String value = "";
                        String text = "";
                        for (Object item : (Collection) boundValue) {
                            BaseEntity entityItem = (BaseEntity) item;
                            value += entityItem.getKey().toString() + ",";
                            text += entityItem.toString() + ",";
                        }
                        if (((Collection) boundValue).size() > 0) {
                            value = value.substring(0, value.length() - 1);
                            text = text.substring(0, text.length() - 1);
                        }

                        this.setValue(value);
                        this.setText(text);
                    } else {
                        this.setValue(boundValue.toString());
                    }
                }
            }
        }
        return val;
    }

    private String getVarName() {
        return field.replace(".", "_") + "_ds";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("xtype", getType());
        json.put("name", field + "-text");
        json.elementOpt("id", id);
        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.elementOpt("value", getValue());
        json.elementOpt("text", getText());
        json.elementOpt("disabled", getDisabled());
        json.elementOpt("forceFormValue", false);
        json.put("mode", "local");
        json.put("displayField", getDisplayField());
        json.elementOpt("displayFieldTpl", getDisplayFieldTpl());
        json.put("valueField", getValueField());

        json.put("store", new JSONFunction(getVarName()));

        json.put("hiddenName", field);
        json.put("emptyText", getEmptyText());
        json.put("selectOnFocus", true);
        json.put("forceSelection", true);
        json.put("resizable", true);
        json.put("anchor", getAnchor() != null ? getAnchor() : "90%");
        json.elementOpt("pageSize", getPageSize());
        json.elementOpt("width", getWidth());
        json.elementOpt("renderHidden", hidden);
        json.elementOpt("allowBlank", allowBlank);

        if (StringUtils.isNotBlank(getHandler())) {
            JSONObject listeners = new JSONObject();
            listeners.put("additem", new JSONFunction(getHandler()));
            json.put("listeners", listeners);
        }

        return json;
    }

    public String getHtmlDeclaration() {
        return null;
    }
}
