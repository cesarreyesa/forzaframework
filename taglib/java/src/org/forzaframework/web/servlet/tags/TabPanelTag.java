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
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.form.FormTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * User: cesarreyes
 * Date: 01-may-2007
 * Time: 12:39:00
 * Description:
 */
public class TabPanelTag extends PanelTag implements PanelItem {

    private Boolean defaultAutoHeight;
    private Boolean defaultAutoScroll;
    private String defaultStyle;
    private Boolean enableTabScroll;
    private Boolean tabPosition;
    private Boolean forceLayout;

    public Boolean getDefaultAutoHeight() {
        return defaultAutoHeight;
    }

    public void setDefaultAutoHeight(Boolean defaultAutoHeight) {
        this.defaultAutoHeight = defaultAutoHeight;
    }

    public Boolean getDefaultAutoScroll() {
        return defaultAutoScroll;
    }

    public void setDefaultAutoScroll(Boolean defaultAutoScroll) {
        this.defaultAutoScroll = defaultAutoScroll;
    }

    public String getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(String defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public Boolean getEnableTabScroll() {
        return enableTabScroll != null  ? enableTabScroll : false;
    }

    public void setEnableTabScroll(Boolean enableTabScroll) {
        this.enableTabScroll = enableTabScroll;
    }

    public void setTopToolbar(Item item) {
    }

    public Boolean getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(Boolean tabPosition) {
        this.tabPosition = tabPosition;
    }

    public Boolean getForceLayout() {
        return forceLayout;
    }

    public void setForceLayout(Boolean forceLayout) {
        this.forceLayout = forceLayout;
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                setItems(new ArrayList<PanelItem>());
                
                if(StringUtils.isBlank(id) && getParent() instanceof PanelTag){
                    // TODO: Revisar si el parent tiene un id, si no, lanzar exception
                    id = ((BaseBodyTag) getParent()).getId() + "-tabs";
                }

                pageContext.getOut().write("<div id=\"" + id + "\">");
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);

                StringBuilder sb = new StringBuilder();
                sb.append("</div>");

                if(parent instanceof PanelTag){
                    ((PanelTag) parent).addItem(new Item(this.toJSON()));
                }else{
                    sb.append("<script type=\"text/javascript\">\n");
                    sb.append("Ext.onReady(function(){\n");
                    sb.append("var tabPanel = new Ext.TabPanel(\n");
                    sb.append(((JSONObject) toJSON()).toString(2)).append(");");
//                    sb.append("tabPanel.render();");
                    sb.append("});");

                    sb.append("</script>\n");
                }

                pageContext.getOut().write(sb.toString());
                id = null;
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("id", id);
        if(!(parent instanceof PanelTag)){
            json.put("renderTo", id);
        }
//        json.put("el", id);
        json.put("xtype", "tabpanel");
        json.put("plain", true);
        json.put("activeTab", 0);
        json.put("border", false);
        json.put("deferredRender", false);
        if(tabPosition != null) {
            json.put("tabPosition", tabPosition);

        }
        if(forceLayout != null) {
            json.put("forceLayout", forceLayout);

        }
        json.elementOpt("height", getHeight());
        json.elementOpt("region", getRegion());
        json.elementOpt("enableTabScroll", getEnableTabScroll());

        String anchor = getAnchor();
        if(StringUtils.isBlank(anchor) && parent instanceof FormTag){
            anchor = "100% -65";
        }
        json.elementOpt("anchor", anchor);

        JSONObject defaults = new JSONObject();
//        defaults.put("bodyStyle", "padding:10px");
        defaults.elementOpt("autoHeight", defaultAutoHeight);
        defaults.elementOpt("autoScroll", defaultAutoScroll);
        defaults.elementOpt("style", defaultStyle);
        json.put("defaults", defaults);

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
