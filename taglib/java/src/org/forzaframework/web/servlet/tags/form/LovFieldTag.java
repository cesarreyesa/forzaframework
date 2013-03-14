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
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.JSONFunction;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.UpdateField;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cesarreyes
 * Date: Mar 18, 2008
 * Time: 10:21:55 PM
 */
public class LovFieldTag extends ComboboxTag {

    private String viewType;
    private String lovTitle;
    private Integer lovWidth;
    private Integer lovHeight;
    private Integer maxItem;
    private Integer minItem;
    private Boolean multiSelect;
    private Boolean textarea;

    public String getLovTitle() {
        return lovTitle;
    }

    public void setLovTitle(String lovTitle) {
        this.lovTitle = lovTitle;
    }

    public Integer getLovWidth() {
        return lovWidth;
    }

    public void setLovWidth(Integer lovWidth) {
        this.lovWidth = lovWidth;
    }

    public Integer getLovHeight() {
        return lovHeight;
    }

    public void setLovHeight(Integer lovHeight) {
        this.lovHeight = lovHeight;
    }

    public Integer getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(Integer maxItem) {
        this.maxItem = maxItem;
    }

    public Integer getMinItem() {
        return minItem;
    }

    public void setMinItem(Integer minItem) {
        this.minItem = minItem;
    }

    public Boolean getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Boolean getTextarea() {
        return textarea;
    }

    public void setTextarea(Boolean textarea) {
        this.textarea = textarea;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getType() {
        return "xlovfield";
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

        json.put("xtype", "xlovfield");
        json.put("name", field + "-text");
        json.elementOpt("id", id);
        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.elementOpt("value", getValue());
        json.elementOpt("text", getText());

        if (getOptions().size() == 0 && getItems() == null) {
            json.put("displayField", getDisplayField());
            json.put("valueField", getValueField());
        } else {
            json.put("displayField", "text");
            json.put("valueField", "value");

            json.put("mode", "local");
        }

        json.elementOpt("lovTitle", lovTitle);
        json.elementOpt("lovWidth", lovWidth);
        json.elementOpt("lovHeight", lovHeight);
        json.elementOpt("maxItem", maxItem);
        json.elementOpt("minItem", minItem);
        json.elementOpt("multiSelect", multiSelect);
        json.elementOpt("textarea", textarea);
        json.elementOpt("allowBlank", allowBlank);

        json.put("hiddenName", field);
        json.put("typeAhead", true);
        json.put("triggerAction", "all");
        json.put("emptyText", "");
        json.put("selectOnFocus", true);
        json.put("lazyInit", false);
        json.elementOpt("pageSize", getPageSize());
//        json.put("lazyInit", loadOnStart == null ? false : loadOnStart);
        json.elementOpt("width", getWidth());
        // TODO: averiguar para que sirve esto, si se pone entonces no funciona correctamente el trigger
//        json.put("lastQuery", "");
//        json.put("valueNotFoundText", "Not found");
        json.elementOpt("renderHidden", hidden);

        if (viewType.equals("grid")) {
            JSONObject gridConfig = new JSONObject();
            gridConfig.put("store", getStore() == null ? new JSONFunction(getVarName()) : new JSONFunction(getStore()));
            JSONArray columnsModel = new JSONArray();
            for (Field field : getFields()) {
                JSONObject fieldJson = new JSONObject();
                fieldJson.elementOpt("header", field.getTitle());
                fieldJson.put("dataIndex", field.getField());
                fieldJson.elementOpt("hidden", field.getHidden());
                fieldJson.elementOpt("width", field.getWidth());
                fieldJson.elementOpt("sortable", field.getSortable() != null ? field.getSortable() : false);
                if (field.getRendererFunction() != null) {
                    fieldJson.put("renderer", new JSONFunction(field.getRendererFunction()));
                }
                columnsModel.add(fieldJson);
            }
            gridConfig.put("columns", columnsModel);
            json.put("view", new JSONFunction("new Ext.grid.GridPanel(" + gridConfig.toString() + ")"));
        }

        if (getUpdateFields().size() > 0) {
            String formId = ((FormTag) findParent(FormTag.class)).getId();
            StringBuilder onSelectFunction = new StringBuilder();
            onSelectFunction.append("function(cmb, record, index){");
            for (UpdateField updateField : getUpdateFields()) {
                onSelectFunction.append("Ext.getCmp('").append(formId).append("').form.findField('").append(updateField.getId()).append("').setValue(record.get('").append(updateField.getField()).append("'));");
            }
            onSelectFunction.append("}");

            JSONObject listeners = new JSONObject();
            listeners.put("select", new JSONFunction(onSelectFunction.toString()));
            json.put("listeners", listeners);
        } else {
            if (StringUtils.isNotBlank(getHandler())) {
                JSONObject listeners = new JSONObject();
                listeners.put("select", new JSONFunction(getHandler()));
                json.put("listeners", listeners);
//                json.put("onSelectFunction", new JSONFunction(getHandler()));
//                
//                StringBuilder onclick = new StringBuilder("function(e){")
//                        //Colocamos las instrucciones adicionales
//                        .append("if (!this.isStoreLoaded) {")
//                        .append("this.view.store.load();")
//                        .append("this.isStoreLoaded = true;")
//                        .append("} else if (this.alwaysLoadStore === true) {")
//                        .append("this.view.store.reload();}")
//                        .append("this.createWindow();")
//                        .append("this.window.setPagePosition(e.xy[0] + 16, e.xy[1] + 16);")
//                        .append("this.window.show();")
//                        .append("}");
//                json.put("onTriggerClick", new JSONFunction(onclick.toString()));
            }
        }


        return json;
    }

    public int doEndTag() throws JspException {

        Field field = new Field();
        field.setId(id);
        field.setField(this.field);
        field.setMapping(mapping);
        field.setValue(value);

        FormTag form = (FormTag) findParent(FormTag.class);
        form.addField(field);

        List<Field> fields;
        if (getUpdateFields().size() > 0) {
            fields = this.getFields();
            for (UpdateField updateField : getUpdateFields()) {
                fields.add(new Field(updateField.getId(), updateField.getField(), updateField.getMapping()));
            }
        } else {
            fields = this.getFields();
        }
        if (getStore() == null) {
            Store store = new Store(getVarName(), getLoadOnStart() == null ? false : getLoadOnStart(), getValueField(), getDisplayField(), fields);
            store.setUrl(getUrl());
            store.setItems((getItems() instanceof String ? evaluate("items", getItems()) : getItems()));
            store.setOptions(getOptions());
            form.addStoreDeclaration(store);
        }

        PanelTag parent = (PanelTag) findParent(PanelTag.class);
        parent.addItem(new Item(this.toJSON()));

        value = null;
        doFinally();

        return EVAL_PAGE;
    }

    public String getHtmlDeclaration() {
        return null;
    }

}
