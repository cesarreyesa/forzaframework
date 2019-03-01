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

import org.forzaframework.web.servlet.tags.form.Field;
import org.forzaframework.web.servlet.tags.form.LovFieldTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 24-dic-2006
 * Time: 12:00:58
 * Description:
 */
public class ColumnTag extends BaseTag implements Observable {

//    protected String defaultValue;
    protected String id;
    protected String title;
    protected String titleKey;
    protected String field;
    protected String mapping;
    protected Boolean hidden;
    protected Boolean locked;
    protected Integer width;
    protected Boolean disabled;
    protected String rendererFunction;
    protected Boolean sortable;
    protected String align;
    protected Boolean alwaysHidden;
    protected Boolean enableKeyEvents;
    protected List<Listener> listeners = new ArrayList<Listener>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getRendererFunction() {
        return rendererFunction;
    }

    public void setRendererFunction(String rendererFunction) {
        this.rendererFunction = rendererFunction;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Boolean getAlwaysHidden() {
        return alwaysHidden;
    }

    public void setAlwaysHidden(Boolean alwaysHidden) {
        this.alwaysHidden = alwaysHidden;
    }

    public Boolean getEnableKeyEvents() {
        return enableKeyEvents;
    }

    public void setEnableKeyEvents(Boolean enableKeyEvents) {
        this.enableKeyEvents = enableKeyEvents;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public int doEndTag() throws JspException {

    	Field field = new Field();
        field.setId(this.id);
        field.setField(this.field);
        field.setTitle(title != null ? title : getText(titleKey));
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setWidth(width);
        field.setLocked(locked);
        field.setRendererFunction(rendererFunction);
        field.setSortable(sortable);
        field.setAlign(align);
        field.setAlwaysHidden(alwaysHidden);

        Tag parent = findParent(GridTag.class);
        if(parent != null){
        	((GridTag) parent).addField(field);	
        }
        else{
        	parent = findParent(EditableGridTag.class);
        	if(parent != null){
            	((EditableGridTag) parent).addField(field);
        	}else{
        		parent = findParent(LovFieldTag.class);
        		if(parent != null){
        			((LovFieldTag) parent).addField(field);
        		}
        	}        	
        }
        
        return Tag.EVAL_PAGE;
    }

}
