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

import javax.servlet.jsp.JspException;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 0:27:18
 * Description:
 */
public class TextColumnTag extends ColumnTag{

    public int doEndTag() throws JspException {
        EditableGridTag tag = (EditableGridTag) findParent(EditableGridTag.class);
        Field field = new Field();

        field.setId(this.getId());
        field.setField(this.getField());
        field.setTitle(title != null ? title : getText(titleKey));
        field.setWidth(width);
        field.setMapping(mapping);
        field.setHidden(hidden);
        field.setType("string");
        field.setEditorJson(this.toJSON());

        tag.addField(field);
        return EVAL_PAGE;
    }

    private Object toJSON() {
        return new JSONFunction("new Ext.form.TextField()");        
    }
}
