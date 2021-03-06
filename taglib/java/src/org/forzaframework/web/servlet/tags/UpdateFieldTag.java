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

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 26/01/2007
 * Time: 04:45:34 PM
 * Description:
 */
public class UpdateFieldTag extends BaseTag {

    private String field;
    private String mapping;

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
        ((ComboboxTag) parent).addField(id, field, mapping);
        return EVAL_PAGE;
    }

}
