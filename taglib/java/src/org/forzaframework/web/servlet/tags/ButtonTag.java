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
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.form.FormTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 06-oct-2007
 * Time: 17:33:44
 * Description:
 */
public class ButtonTag extends BaseTag implements PanelItem {

	protected String type;
    private String scale;
    private String text;
    private String textKey;
    private Boolean enableToggle;
    private String toggleGroup;
    private String group;
    private Boolean pressed;
    private Boolean checked;
    private Boolean disabled = false;
    private String iconCls;
    private String icon;
    private Boolean hidden;
    private String handler;
    private String href;
    private Boolean allowDepress;
    private List<Item> items = new ArrayList<Item>();

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

    public Boolean getEnableToggle() {
        return enableToggle;
    }

    public void setEnableToggle(Boolean enableToggle) {
        this.enableToggle = enableToggle;
    }

    public String getToggleGroup() {
        return toggleGroup;
    }

    public void setToggleGroup(String toggleGroup) {
        this.toggleGroup = toggleGroup;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getPressed() {
        return pressed;
    }

    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
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

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getAllowDepress() {
        return allowDepress;
    }

    public void setAllowDepress(Boolean allowDepress) {
        this.allowDepress = allowDepress;
    }

    public int doEndTag() throws JspException {
//        Assert.isTrue(parent instanceof WindowTag, "ButtonTag must be inside a WindowTag");
        Tag menu = findParent(MenuTag.class);
        if(menu != null){
            ((MenuTag) menu).addItem(new Item(this.toJSON()));
        }else{
            Tag toolbar = findParent(ToolbarTag.class);
            if(toolbar != null){
                ((ToolbarTag) toolbar).addItem(new Item(this.toJSON()));
            }else{
            	Tag form = findParent(FormTag.class);
            	if(form != null){
            		((FormTag) form).addButton(new Item(this.toJSON()));
            	}else{
                    Tag window = findParent(WindowTag.class);
                    ((WindowTag) window).addButton(new Item(this.toJSON()));
            	}
            }
        }
        
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);
        json.put("text", text != null ? text : getText(textKey));
        json.elementOpt("enableToggle", enableToggle);
        if(toggleGroup != null && enableToggle != null && enableToggle) {
            json.elementOpt("allowDepress", allowDepress != null ? allowDepress : false);
        } else if (allowDepress != null) {
            json.elementOpt("allowDepress", allowDepress);
        }
        json.elementOpt("toggleGroup", toggleGroup);
        json.elementOpt("group", group);
        json.elementOpt("pressed", pressed);
        json.elementOpt("checked", checked);
        json.elementOpt("disabled", disabled);
        json.elementOpt("hidden", hidden);
        json.elementOpt("iconCls", iconCls);
        json.elementOpt("icon", icon);
        json.elementOpt("scale", scale);
        json.elementOpt("xtype", type);

        if (items.size() > 0) {
            JSONArray jsonItems = new JSONArray();
            for (PanelItem item : items) {
                jsonItems.add(item.toJSON());
            }
            json.put("items", jsonItems);
        }

        if(StringUtils.isNotBlank(handler)) {
            if(enableToggle != null && enableToggle){
                json.put("toggleHandler", new JSONFunction(parseHandler(handler)));
            }else{
                String eventName = "handler";
                if(checked != null){
                    eventName = "checkHandler";
                }
                json.put(eventName, new JSONFunction(parseHandler(handler)));
            }
        }
        json.elementOpt("href", href);
        return json;
    }
    
    private String parseHandler(String handler){
    	if(handler.startsWith("function(")){
    		return handler;
    	}else{
    		return "function(b, e){" + handler + "}";
    	}
    }
}
