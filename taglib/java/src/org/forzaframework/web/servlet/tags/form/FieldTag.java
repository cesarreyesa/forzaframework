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

import org.apache.commons.lang.StringUtils;
import org.forzaframework.core.persistance.BaseEntity;
import org.forzaframework.util.DateUtils;
import org.forzaframework.web.servlet.tags.*;

import javax.servlet.jsp.JspException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 05:09:37 PM
 * Description:
 */
public abstract class FieldTag extends AbstractDataBoundFormElementTag implements PanelItem, Observable {

	protected String description;
    protected Boolean bind = true;
    protected String anchor;
    protected String value;
    protected String title;
    protected String titleKey;
    protected String field;
    protected String mapping;
    protected Boolean hidden;
    protected Boolean allowBlank;
    protected String width;
    protected String height;
    protected Boolean autoHeight;
    protected Boolean disabled;
    protected Boolean hideLabel;
    protected String labelSeparator;
    protected List<Listener> listeners = new ArrayList<Listener>();

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHideLabel() {
		return hideLabel;
	}

	public void setHideLabel(Boolean hideLabel) {
		this.hideLabel = hideLabel;
	}

	public String getLabelSeparator() {
		return labelSeparator;
	}

	public void setLabelSeparator(String labelSeparator) {
		this.labelSeparator = labelSeparator;
	}

	public Boolean getAllowBlank() {
		return allowBlank;
	}

	public void setAllowBlank(Boolean allowBlank) {
		this.allowBlank = allowBlank;
	}

	public Boolean getBind() {
        return bind;
    }

    public void setBind(Boolean bind) {
        this.bind = bind;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
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

    public Boolean getAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(Boolean autoHeight) {
        this.autoHeight = autoHeight;
    }

    public Boolean getDisabled() {
        return disabled;        
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public int doStartTag() throws JspException {
        int val = super.doStartTag();
        if(StringUtils.isBlank(getValue()) && bind){
            this.setValue("");
            this.setPath(getField());
            if(StringUtils.isNotBlank(getCommandName())){
                this.setPath(getCommandName() + "." + getField());
                if(getActualValue() != null) {
                    Object value = getActualValue();
                    if(value instanceof BaseEntity && ((BaseEntity)value).getKey() != null){
                        this.setValue(((BaseEntity)value).getKey().toString());
                    }else{
                        if(getType().equals("datefield") && (value instanceof Timestamp || value instanceof Date)){
                            Date time = (Date) value;
                            this.setValue(DateUtils.getString(time));
                        }else{
                            this.setValue(value.toString());
                        }
                    }
                }
            }            
        }
        return val;
    }

    public int doEndTag() throws JspException {

        Field field = new Field();
        field.setId(id);
        field.setField(this.field);
        field.setMapping(mapping);
        field.setValue(value);

        FormTag form = (FormTag) findParent(FormTag.class);
        form.addField(field);

        PanelTag parent = (PanelTag) findParent(PanelTag.class);
        parent.addItem(new Item(this.toJSON()));

        value = null;
        doFinally();

        return EVAL_PAGE;
    }

    public abstract String getType();

    public abstract String getHtmlDeclaration();

}
