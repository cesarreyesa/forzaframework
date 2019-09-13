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

package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.JSONFunction;
import org.forzaframework.web.servlet.tags.PanelItem;
import org.forzaframework.web.servlet.tags.Item;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.springframework.core.Conventions;
//import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 02:54:14 PM
 * Description:
 */
public class FormTag extends PanelTag implements PanelItem {

    public static final String DEFAULT_COMMAND_NAME = "";
    private static final String MODEL_ATTRIBUTE = "modelAttribute";
    static final String MODEL_ATTRIBUTE_VARIABLE_NAME = Conventions.getQualifiedAttributeName(FormTag.class, MODEL_ATTRIBUTE);

    private String description;
    private String modelAttribute = DEFAULT_COMMAND_NAME;
    private String labelAlign;
    private String buttonAlign;
    private Integer labelWidth = 75;
    private String url;
    private String loadUrl;
    private String waitMsgTarget;
    private String onActionComplete;
    private String onActionFailed;
    private Boolean fileUpload;
    private Boolean initialize = true;
    private Boolean hidden;
    private List<Field> fields = new ArrayList<Field>();
    private List<Item> buttons = new ArrayList<Item>();

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public String getModelAttribute() {
        return modelAttribute;
    }

    public void setModelAttribute(String modelAttribute) {
        this.modelAttribute = modelAttribute;
    }

    public String getLabelAlign() {
        return labelAlign;
    }

    public void setLabelAlign(String labelAlign) {
        this.labelAlign = labelAlign;
    }

    public String getButtonAlign() {
		return buttonAlign;
	}

	public void setButtonAlign(String buttonAlign) {
		this.buttonAlign = buttonAlign;
	}

	public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(Boolean fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public String getWaitMsgTarget() {
        return waitMsgTarget;
    }

    public void setWaitMsgTarget(String waitMsgTarget) {
        this.waitMsgTarget = waitMsgTarget;
    }

    public String getOnActionComplete() {
        return onActionComplete;
    }

    public void setOnActionComplete(String onActionComplete) {
        this.onActionComplete = onActionComplete;
    }

    public String getOnActionFailed() {
        return onActionFailed;
    }

    public void setOnActionFailed(String onActionFailed) {
        this.onActionFailed = onActionFailed;
    }

    public Boolean getInitialize() {
        return initialize;
    }

    public void setInitialize(Boolean initialize) {
        this.initialize = initialize;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public void addField(Field field){
        this.fields.add(field);
    }

    private List<Store> storeDeclarations = new ArrayList<Store>();
    public void addStoreDeclaration(Store store){
        storeDeclarations.add(store);
    }

    public void addButton(Item button) {
        buttons.add(button);
    }

    public void doInitBody() throws JspException {
    	super.doInitBody();
    	
    	// inicializacion de variables
        fields = new ArrayList<Field>();
        buttons = new ArrayList<Item>();        
        storeDeclarations = new ArrayList<Store>();

        // spring model support
        String modelAttribute = resolveModelAttribute();
        this.pageContext.setAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, PageContext.REQUEST_SCOPE);
        // revisar porque??
        modelAttribute = "";        
    }

	public String prepareOnReadyFunction(){
		StringBuilder sb = new StringBuilder();
		
        sb.append("Ext.form.Field.prototype.msgTarget = 'side';\n");

        for(Store store : storeDeclarations){
            sb.append(store.buildStoreDeclaration()).append("\n");
            if(store.getLoadOnStart() && "remote".equals(store.getType())) {
                sb.append(store.getName()).append(".load();").append("\n");
            }
        }

        prepareConfig();
        sb.append("var formPanel = new Ext.FormPanel(").append(getConfig().toString(2)).append(");");

        sb.append("var form = formPanel.getForm();");
        if(StringUtils.isNotBlank(this.onActionComplete)){
            sb.append("form.on('actioncomplete', ").append(onActionComplete).append(");\n");
        }
        if(StringUtils.isNotBlank(this.onActionFailed)){
            sb.append("form.on('actionfailed', ").append(onActionFailed).append(");\n");
        }

        sb.append("form.reader = new Ext.data.XmlReader(");
        sb.append("{record: 'item', success: '@success'},");

        JSONArray fields = new JSONArray();
        fields.add("id");

        for(Field field : this.fields){
            JSONObject fieldJSON = new JSONObject();
            fieldJSON.put("name", field.getField());
            fieldJSON.elementOpt("mapping", field.getMapping());
            fields.add(fieldJSON);
        }
        sb.append(fields.toString()).append(");\n");

        if(StringUtils.isNotBlank(getReplacePanel())){
            sb.append(getReplacePanel()).append(".replacePanel(formPanel);\n");
        }else {
        	Tag parent = (PanelTag) findParent(PanelTag.class);
        	if(parent == null){
                sb.append("formPanel.render('").append(id).append("');");                    
        	}
        }

        return sb.toString();
	}

	public int doEndTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("Ext.onReady(function(){\n");
        sb.append(prepareOnReadyFunction());
        sb.append("});");
        sb.append("</script>\n");

    	Tag parent = (PanelTag) findParent(PanelTag.class);
    	if(parent != null){
            ((PanelTag) parent).addItem(new Item(this.toJSON()));
    	}

        try {
            JspWriter writer = bodyContent.getEnclosingWriter();
            bodyContent.writeOut(writer);
            pageContext.getOut().write(sb.toString());
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }

        return EVAL_PAGE;
    }    
	
    public void prepareConfig(){
    	super.prepareConfig();

        addConfigElementOpt("description", getDescription());

        addConfigElementOpt("anchor", getAnchor());

        addConfigElementOpt("labelAlign", labelAlign);
        addConfigElementOpt("buttonAlign", buttonAlign);
        addConfigElementOpt("labelWidth", labelWidth);
        addConfigElementOpt("waitMsgTarget", waitMsgTarget);
        addConfigElementOpt("fileUpload", fileUpload);
        if (hidden  != null) {
            addConfigElementOpt("hidden", hidden);
        }
        addConfigElementOpt("url", url);
        addConfigElement("errorReader", new JSONFunction("new Ext.form.XmlErrorReader()"));

        JSONArray jsonItems = new JSONArray();
        for(PanelItem item : getItems()){
            jsonItems.add(item.toJSON());
        }
        addConfigElement("items", jsonItems);

        if(buttons.size() > 0){
            JSONArray butonItems = new JSONArray();
            for(Item button : buttons){
            	butonItems.add(button.toJSON());
            }
            addConfigElement("buttons", butonItems);
        }
    }

    public Object toJSON() {
        return new JSONFunction("Ext.getCmp('" + id + "')");
    }

    protected String resolveModelAttribute() throws JspException {
        Object resolvedModelAttribute = evaluate(MODEL_ATTRIBUTE, getModelAttribute());
        if (resolvedModelAttribute == null) {
            throw new IllegalArgumentException(MODEL_ATTRIBUTE + " must not be null");
        }
        return (String) resolvedModelAttribute;
    }

    protected Object evaluate(String attributeName, Object value) throws JspException {
        if (value instanceof String) {
            ELContext elContext =  this.pageContext.getELContext();
            JspFactory jf = JspFactory.getDefaultFactory();
            JspApplicationContext jac = jf.getJspApplicationContext(pageContext.getServletContext());
            ExpressionFactory ef = jac.getExpressionFactory();
            ValueExpression val = ef.createValueExpression(elContext, value.toString(), Object.class);
            return val.getValue(elContext);

//            return ExpressionEvaluationUtils.evaluate(attributeName, (String) value, this.pageContext);
        }
        else {
            return value;
        }
    }

}
