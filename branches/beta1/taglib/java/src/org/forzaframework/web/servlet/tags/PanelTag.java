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
import org.forzaframework.web.servlet.tags.BaseBodyTag;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Cesar Reyes
 * Date: 31/08/2007
 * Time: 04:32:29 PM
 */
public abstract class PanelTag extends BaseBodyTag {

	private String xtype = "panel";
    private String renderTo;
	private String contentEl;
	private String columnWidth;
    private String bodyBorder;
    private String margins;
    private String cmargins;
    private String layout;
    private String style;
    private String height;
    private String width;
    private Boolean collapsible;
    private Boolean collapsed;
    private Boolean autoHeight;
    private Boolean autoScroll;
    private Boolean titlebar;
    private String region;
    private Boolean frame;
    private String title;
    private String titleKey;
    private String anchor;
    private Boolean border;
    private Boolean split;
    private String replacePanel;
    private List<PanelItem> items = new ArrayList<PanelItem>();
    private List<PanelItem> tools = new ArrayList<PanelItem>();

    public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

    public String getRenderTo() {
        return renderTo;
    }

    public void setRenderTo(String renderTo) {
        this.renderTo = renderTo;
    }

    public String getContentEl() {
		return contentEl;
	}

	public void setContentEl(String contentEl) {
		this.contentEl = contentEl;
	}

