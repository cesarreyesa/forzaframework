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

import javax.servlet.jsp.JspException;
import java.util.ArrayList;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;

/**
 * User: Cesar Reyes
 * Date: 12/06/2007
 * Time: 12:04:55 PM
 * Description:
 */
public class FieldsetTag extends PanelTag {

    private Boolean hideLabels;
    private Boolean checkboxToggle;
    private String labelAlign;
    private String description;

    public FieldsetTag() {
        setAutoHeight(true);
        setLayout("form");
    }

    public Boolean getCheckboxToggle() {
        return checkboxToggle;
    }

    public void setCheckboxToggle(Boolean checkboxToggle) {
        this.checkboxToggle = checkboxToggle;
    }

    public Boolean getHideLabels() {
        return hideLabels;
    }

    public void setHideLabels(Boolean hideLabels) {
        this.hideLabels = hideLabels;
    }

    public String getLabelAlign() {
        return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
        this.labelAlign = labelAlign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopToolbar(Item item) {
    }

    public void doInitBody() throws JspException {
        setItems(new ArrayList<PanelItem>());
    }

    public int doEndTag() throws JspException {
        ((PanelTag) getParent()).addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("xtype", "fieldset");
        if(getTitle() != null || getTitleKey() != null){
            json.put("title", getTitle() != null ? getTitle() : getText(getTitleKey()));
        }
        json.elementOpt("border", getBorder());
        json.elementOpt("anchor", getAnchor());
        json.elementOpt("hideLabels", hideLabels);
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("collapsible", getCollapsible());
        json.elementOpt("collapsed", getCollapsed());
        json.elementOpt("layout", getLayout());
        json.elementOpt("description", description);

        if(getHeight() != null){
            if(getHeight().contains("%")){
            	json.put("height", getHeight());
            }else{
            	json.elementOpt("height", Integer.valueOf(getHeight()));
            }
        }

        if(getWidth() != null){
            if(getWidth().contains("%")){
            	json.put("width", getWidth());
            }else{
            	json.elementOpt("width", Integer.valueOf(getWidth()));
            }
        }

        json.elementOpt("labelAlign", getLabelAlign());
        json.elementOpt("checkboxToggle", checkboxToggle);

        if(getItems().size() > 0){
            JSONArray jsonItems = new JSONArray();

            for(PanelItem item : getItems()){
                jsonItems.add(item.toJSON());
            }
            json.put("items", jsonItems);
        }

        return json;
    }
}
