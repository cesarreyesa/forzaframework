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
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.BaseTag;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 26/06/2007
 * Time: 11:50:26 AM
 * Description:
 */
public class ToolbarComboboxTag extends BaseTag {

    private String value;
    private String text;
    private String emptyText;
    private String url;
    private String valueField = "id";
    private String displayField = "name";
    private String handler;
    private String width;
    private String listWidth;
    private String reader;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
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

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getListWidth() {
        return listWidth;
    }

    public void setListWidth(String listWidth) {
        this.listWidth = listWidth;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        parent.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON(){
        final StringBuilder sb = new StringBuilder();

        JSONObject json = new JSONObject();

        json.put("xtype", "combo");
        json.elementOpt("id", id);
        json.elementOpt("text", text);
        json.elementOpt("value", value);

        sb.append("new Ext.data.Store({");
        sb.append("proxy: new Ext.data.HttpProxy(new Ext.data.Connection({url: \"").append(url).append("\"})),");
//        sb.append("reader: new Ext.data.XmlReader({");
//        sb.append("record: \"item\", totalRecords: \"totalCount\", id: \"" + valueField + "\"},");
//        sb.append("[{name: \"").append(displayField).append("\"}, {name: \"" + valueField + "\"}] )");
//        sb.append("})");

        if(reader == null || reader.equals("xml")) {
            sb.append("reader: new Ext.data.XmlReader({");
            sb.append("record: \"item\", totalRecords: \"totalCount\", id: \"" + valueField + "\"},");
            sb.append("[{name: \"").append(displayField).append("\"}, {name: \"" + valueField + "\"}] )");
            sb.append("})");
        }else if(reader.equals("json")){
            sb.append("reader: new Ext.data.JsonReader({");
            sb.append("root: \"items\", totalProperty: \"totalCount\", id: \"" + valueField + "\"},");
            sb.append("[{name: \"").append(displayField).append("\"}, {name: \"" + valueField + "\"}] )");
            sb.append("})");
        }


        json.put("store", new JSONFunction(sb.toString()));

        json.put("displayField", displayField);
        json.put("valueField", valueField);
        json.put("typeAhead", true);
        json.put("triggerAction", "all");
        json.put("emptyText", emptyText);
        json.put("selectOnFocus", true);
        json.put("width", getWidth() != null ? Integer.valueOf(getWidth()) : Integer.valueOf(200));
        json.put("listWidth", getListWidth() != null ? Integer.valueOf(getListWidth()) : getWidth() != null ? Integer.valueOf(getWidth()) : Integer.valueOf(200));
        json.put("mode", "local");
        json.put("lastQuery", "");


        if(StringUtils.isNotBlank(handler)){
            JSONObject listeners = new JSONObject();
            listeners.put("select", new JSONFunction(handler));
            json.put("listeners", listeners);
        }


        return json;
    }
}
