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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;

/**
 * User: cesarreyes
 * Date: 15-oct-2007
 * Time: 10:06:59
 * Description:
 */
public class MenuTag extends PanelTag implements PanelItem {

    private String type;
    private String handler;
    private String scale;
    private Boolean hidden = false;
    private String text;
    private String textKey;
    private String iconCls;
    private String icon;

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextKey() {
        return textKey;
    }

    public void setTextKey(String textKey) {
        this.textKey = textKey;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTopToolbar(Item item) {
    }

    public void doInitBody() throws JspException {
        setItems(new ArrayList<PanelItem>());
    }

    public int doEndTag() throws JspException {
//        Assert.isTrue(parent instanceof ToolbarTag, "Menu must be inside a toolbarTag");
        Tag menu = findParent(MenuTag.class);
        if(menu != null){
            ((MenuTag) menu).addItem(new Item(this.toJSON()));
        }else{
            Tag toolbar = findParent(ToolbarTag.class);
            if(toolbar != null){
                ((ToolbarTag) toolbar).addItem(new Item(this.toJSON()));
            }else if(parent instanceof GridTag && type.equals("contextmenu")){
                ((GridTag) parent).setContextMenu(new Item(this.toJSON()));
            }else if(parent instanceof DataViewTag && type.equals("contextmenu")){
                ((DataViewTag) parent).setContextMenu(new Item(this.toJSON()));
            }
        }

        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);

        if (getItems().size() > 0) {
            JSONArray jsonItems = new JSONArray();
            for (PanelItem item : getItems()) {
                jsonItems.add(item.toJSON());
            }
            json.put("items", jsonItems);
        }

        JSONObject menu = new JSONObject();

        menu.put("text", text != null ? text : getText(textKey));
        menu.elementOpt("hidden", hidden);
        menu.elementOpt("iconCls", iconCls);
        menu.elementOpt("icon", icon);
        json.elementOpt("scale", scale);

        if(StringUtils.isNotBlank(handler)) {
            menu.put("menu", new JSONFunction("new Ext.menu.Menu(" + json.toString(2) + ")"));
            menu.put("handler", new JSONFunction(parseHandler(handler)));
        	return new JSONFunction("new Ext.Toolbar.SplitButton(" + menu.toString() + ")");
        }else{
            menu.put("menu", new JSONFunction("new Ext.menu.Menu(" + json.toString(2) + ")"));
            return menu;        	
        }
    }

    private String parseHandler(String handler){
    	if(handler.startsWith("function(")){
    		return handler;
    	}else{
    		return "function(){" + handler + "}";
    	}
    }
}
