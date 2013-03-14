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

import javax.servlet.jsp.JspException;

/**
 * Created with IntelliJ IDEA.
 * User: gabriel.chulim
 * Date: 15/05/12
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToolbarSearchTag extends BaseTag implements PanelItem {
    private Integer width;
    private String handler;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        parent.addItem(new Item(this.toJSON()));
        return EVAL_PAGE;
    }


    public Object toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("width", width);
        json.put("onTrigger2Click", new JSONFunction(handler));

        return new JSONFunction("new Ext.ux.SearchField(" + json.toString() + ")");
    }
}
