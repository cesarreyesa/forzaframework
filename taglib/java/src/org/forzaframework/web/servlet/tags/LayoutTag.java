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
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 20/06/2007
 * Time: 10:47:45 AM
 * Description:
 */
public class LayoutTag extends PanelTag {

    private List<PanelItem> panels = new ArrayList<PanelItem>();

    public List<PanelItem> getPanels() {
        return panels;
    }

    public void setTopToolbar(Item item) {
    }

    public void addItem(PanelItem panel){
        this.panels.add(panel);
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                panels = new ArrayList<PanelItem>();

                StringBuffer sb = new StringBuffer();

                sb.append("<div id=\"").append(id).append("\">");

                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuffer sb = new StringBuffer();

                sb.append("</div>");
                sb.append("<script type=\"text/javascript\">");
                sb.append("var panel = new Ext.Panel(");
                JSONObject json = new JSONObject();

                json.put("layout", "border");
                json.elementOpt("border", getBorder());
                json.put("el", id);
                json.put("id", id);

                JSONArray items = new JSONArray();          

                for(PanelItem item : panels){
                    items.add(item.toJSON());
                }

                json.put("items", items);

                sb.append(json.toString()).append(");");

                sb.append(getReplacePanel()).append(".replacePanel(panel);");
                sb.append("</script>");

                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                pageContext.getOut().write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }
}
