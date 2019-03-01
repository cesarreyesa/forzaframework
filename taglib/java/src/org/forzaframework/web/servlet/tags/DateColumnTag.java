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
import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 1:05:23
 * Description:
 */
public class DateColumnTag extends ColumnTag{

    public int doEndTag() throws JspException {
        EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
        Field field = new Field();

        field.setField(this.getField());
        field.setTitle(title != null ? title : getText(titleKey));
        field.setWidth(width);
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setType("date");
        field.setXtype("date");
        field.setSortable(sortable);

        if(tag != null){
            field.setEditorJson(this.toJSON());
            tag.addField(field);
        }else{
            GridTag grid = (GridTag) findParent(GridTag.class);
            field.setRendererFunction("function (value){ return value ? value.dateFormat('d/m/Y') : ''; }");
            grid.addField(field);
        }        

        return EVAL_PAGE;
    }

    private Object toJSON() {
        JSONObject json = new JSONObject();

        json.put("format", "d/m/Y");
        json.put("enableKeyEvents", this.enableKeyEvents == null ? false : this.enableKeyEvents);

        if(this.listeners.size() > 0){
            JSONObject listeners = new JSONObject();
            for (Listener listener : this.listeners) {
                listeners.put(listener.getEventName(), new JSONFunction(listener.getHandler()));
            }
            json.put("listeners", listeners);
        }

        return new JSONFunction("new Ext.form.DateField(" + json.toString() + ")");
    }
}