	public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}

	public String getReplacePanel() {
		return replacePanel;
	}

	public void setReplacePanel(String replacePanel) {
		this.replacePanel = replacePanel;
	}

	public String getBodyBorder() {
        return bodyBorder;
    }

    public void setBodyBorder(String bodyBorder) {
        this.bodyBorder = bodyBorder;
    }

    public String getMargins() {
        return margins;
    }

    public void setMargins(String margins) {
        this.margins = margins;
    }

    public String getCmargins() {
		return cmargins;
	}

	public void setCmargins(String cmargins) {
		this.cmargins = cmargins;
	}

	public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Boolean getCollapsible() {
        return collapsible;
    }

    public void setCollapsible(Boolean collapsible) {
        this.collapsible = collapsible;
    }

    public Boolean getCollapsed() {
        return collapsed;
    }

    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

    public Boolean getAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(Boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public Boolean getAutoScroll() {
        return autoScroll;
    }

    public void setAutoScroll(Boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getFrame() {
        return frame;
    }

    public void setFrame(Boolean frame) {
        this.frame = frame;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Boolean getBorder() {
        return border;
    }

    public void setBorder(Boolean border) {
        this.border = border;
    }

    public Boolean getSplit() {
        return split;
    }

    public void setSplit(Boolean split) {
        this.split = split;
    }

    public Boolean getTitlebar() {
        return titlebar;
    }

    public void setTitlebar(Boolean titlebar) {
        this.titlebar = titlebar;
    }

    public List<PanelItem> getItems() {
        return items;
    }

    public void setItems(List<PanelItem> items) {
        this.items = items;
    }

    public void addItem(PanelItem item){
        this.items.add(item);
    }

    public List<PanelItem> getTools(){
    	return tools;
    }
    
    public void setTools(List<PanelItem> tools){
    	this.tools = tools;
    }
    
    public void addTool(PanelItem item){
    	this.tools.add(item);
    }
    
    private Object topToolbar;
    public void setTopToolbar(Item item) {
    	if(item != null){
            topToolbar = item.toJSON();
    	}
    }
    
    public Object getTopToolbar(){
    	return topToolbar;
    }

    private Object bottomToolbar;
    public void setBottomToolbar(Item item) {
        bottomToolbar = item.toJSON();
    }
    
    public Object getBottomToolbar(){
    	return bottomToolbar;
    }
    
    private JSONObject config = new JSONObject();
    
    public void addConfigElement(String key, Object value){
    	this.config.element(key, value);
    }

    public void addConfigElementOpt(String key, Object value){
    	this.config.elementOpt(key, value);
    }
    
    public void addRemoveElement(String key){
    	this.config.remove(key);
    }

    public JSONObject getConfig(){
    	return config;
    }
    
    public void prepareConfig(){
        config.elementOpt("id", id);
        config.elementOpt("contentEl", contentEl);
        
        config.put("xtype", getXtype());
        config.elementOpt("region", getRegion());

        if(getTitle() != null || getTitleKey() != null) {
        	config.put("title", getTitle() != null ? getTitle() : getText(getTitleKey()));
        }
                
        config.elementOpt("margins", getMargins());
        config.elementOpt("cmargins", getCmargins());
        config.elementOpt("titlebar", getTitlebar());
        config.elementOpt("titleCollapse", true);
        config.elementOpt("collapsed", getCollapsed());
        config.elementOpt("collapsible", getCollapsible());
        config.elementOpt("autoScroll", getAutoScroll());
        config.elementOpt("autoHeight", getAutoHeight());
        config.elementOpt("split", getSplit());
        config.elementOpt("border", getBorder());
        config.elementOpt("bodyBorder", getBodyBorder());
        config.elementOpt("bodyStyle", getStyle());
        
        config.elementOpt("tbar", topToolbar);
        config.elementOpt("bbar", bottomToolbar);
        
        config.elementOpt("layout", getLayout());
        config.elementOpt("frame", getFrame());
        
        if(StringUtils.isNotBlank(columnWidth)){
        	config.put("columnWidth", Double.valueOf(columnWidth));
        }

        if(getHeight() != null){
            if(getHeight().contains("%")){
                // compatibilidad con el custom layout ux.row
                if(parent != null && parent instanceof PanelTag && ((PanelTag) parent).getLayout().equals("ux.row"))
                    config.put("rowHeight", Integer.parseInt(getHeight().substring(1)) / 100);
                else
                    config.put("height", getHeight());
            }else{
                config.elementOpt("height", Integer.valueOf(getHeight()));
            }
        }

        if(getWidth() != null){
            if(getWidth().contains("%")){
                config.put("width", getWidth());
            }else{
                config.elementOpt("width", Integer.valueOf(getWidth()));
            }
        }

        if(getItems().size() > 0){
            JSONArray jsonItems = new JSONArray();

            for(PanelItem item : getItems()){
                jsonItems.add(item.toJSON());
            }
            config.put("items", jsonItems);
        }
    	
        if(getTools().size() > 0){
            JSONArray jsonItems = new JSONArray();

            for(PanelItem item : getTools()){
                jsonItems.add(item.toJSON());
            }
            config.put("tools", jsonItems);
        }
    }

    public Object toJSON(){
    	prepareConfig();
        return config;    
    }

	public void doInitBody() throws JspException {
        setItems(new ArrayList<PanelItem>());
        setTools(new ArrayList<PanelItem>());
        config = new JSONObject();
        topToolbar = null;
        bottomToolbar = null;
    }
    
	public String prepareOnReadyFunction(){
		StringBuilder sb = new StringBuilder();
        sb.append("var panel = new Ext.Panel(").append(this.toJSON().toString()).append(");");
        if(StringUtils.isNotBlank(getReplacePanel())){
            sb.append(getReplacePanel()).append(".replacePanel(panel);\n");
        }else if(StringUtils.isNotBlank(renderTo)){
            sb.append("panel.render('").append(renderTo).append("');\n");
        }
        return sb.toString();
	}
	
	public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();

                if(StringUtils.isNotBlank(replacePanel) || StringUtils.isNotBlank(renderTo)){
                    sb.append("<script type=\"text/javascript\">\n");
                    sb.append("Ext.onReady(function(){\n");
                    sb.append(prepareOnReadyFunction());
                    sb.append("});");
                    sb.append("</script>\n");
                }

                Tag panel = findParent(PanelTag.class);
                if(panel != null){
                    ((PanelTag) panel).addItem(new Item(this.toJSON()));
                }

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
