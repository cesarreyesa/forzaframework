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
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 06-oct-2007
 * Time: 17:33:44
 * Description:
 */
public class ToolTag extends BaseTag implements PanelItem {

    private String text;
    private String textKey;
    private String handler;

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

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int doEndTag() throws JspException {
        Tag tag = findParent(PanelTag.class);
        if(tag != null){
            ((PanelTag) tag).addTool(new Item(this.toJSON()));
        }else{
        	throw new JspException("Un tooltag solo puede ir como hijo de un panel");
        }
        
        return EVAL_PAGE;
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();

        json.elementOpt("id", id);
        json.put("text", text != null ? text : getText(textKey));

        if(StringUtils.isNotBlank(handler)) {
            json.put("handler", new JSONFunction(parseHandler(handler)));
        }

        return json;
    }
    
    private String parseHandler(String handler){
    	if(handler.startsWith("function(")){
    		return handler;
    	}else{
    		return "function(){" + handler + "}";
    	}
    }
}
