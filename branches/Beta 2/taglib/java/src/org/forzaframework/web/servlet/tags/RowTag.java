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

import org.forzaframework.web.servlet.tags.EditableGridTag;
import org.forzaframework.web.servlet.tags.BaseTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: Cesar Reyes
 * Date: 14/03/2007
 * Time: 04:59:29 PM
 * Description:
 */
public class RowTag extends BaseTag {

    private String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int doEndTag() throws JspException {
        Tag tag = getParent().getParent();
        EditableGridTag parent;
        if(tag instanceof EditableGridTag){
            parent = (EditableGridTag) tag;
        }else{
            parent = (EditableGridTag) tag.getParent();
        }
        parent.addRow(json);

        return EVAL_PAGE;
    }

}
