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
import org.forzaframework.web.servlet.tags.form.*;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: cesarreyes
 * Date: 24-dic-2006
 * Time: 11:48:17
 * Description:
 */
public class GridTag extends PanelTag implements PanelItem {

    private String defaultSort;
    private String defaultSortDir = "asc";
    private Boolean displayPagingInfo = false;
	private String reader;
	private Boolean stripeRows;
    private String bodyField;
    private Boolean forceFit;
    private Boolean loadMask;
    private Integer pageSize = 10;
    private String itemTag = "item";
    private String url = "";
    private String dataSourceType = "remote";
    private String idProperty = "id";
    private String onSelectionChange;
    private String onRowDblClick;
    private Boolean enableDragDrop = false;
    private Boolean enableDrop = false;
    private Boolean enableColumnsLock = false;
    private String onDrop;
    private String ddGroup = "GridDD";
    private String dropGroup = "GridDD";
    private Boolean autoSizeColumns = false;
    private Boolean enablePagination = true;
    private Boolean initialize = true;
    private Boolean loadOnStart = true;
    private Boolean singleSelect = true;
    private List<Field> fields = new ArrayList<Field>();
    private Boolean enableFilter = false;
    private String groupField;

    public Boolean getDisplayPagingInfo() {
		return displayPagingInfo;
	}

	public void setDisplayPagingInfo(Boolean displayPagingInfo) {
		this.displayPagingInfo = displayPagingInfo;
	}

	public Boolean getStripeRows() {
		return stripeRows;
	}

	public void setStripeRows(Boolean stripeRows) {
		this.stripeRows = stripeRows;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}
    
    /**
     * The {@link java.util.Collection}, {@link Map} or array of objects used to generate the inner
     * '<code>option</code>' tags.
     */
    private Object rowItems;

    /**
	 * Set the {@link java.util.Collection}, {@link Map} or array of objects used to
	 * generate the inner '<code>option</code>' tags.
	 * <p>Required when wishing to render '<code>option</code>' tags from
	 * an array, {@link java.util.Collection} or {@link Map}.
	 * <p>Typically a runtime expression.
	 * @param rowItems the items that comprise the options of this selection
	 */
	public void setRowItems(Object rowItems) {
		this.rowItems = (rowItems != null ? rowItems : new Object());
        this.dataSourceType = "jstl";
	}

	public String getBodyField() {
        return bodyField;
    }

    public void setBodyField(String bodyField) {
        this.bodyField = bodyField;
    }

    public Boolean getForceFit() {
        return forceFit;
    }

