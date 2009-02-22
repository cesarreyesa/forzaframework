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
import org.springframework.util.Assert;
import org.forzaframework.web.servlet.tags.BaseBodyTag;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 21/12/2006
 * Time: 03:30:14 PM
 * Description:
 */
public class ToolbarTag extends BaseBodyTag {

    private String type;
    private String defaultText;
    private String align;
    private List<Item> items = new ArrayList<Item>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public void addItem(Item item){
        items.add(item);
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();
                items = new ArrayList<Item>();

                if (!(parent instanceof PanelTag)) {
                    if(getId() == null && getParent() instanceof BaseBodyTag){
                        BaseBodyTag tag = (BaseBodyTag) getParent();
                        id = tag.getId();
                    }

                    sb.append("<div id=\"").append(id).append("-tb\"></div>");
                }

                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();
                Tag parent = findParent(PanelTag.class);
                if (parent != null) {
                    Assert.notNull(type, "A Toolbar inside a PanelTag must have the [type] attribute");

                    if(type.equals("top")){
                        ((PanelTag) parent).setTopToolbar(new Item(this.toJSON()));
                    }else if(type.equals("bottom")){
                    	((PanelTag) parent).setBottomToolbar(new Item(this.toJSON()));
                    }                    
                }

                id = null;
                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public Object toJSON(){
        JSONArray items = new JSONArray();

        for(Item item : this.items) {
            items.add(item.toJSON());            
        }

        return items;
    }

}
