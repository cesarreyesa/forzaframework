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
import net.sf.json.JSONArray;

import javax.servlet.jsp.JspException;
import java.util.List;
import java.util.ArrayList;

import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: cesarreyes
 * Date: 27-feb-2008
 * Time: 11:59:44
 */
public class RadioGroupTag extends FieldTag {

    private Integer columns;
    private Boolean vertical;
    private Boolean horizontal = true;

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Boolean getVertical() {
        return vertical;
    }

    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    public Boolean getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }

    private List<PanelItem> items = new ArrayList<PanelItem>();

    public String getType() {
        return "radiogroup";
    }

    public void doInitBody() throws JspException {
        if(this.bodyContent != null){
            items = new ArrayList<PanelItem>();
        }
    }

    public JSON toJSON() {
        JSONObject json = new JSONObject();

        String title = this.getTitleKey() != null ? getText(this.getTitleKey()) : this.getTitle();
        json.put("fieldLabel", title);

        if(StringUtils.isBlank(title)){
            json.put("labelSeparator", "");
        }

        json.put("name", this.getField());
        json.elementOpt("id", id);
        json.elementOpt("inputValue", this.getValue());
        json.elementOpt("disabled", this.getDisabled());
        json.elementOpt("width", getWidth());
        json.elementOpt("columns", columns);
        json.elementOpt("vertical", vertical);
        json.elementOpt("horizontal", horizontal);
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("renderHidden", hidden == null ? false : hidden);
        json.put("xtype", getType());

        JSONArray radios = new JSONArray();
        for(PanelItem item : items){
            radios.add(item.toJSON());
        }
        json.put("items", radios);

        return json;
    }

    public String getHtmlDeclaration(){
        StringBuilder sb = new StringBuilder();

        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"radio\">");

        return sb.toString();
    }

    public void setTopToolbar(Item item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addItem(PanelItem item) {
        this.items.add(item);
    }
}
