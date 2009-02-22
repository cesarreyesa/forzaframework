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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.form.Field;
import org.forzaframework.web.servlet.tags.form.Option;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 1:00:14
 * Description:
 */
public class ComboboxColumnTag extends ColumnTag {

    private String url;
    private String displayField = "name";
    private String valueField = "id";
    private String idProperty;
    private List<Option> options = new ArrayList<Option>();

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

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public void addOption(Option option){
        this.options.add(option);
    }

    public int doStartTag() throws JspException {
        options = new ArrayList<Option>();
        
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
        Field field = new Field();

        field.setId(id);
        field.setField(this.getField());
        field.setTitle(title != null ? title : getText(titleKey));
        field.setWidth(width);
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setType("string");
        field.setEditorJson(this.toJSON());
        field.setXtype("combo");
        field.setDisplayField(displayField);
        tag.addStoreDeclaration(getStoreDeclaration());


        tag.addField(field);
        return EVAL_PAGE;
    }

    private String getVarName(){
        return field.replace(".", "_") + "_ds";
    }

    public Map<String, Object> getStoreDeclaration(){
        StringBuilder sb = new StringBuilder();
        if(options.size() == 0 && StringUtils.isNotBlank(url)){

            sb.append("var ").append(getVarName()).append(" = new Ext.data.Store({");
            String id = this.id;
            if(StringUtils.isBlank(this.id)){
                EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
                id = tag.getId() + "_" + field + "_cmb";
            }
            sb.append("id:'").append(id).append("_ds',");
            sb.append("proxy: new Ext.data.HttpProxy(new Ext.data.Connection({url: \"").append(url).append("\"})),");
            sb.append("reader: new Ext.data.XmlReader({");
            sb.append("record: \"item\", totalRecords: \"totalCount\", id: \"").append(valueField).append("\"},");

            JSONArray record = new JSONArray();
            JSONObject field = new JSONObject();
            field.put("name", valueField);
            record.add(field);
            field = new JSONObject();
            field.put("name", displayField);
            record.add(field);
            sb.append(record.toString()).append(")");

            sb.append("});");
        }else{
            sb.append("var ").append(getVarName()).append(" = new Ext.data.SimpleStore(");
            JSONObject store = new JSONObject();
            String id = this.id;
            if(StringUtils.isBlank(this.id)){
                EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
                id = tag.getId() + "_" + field + "_cmb_ds";
            }
            store.put("id", id);
            JSONArray fields = new JSONArray();
            fields.add("value");
            fields.add("text");
            store.put("fields", fields);
            JSONArray data = new JSONArray();
            for(Option option : options){
                JSONArray optionArray = new JSONArray();
                optionArray.add(option.getValue());
                optionArray.add(option.getText());
                data.add(optionArray);
            }
            store.put("data", data);
            sb.append(store.toString()).append(");");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", getVarName());
        map.put("js", sb.toString());
        map.put("load", options.size() == 0 && StringUtils.isNotBlank(url));

        return map;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("xtype", "combo");
        json.put("name", this.getField());
        json.elementOpt("id", this.id);
        String id = this.id;
        if(StringUtils.isBlank(this.id)){
            EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
            id = tag.getId() + "-" + field + "-cmb";
        }
        json.put("fieldLabel", title != null ? title : getText(titleKey));

//        StringBuilder sb = new StringBuilder();
//        sb.append("new Ext.data.Store({");
//        sb.append("id:'").append(id).append("-ds',");
//        sb.append("proxy: new Ext.data.HttpProxy(new Ext.data.Connection({url: \"").append(url).append("\"})),");
//        sb.append("reader: new Ext.data.XmlReader({");
//        sb.append("record: \"item\", totalRecords: \"totalCount\", id: \"id\"},");
//        sb.append("[{name: \"").append(displayField).append("\"}, {name: \"id\"}] )");
//        sb.append("})");
//
//        json.put("store", new JSONFunction(sb.toString()));
        json.put("store", new JSONFunction(getVarName()));

        if(options.size() == 0 && StringUtils.isNotBlank(url)){
            json.put("displayField", displayField);
            json.put("valueField", valueField);
        }else{
            json.put("displayField", "text");
            json.put("valueField", "value");

            json.put("mode", "local");
        }

        json.put("typeAhead", true);
        json.put("triggerAction", "all");
        json.put("emptyText", "");
        json.put("selectOnFocus", true);
        json.put("lazyInit", false);
        json.put("width", 200);

//        if(StringUtils.isNotBlank(handler)){
//            JSONObject listeners = new JSONObject();
//            listeners.put("select", new JSONFunction(handler));
//            json.put("listeners", listeners);
//        }

        return new JSONFunction("new Ext.form.ComboBox(" + json.toString() + ")");
    }

    public String getHtmlDeclaration() {
        return null;
    }

}
