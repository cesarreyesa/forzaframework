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
import org.forzaframework.web.servlet.tags.form.Field;
import org.forzaframework.web.servlet.tags.Item;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 0:09:42
 * Description:
 */

public class EditableGridTag extends PanelTag implements PanelItem {

    private Boolean loadMask;
    private String url = "";
    private String formUrl;
    private String path;
    private String formId;
    private String itemTag = "item";
    private String errorCode;
    private String onLoad;
    private String cellUpdated;
    private Boolean insertRecordIfEmpty = false;
    private List<String> rows;
    private Boolean initialize = true;
    private String onSelectionChange;
    private Boolean autoSizeColumns = false;
    private Boolean loadOnStart = true;
    private Boolean pruneModifiedRecords = false;

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

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Boolean getLoadMask() {
        return loadMask;
    }

    public void setLoadMask(Boolean loadMask) {
        this.loadMask = loadMask;
    }

    public String getOnLoad() {
        return onLoad;
    }

    public void setOnLoad(String onLoad) {
        this.onLoad = onLoad;
    }

    public String getCellUpdated() {
        return cellUpdated;
    }

    public void setCellUpdated(String cellUpdated) {
        this.cellUpdated = cellUpdated;
    }

    public Boolean getInsertRecordIfEmpty() {
        return insertRecordIfEmpty;
    }

    public void setInsertRecordIfEmpty(Boolean insertRecordIfEmpty) {
        this.insertRecordIfEmpty = insertRecordIfEmpty;
    }

    public Boolean getInitialize() {
        return initialize;
    }

    public void setInitialize(Boolean initialize) {
        this.initialize = initialize;
    }

    public String getOnSelectionChange() {
        return onSelectionChange;
    }

    public void setOnSelectionChange(String onSelectionChange) {
        this.onSelectionChange = onSelectionChange;
    }

    public Boolean getAutoSizeColumns() {
        return autoSizeColumns;
    }

    public void setAutoSizeColumns(Boolean autoSizeColumns) {
        this.autoSizeColumns = autoSizeColumns;
    }

    public Boolean getPruneModifiedRecords() {
        return pruneModifiedRecords;
    }

    public void setPruneModifiedRecords(Boolean pruneModifiedRecords) {
        this.pruneModifiedRecords = pruneModifiedRecords;
    }

    private List<Field> fields;

    public void addField(Field field){
        fields.add(field);
    }

    public void addRow(String json){
        rows.add(json);
    }

    private List<Map<String, Object>> storeDeclarations = new ArrayList<Map<String, Object>>();
    public void addStoreDeclaration(Map<String, Object> map){
        storeDeclarations.add(map);
    }

