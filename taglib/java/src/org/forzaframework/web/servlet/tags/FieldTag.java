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

import org.forzaframework.web.servlet.tags.form.ComboboxTag;
import org.forzaframework.web.servlet.tags.form.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 29-nov-2007
 * Time: 23:14:52
 * Description:
 */
public class FieldTag extends BaseTag {

    protected String field;
    protected String mapping;

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

    public int doEndTag() throws JspException {

        Tag parent = getParent();

        Field field = new Field();
        field.setField(this.field);
        field.setMapping(mapping);

        if(parent instanceof DataViewTag)
            ((DataViewTag) parent).addField(field);
        else if(parent instanceof ComboboxTag)
            ((ComboboxTag) parent).addField(field);
        //DataViewTag parent = (DataViewTag) findParent(DataViewTag.class);
        //parent.addField(field);

        return Tag.EVAL_PAGE;
    }

}