    public void setForceFit(Boolean forceFit) {
        this.forceFit = forceFit;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getOnSelectionChange() {
        return onSelectionChange;
    }

    public Boolean getEnableFilter() {
        return enableFilter;
    }

    public void setEnableFilter(Boolean enableFilter) {
        this.enableFilter = enableFilter;
    }

    public void setOnSelectionChange(String onSelectionChange) {
        this.onSelectionChange = onSelectionChange;
    }

    public String getOnRowDblClick() {
        return onRowDblClick;
    }

    public void setOnRowDblClick(String onRowDblClick) {
        this.onRowDblClick = onRowDblClick;
    }

    public Boolean getEnableDragDrop() {
        return enableDragDrop;
    }

    public void setEnableDragDrop(Boolean enableDragDrop) {
        this.enableDragDrop = enableDragDrop;
    }

    public Boolean getEnableDrop() {
        return enableDrop;
    }

    public void setEnableDrop(Boolean enableDrop) {
        this.enableDrop = enableDrop;
    }

    public String getOnDrop() {
        return onDrop;
    }

    public void setOnDrop(String onDrop) {
        this.onDrop = onDrop;
    }

    public String getDdGroup() {
        return ddGroup;
    }

    public void setDdGroup(String ddGroup) {
        this.ddGroup = ddGroup;
    }

    public String getDropGroup() {
        return dropGroup;
    }

    public void setDropGroup(String dropGroup) {
        this.dropGroup = dropGroup;
    }

    public String getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(String defaultSort) {
        this.defaultSort = defaultSort;
    }

    public String getDefaultSortDir() {
        return defaultSortDir;
    }

    public void setDefaultSortDir(String defaultSortDir) {
        this.defaultSortDir = defaultSortDir;
    }

    public Boolean getAutoSizeColumns() {
        return autoSizeColumns;
    }

    public void setAutoSizeColumns(Boolean autoSizeColumns) {
        this.autoSizeColumns = autoSizeColumns;
    }

    public Boolean getEnablePagination() {
        return enablePagination;
    }

    public void setEnablePagination(Boolean enablePagination) {
        this.enablePagination = enablePagination;
    }

    public Boolean getLoadOnStart() {
        return loadOnStart;
    }

    public void setLoadOnStart(Boolean loadOnStart) {
        this.loadOnStart = loadOnStart;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getInitialize() {
        return initialize;
    }

    public void setInitialize(Boolean initialize) {
        this.initialize = initialize;
    }

    public Boolean getSingleSelect() {
        return singleSelect;
    }

    public void setSingleSelect(Boolean singleSelect) {
        this.singleSelect = singleSelect;
    }

    public Boolean getLoadMask() {
        return loadMask;
    }

    public void setLoadMask(Boolean loadMask) {
        this.loadMask = loadMask;
    }

    public Boolean getEnableColumnsLock() {
		return enableColumnsLock;
	}

	public void setEnableColumnsLock(Boolean enableColumnsLock) {
		this.enableColumnsLock = enableColumnsLock;
	}

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public void addField(Field field){
        fields.add(field);
    }

    public void doInitBody() throws JspException {
    	super.doInitBody();
        fields = new ArrayList<Field>();
        contextMenu = null;
    }

	public String prepareOnReadyFunction(){
		StringBuilder sb = new StringBuilder();
		
        sb.append("var cm, sm;\n");
        sb.append("sm = new Ext.grid.RowSelectionModel({singleSelect:").append(singleSelect ? "true" : "false").append("});\n");
        
        Store store = new Store("ds", loadOnStart == null ? false : loadOnStart, fields);
        store.setItemTag(itemTag);
        store.setIdField(idProperty);
        store.setGroupField(groupField);

        if(StringUtils.isNotBlank(url))
            store.setUrl(url);

        store.setReader(reader);
        store.setType(dataSourceType);
        if(rowItems != null){
            try {
                store.setItems((rowItems instanceof String ? evaluate("rowItems", rowItems) : rowItems));
            } catch (JspException e) {
                throw new RuntimeException(e);
            }
        }
		store.setFields(fields);
		sb.append(store.buildStoreDeclaration());

        if(defaultSort != null && defaultSort.length() > 0){
            sb.append("ds.setDefaultSort('").append(defaultSort).append("', '").append(defaultSortDir).append("');");
        }

        if(StringUtils.isNotBlank(onSelectionChange)){
            sb.append("sm.on('selectionchange', ").append(onSelectionChange).append(");\n");
        }

        if(enableColumnsLock){
            sb.append("cm = new Ext.grid.LockingColumnModel(");
        }else{
            sb.append("cm = new Ext.grid.ColumnModel(");
        }

        JSONArray columnsModel = new JSONArray();
        for(Field field : fields){
            JSONObject json = new JSONObject();
            json.elementOpt("header", field.getTitle());
            json.put("dataIndex", field.getField());
            json.elementOpt("hidden", field.getHidden());
            json.elementOpt("width", field.getWidth());
            if(enableColumnsLock){
                json.elementOpt("locked", field.getLocked());
            }
            if(field.getRendererFunction() != null){
                json.put("renderer", new JSONFunction(field.getRendererFunction()));
            }
            columnsModel.add(json);
        }
        sb.append(columnsModel.toString());
        sb.append(");\n");
        sb.append("cm.defaultSortable = true;\n");

        
        prepareConfig();        
        if(enableDrop){
        	JSONObject listeners = new JSONObject();
        	listeners.element("render", new JSONFunction("function(){ new Ext.dd.DropTarget(grid.el, {ddGroup: '" + dropGroup + "', notifyDrop: " + onDrop + "}); }"));
        	addConfigElement("listeners", listeners);
        }

        if(enableColumnsLock){
            sb.append("var grid = new Ext.grid.LockingGridPanel(").append(getConfig().toString(2)).append(");");        	
        }else{
            sb.append("var grid = new Ext.grid.GridPanel(").append(getConfig().toString(2)).append(");");
        }
        
        if(StringUtils.isNotBlank(onRowDblClick)){
            sb.append("grid.on('rowdblclick', ").append(onRowDblClick).append(");");
        }
        if(contextMenu != null){
            sb.append("grid.contextMenu = ").append(contextMenu.toString()).append(";");
            sb.append("grid.on('rowcontextmenu', function(grid, rowIndex, e) {");
            sb.append("e.stopEvent();");
            sb.append("var coords = e.getXY();");
            sb.append("grid.getSelectionModel().selectRow(rowIndex);");
            sb.append("grid.contextMenu.showAt([coords[0], coords[1]]);");
            sb.append("});");
        }

        if(StringUtils.isNotBlank(getReplacePanel())){
            sb.append(getReplacePanel()).append(".replacePanel(grid);\n");
        }else {
        	Tag parent = findParent(PanelTag.class);
        	if(parent == null){
        		sb.append("grid.render('").append(id).append("-grid');");
        	}
        }

        if(loadOnStart){
            if(enablePagination){
                sb.append("ds.load({params:{start:0, limit:").append(pageSize.toString()).append("}});");
            }else if(rowItems == null){
                sb.append("ds.load();");
            }
        }else{
            sb.append("ds.lastOptions = {params:{start:0, limit:").append(pageSize.toString()).append("}};");
        }
                
        return sb.toString();
	}

	public int doEndTag() throws JspException {
        StringBuilder sb = new StringBuilder();

        if(StringUtils.isBlank(getReplacePanel())){
            Tag parent = findParent(PanelTag.class);
            if(parent == null){
                sb.append("<div id=\"").append(id).append("-grid\"></div>");                    
            }
        }

        sb.append("<script type=\"text/javascript\">\n");
        sb.append("Ext.onReady(function(){\n");
        sb.append(prepareOnReadyFunction());
        sb.append("});");
        sb.append("</script>\n");

    	Tag parent = findParent(PanelTag.class);
    	if(parent != null){
            ((PanelTag) parent).addItem(new Item(this.toJSON()));
    	}

        try {
	        pageContext.getOut().write(sb.toString());
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return Tag.EVAL_PAGE;
    }
	
	public void prepareConfig(){
    	super.prepareConfig();
    	
    	addRemoveElement("xtype");
        addConfigElement("ds", new JSONFunction("ds"));
        addConfigElement("cm", new JSONFunction("cm"));
        addConfigElement("sm", new JSONFunction("sm"));

        //addConfigElementOpt("stripeRows", stripeRows);
        addConfigElementOpt("forceFit", forceFit);
        addConfigElementOpt("loadMask", loadMask);

        JSONObject viewConfig = new JSONObject();
        viewConfig.put("emptyText", getText("grid.emptyText"));
        
        if(StringUtils.isNotBlank(bodyField)){
            viewConfig.put("enableRowBody", true);
            viewConfig.put("getRowClass", new JSONFunction("function(record, rowIndex, p, store){ p.body = '<p>' + record.data." + bodyField + " + '</p>'; return 'x-grid3-row-expanded';}"));
        }
        addConfigElement("viewConfig", viewConfig);

        if(StringUtils.isNotBlank(groupField)){
            addConfigElement("view", new JSONFunction("new Ext.grid.GroupingView({forceFit:true, groupTextTpl: '{text} ({[values.rs.length]})'})"));
        }

        addConfigElementOpt("enableDragDrop", enableDragDrop);
        addConfigElementOpt("ddGroup", ddGroup);

        if(enablePagination){
            JSONObject toolbar = new JSONObject();
            toolbar.put("store", new JSONFunction("ds"));
            toolbar.put("pageSize", pageSize);
            toolbar.put("displayInfo", displayPagingInfo);

            if(enableFilter){
                JSONArray toolbarItems = new JSONArray();
                toolbarItems.add("-");
                toolbarItems.add("Search");
                JSONObject filter = new JSONObject();
                filter.put("store", new JSONFunction("ds"));
                filter.put("width", 250);
                toolbarItems.add(new JSONFunction("new Ext.ux.SearchField(" + filter + ")"));
                toolbar.put("items", toolbarItems);
            }
            addConfigElement("bbar", new JSONFunction("new Ext.PagingToolbar(" + toolbar.toString() + ")"));
        }
    }

    public Object toJSON() {
        return new JSONFunction("Ext.getCmp('" + id + "')");
    }

    public void addItem(PanelItem item) {
        throw new RuntimeException("El grid no puede contener items");
    }

    private Object contextMenu;
    public void setContextMenu(Item item){
        contextMenu = ((JSONObject) item.toJSON()).get("menu");
    }
    
    protected Object evaluate(String attributeName, Object value) throws JspException {
        if (value instanceof String) {
            return ExpressionEvaluationUtils.evaluate(attributeName, (String) value, this.pageContext);
        }
        else {
            return value;
        }
    }
    
}