    public void doInitBody() throws JspException {
        try{
            if(this.bodyContent != null){
                // limpia la variable
                fields = new ArrayList<Field>();
                rows = new ArrayList<String>();
                storeDeclarations = new ArrayList<Map<String, Object>>();
                topToolbar = null;

                JspWriter out = pageContext.getOut();
                StringBuilder sb = new StringBuilder();

                if(StringUtils.isNotBlank(errorCode)){
                    sb.append("<div id=\"").append(errorCode).append("\" ></div>");
                }
                sb.append("<div id=\"").append(id).append("\">");


                out.write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        try {
            if(this.bodyContent != null){
                StringBuilder sb = new StringBuilder();

                sb.append("</div>");

                sb.append("<script type=\"text/javascript\">\n");
                sb.append("Ext.onReady(function(){\n");

                for(Map<String, Object> storeDeclaration : storeDeclarations){
                    sb.append(storeDeclaration.get("js")).append("\n");
                    if((Boolean) storeDeclaration.get("load")) {
                        sb.append(storeDeclaration.get("name")).append(".load();").append("\n");
                    }
                }

                sb.append("sm = new Ext.grid.RowSelectionModel();");
                sb.append("cm = new Ext.grid.ColumnModel(");
                JSONArray columnsModel = new JSONArray();
                for(Field field : fields){
                    JSONObject json = new JSONObject();
                    json.put("header", field.getTitle());
                    json.elementOpt("width", field.getWidth());
                    json.put("dataIndex", field.getField());
                    json.put("editor", field.getEditorJson());
                    json.elementOpt("hidden", field.getHidden());
                    if(field.getXtype() != null && field.getXtype().equals("combo")){
                        String id = field.getId();
                        if(StringUtils.isBlank(field.getId())){
                            id = this.id + "_" + field.getField() + "_cmb";
                        }
                        json.put("renderer", new JSONFunction("function(value){ var record = Ext.StoreMgr.get('" + id + "_ds').getById(value); if(record) { return record.data['" + field.getDisplayField() + "']; } else { return value; } }"));

                    }
                    if(field.getXtype() != null && field.getXtype().equals("date")){
                        json.put("renderer", new JSONFunction("function (value){ return value ? value.dateFormat('d/m/Y') : ''; }"));
                    }
                    if(field.getRendererFunction() != null){
                        json.put("renderer", new JSONFunction(field.getRendererFunction()));
                    }

                    columnsModel.add(json);
                }
                sb.append(columnsModel.toString());
                sb.append(" );");
                sb.append("cm.defaultSortable = true;");

                JSONArray defaultValues = new JSONArray();
                for(Field field : fields){
                    if(field.getType() == null || field.getType().equals("string")){
                        defaultValues.add("");
                    }else if(field.getType().equals("date")){
                        defaultValues.add(new JSONFunction("new Date()"));
                    }else if(field.getType().equals("float")){
                        defaultValues.add(0);
                    }
                }
                defaultValues.add(new JSONFunction("++Ext.data.Record.AUTO_ID"));

                if(StringUtils.isEmpty(url)){
                    if(insertRecordIfEmpty){
                        sb.append("var data = [").append(defaultValues.toString()).append("];");
                    }
                    else{
                        sb.append("var data = [];");
                    }
                }

                sb.append("var RecordType = Ext.data.Record.create(");
                JSONArray fields = new JSONArray();
                for(Field field : this.fields){
                    JSONObject jsonField = new JSONObject();

                    jsonField.put("name", field.getField());
                    jsonField.elementOpt("mapping", field.getMapping());

                    if(field.getType() != null){
                        if(field.getType().equals("string")){
                            jsonField.put("type", "string");
                        }else if(field.getType().equals("float")){
                            jsonField.put("type", "float");
                        }else if(field.getType().equals("date")){
                            jsonField.put("type", "date");
                            jsonField.put("dateFormat", "d/m/Y");
                        }                        
                    }
                    
                    fields.add(jsonField);
                }
                sb.append(fields.toString());
                sb.append(");");

                sb.append("ds = new Ext.data.Store({");
                if(StringUtils.isEmpty(url)){
                    sb.append("proxy: new Ext.data.MemoryProxy(data),");
                    sb.append("reader: new Ext.data.ArrayReader({ id: ").append(this.fields.size()).append("}, RecordType),");
                }else{
                    sb.append("proxy: new Ext.data.HttpProxy({ url: '").append(url).append("'}),");
                    sb.append("reader: new Ext.data.XmlReader({ record:'").append(itemTag).append("',totalRecords:'totalCount',id:'id'}, RecordType),");
                }
                sb.append("pruneModifiedRecords:").append(pruneModifiedRecords.toString()).append("});");

                if(StringUtils.isNotBlank(onSelectionChange)){
                    sb.append("sm.on('selectionchange', ").append(onSelectionChange).append(");");
                }


                sb.append("grid = new Ext.grid.EditorGridPanel(");

                JSONObject gridPanel = new JSONObject();
                gridPanel.put("el", id);
                gridPanel.put("id", id);
                gridPanel.elementOpt("region", getRegion());
                gridPanel.elementOpt("collapsible", getCollapsible());
                gridPanel.put("ds", new JSONFunction("ds"));
                gridPanel.put("cm", new JSONFunction("cm"));
                gridPanel.put("sm", new JSONFunction("sm"));
                gridPanel.put("enableColLock", false);
                gridPanel.put("clicksToEdit", 1);

                JSONObject viewConfig = new JSONObject();
                viewConfig.put("emptyText", getText("grid.emptyText"));                
                gridPanel.put("viewConfig", viewConfig);

                gridPanel.elementOpt("loadMask", loadMask);

                if(getHeight() != null){
                    if(getHeight().contains("%")){
                        gridPanel.put("height", getHeight());
                    }else{
                        gridPanel.elementOpt("height", Integer.valueOf(getHeight()));
                    }
                }

                if(getWidth() != null){
                    if(getWidth().contains("%")){
                        gridPanel.put("width", getWidth());
                    }else{
                        gridPanel.put("width", Integer.valueOf(getWidth()));
                    }
                }

                gridPanel.elementOpt("autoHeight", getAutoHeight());
                gridPanel.elementOpt("autoScroll", getAutoScroll());

                gridPanel.elementOpt("split", getSplit());
                gridPanel.elementOpt("border", getBorder());
                gridPanel.elementOpt("autoSizeColumns", autoSizeColumns);
                gridPanel.elementOpt("tbar", topToolbar);

                if(getTitle() != null || getTitleKey() != null) {
                    gridPanel.elementOpt("title", getTitle() != null ? getTitle() : getText(getTitleKey()));
                }

                sb.append(gridPanel.toString(2)).append(");");

                if(StringUtils.isNotBlank(cellUpdated)){
                    sb.append("grid.on('afteredit', ").append(cellUpdated).append(");");
                }

                if(StringUtils.isNotBlank(getReplacePanel())){
                    sb.append(getReplacePanel()).append(".replacePanel(grid);\n");
                }
                Tag panel = findParent(PanelTag.class);
                if(panel != null){
                    ((PanelTag) panel).addItem(new Item(this.toJSON()));
                }
                if(StringUtils.isBlank(getReplacePanel()) && panel == null){
                    sb.append("grid.render('").append(id).append("');");
                }

                if(StringUtils.isBlank(formId) && StringUtils.isNotBlank(formUrl)){
                    sb.append("form = Ext.DomHelper.append(Ext.get(document.body || document.documentElement), {tag:'form', method:'post', url:'").append(formUrl).append("'}, true);");
                }

                if(loadOnStart){
                    sb.append("ds.load();");
                }

                for(String json : rows){
                    sb.append("ds.add(new RecordType(").append(json).append(", ++Ext.data.Record.AUTO_ID));");
                }


                sb.append("});");
                sb.append("</script>");

                JspWriter writer = bodyContent.getEnclosingWriter();
                bodyContent.writeOut(writer);
                JspWriter out = pageContext.getOut();
                out.write(sb.toString());
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
    }

    public Object toJSON() {
        return new JSONFunction("Ext.getCmp('" + id + "')");
    }

    public void addItem(PanelItem item) {

    }

    private Object topToolbar;
    public void setTopToolbar(Item item) {
        topToolbar = item.toJSON();
    }

}
