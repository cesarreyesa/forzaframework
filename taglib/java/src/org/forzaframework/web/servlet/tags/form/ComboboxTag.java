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
import org.forzaframework.web.servlet.tags.UpdateField;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Cesar Reyes
 * Date: 21/07/2007
 * Time: 10:33:23 AM
 * Description:
 */
public class ComboboxTag extends FieldTag {

    private String text;
    private String emptyText = "";
    private String template;
    private String url;
    private String valueField = "id";
    private String displayField = "name";
    private String handler;
    private Boolean loadOnStart;
    private Boolean selectFirstRecord;
    private Integer pageSize;
    private String store;
    private String noSelection;
    private String reader;
    private String listWidth;
    private List<Field> fields = new ArrayList<Field>();
    private List<UpdateField> updateFields = new ArrayList<UpdateField>();
    private List<Option> options = new ArrayList<Option>();

    /**
     * The {@link java.util.Collection}, {@link Map} or array of objects used to generate the inner
     * '<code>option</code>' tags.
     */
    private Object items;
    private String dataSourceType = "remote";


    /**
     * Get the value of the '<code>items</code>' attribute.
     * <p>May be a runtime expression.
     * @return items
     */
    protected Object getItems() {
        return this.items;
    }

    /**
	 * Set the {@link java.util.Collection}, {@link Map} or array of objects used to
	 * generate the inner '<code>option</code>' tags.
	 * <p>Required when wishing to render '<code>option</code>' tags from
	 * an array, {@link java.util.Collection} or {@link Map}.
	 * <p>Typically a runtime expression.
	 * @param items the items that comprise the options of this selection
	 */
	public void setItems(Object items) {
		this.items = (items != null ? items : new Object());
        this.dataSourceType = "jstl";
	}

    public String getType() {
        return "combo";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public Boolean getSelectFirstRecord() {
        return selectFirstRecord;
    }

    public void setSelectFirstRecord(Boolean selectFirstRecord) {
        this.selectFirstRecord = selectFirstRecord;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Boolean getLoadOnStart() {
        return loadOnStart;
    }

    public void setLoadOnStart(Boolean loadOnStart) {
        this.loadOnStart = loadOnStart;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getNoSelection() {
        return noSelection;
    }

    public void setNoSelection(String noSelection) {
        this.noSelection = noSelection;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getListWidth() {
        return listWidth;
    }

    public void setListWidth(String listWidth) {
        this.listWidth = listWidth;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void addField(String id, String field, String mapping) {
        this.updateFields.add(new UpdateField(id, field, mapping));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<UpdateField> getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(List<UpdateField> updateFields) {
        this.updateFields = updateFields;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public void addField(Field field){
        fields.add(field);
    }

    public void addOption(Option option){
        this.options.add(option);
        this.dataSourceType = "options";
    }

    public int doStartTag() throws JspException {
        options = new ArrayList<Option>();
        fields = new ArrayList<Field>();
        updateFields = new ArrayList<UpdateField>();
        
        return super.doStartTag();
    }

    private String getVarName(){
        return field.replace(".", "_") + "_ds";
    }


    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("xtype", "combo");
        json.put("name", field + "-text");
        json.elementOpt("id", id);
        if(getTitle() != null || getTitleKey() != null){
            json.put("fieldLabel", title != null ? title : getText(titleKey));
        }
        json.elementOpt("value", this.getValue());
        json.elementOpt("text", text);
        json.elementOpt("disabled", getDisabled());
        json.elementOpt("description", getDescription());
        json.elementOpt("selectFirstRecord", selectFirstRecord);
        if(StringUtils.isNotBlank(this.template)){
            String template = this.template;
            template = template.replace("\n", "','");
            template = "new Ext.XTemplate('" + template + "')";
            json.put("tpl", new JSONFunction(template));
        }

        if(options.size() == 0 && StringUtils.isNotBlank(url)) {
            json.put("displayField", displayField);
            json.put("valueField", valueField);
        } else {
            json.put("displayField", displayField == null ? "text" : displayField);
            json.put("valueField", valueField == null ? "value" : valueField);
        }

        json.put("store", store == null ? new JSONFunction(getVarName()) : new JSONFunction(store));

        json.put("hiddenName", field);
        json.put("typeAhead", false);
        json.put("triggerAction", "all");
        json.put("emptyText", emptyText);
        json.put("selectOnFocus", true);
        json.put("lazyInit", false);
        json.put("minChars", 1);
        json.put("queryParam", "disableQuery");
        json.put("allQuery", "");
        json.elementOpt("pageSize", pageSize);
        json.elementOpt("hideLabel", getHideLabel());
//        json.put("lazyInit", loadOnStart == null ? false : loadOnStart);
        if(StringUtils.isNotBlank(width)) json.elementOpt("width", Integer.valueOf(width));
        if(StringUtils.isNotBlank(listWidth)) json.elementOpt("listWidth", Integer.valueOf(listWidth));
        // TODO: averiguar para que sirve esto, si se pone entonces no funciona correctamente el trigger
//        json.put("lastQuery", "");
//        json.put("valueNotFoundText", "Not found");
        json.elementOpt("renderHidden", hidden == null ? false : hidden);
        json.elementOpt("allowBlank", allowBlank);

        JSONObject listeners = new JSONObject();
        if ((loadOnStart == null && StringUtils.isNotBlank(url)) || (loadOnStart != null && !loadOnStart)) {
            json.put("mode", "remote");
            String expand = "function(combo){Ext.apply(combo, {mode: 'local'}, {});}";
            listeners.put("expand", new JSONFunction(expand));
        } else {
            json.put("mode", "local");
            json.put("lastQuery", "");
        }

        if(updateFields.size() > 0){
            String formId = ((FormTag) findParent(FormTag.class)).getId();
            StringBuilder onSelectFunction = new StringBuilder();
            onSelectFunction.append("function(cmb, record, index){");
            for (UpdateField updateField : updateFields) {
                onSelectFunction.append("Ext.getCmp('").append(formId).append("').form.findField('").append(updateField.getId()).append("').setValue(record.get('").append(updateField.getField()).append("'));");
            }
            onSelectFunction.append("}");

            listeners.put("select", new JSONFunction(onSelectFunction.toString()));
        }else{
            if(StringUtils.isNotBlank(handler)){
                listeners.put("select", new JSONFunction(handler));
            }
        }

        if (!listeners.isEmpty()) {
            json.put("listeners", listeners);
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
        if(updateFields.size() > 0){
        	fields = new ArrayList<Field>();
        	for(UpdateField updateField : updateFields){
        		fields.add(new Field(updateField.getId(), updateField.getField(), updateField.getMapping()));
        	}
        }else{
        	fields = this.fields;
        }

        fields.add(new Field(valueField, valueField, null));
        fields.add(new Field(displayField, displayField, null));

        if (store == null) {
            Store store = new Store(getVarName(), loadOnStart != null && loadOnStart, valueField, displayField, fields);
            store.setUrl(url);
            store.setItems((items instanceof String ? evaluate("items", items) : items));
            store.setOptions(options);
            store.setType(dataSourceType);
            store.setNoSelection(noSelection);
            store.setReader(reader);
            store.setRemoteSort(false);
            if ("json".equals(reader)) {
                store.setItemTag("items");
            }
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
