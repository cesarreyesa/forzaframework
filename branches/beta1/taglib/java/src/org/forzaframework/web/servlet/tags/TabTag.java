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

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.PanelTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * User: cesarreyes
 * Date: 01-may-2007
 * Time: 16:49:23
 * Description:
 */
public class TabTag extends PanelTag implements PanelItem {

    private String url;
    private String onActivate;
    private String onDeactivate;
    private List<Item> items = new ArrayList<Item>();

    public TabTag() {
        this.setLayout("form");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOnActivate() {
        return onActivate;
    }

    public void setOnActivate(String onActivate) {
        this.onActivate = onActivate;
    }

    public String getOnDeactivate() {
        return onDeactivate;
    }

    public void setOnDeactivate(String onDeactivate) {
        this.onDeactivate = onDeactivate;
    }

    public void addItem(PanelItem item) {
        items.add(new Item(item.toJSON()));
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                TabPanelTag parent;
                if (this.parent.getClass().getName().equals("org.apache.taglibs.standard.tag.rt.core.ForEachTag") && this.parent.getParent() instanceof TabPanelTag) {
                    parent = ((TabPanelTag) this.parent.getParent());
                } else {
                    parent = ((TabPanelTag) this.parent);
                }

                items = new ArrayList<Item>();
                topToolbar = null;
                
                if(StringUtils.isBlank(id)){
                    id = parent.getId() + "-" + parent.getItems().size();
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
                Assert.isTrue(parent instanceof TabPanelTag || (this.parent.getClass().getName().equals("org.apache.taglibs.standard.tag.rt.core.ForEachTag") && this.parent.getParent() instanceof TabPanelTag), "TabTag must be inside a TabPanelTag");
                
                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);

                pageContext.getOut().write("</div>");
                
                TabPanelTag parent;
                if (this.parent.getClass().getName().equals("org.apache.taglibs.standard.tag.rt.core.ForEachTag") && this.parent.getParent() instanceof TabPanelTag) {
                    parent = ((TabPanelTag) this.parent.getParent());
                } else {
                    parent = ((TabPanelTag) this.parent);
                }

                parent.addItem(new Item(this.toJSON()));

                id = null;
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    private Object topToolbar;
    public void setTopToolbar(Item item) {
        topToolbar = item.toJSON();
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        // Esto sirve para poder poner contenido html dentro del tag, pero no funciona, marca un error de js en ciertas circunstancias
//        json.put("contentEl", id);
        json.put("id", id);
//        json.put("el", id);
        json.put("title", getTitle() != null ? getTitle() : getText(getTitleKey()));
        json.put("layout", getLayout());
        json.elementOpt("style", getStyle());
        json.elementOpt("tbar", topToolbar);
        json.elementOpt("autoScroll", getAutoScroll());
        json.elementOpt("autoHeight", getAutoHeight());

        if(StringUtils.isNotBlank(onActivate) || StringUtils.isNotBlank(onDeactivate)){
            JSONObject listeners = new JSONObject();

            if(StringUtils.isNotBlank(onActivate)) {
                listeners.put("activate", new JSONFunction(onActivate));
            }
            if(StringUtils.isNotBlank(onDeactivate)) {
                listeners.put("deactivate", new JSONFunction(onDeactivate));
            }
            json.put("listeners", listeners);
        }

        if(items.size() > 0){
            JSONArray jsonItems = new JSONArray();
            for(PanelItem item : items){
                jsonItems.add(item.toJSON());
            }
            json.put("items", jsonItems);
        }


        return json;
    }
}
