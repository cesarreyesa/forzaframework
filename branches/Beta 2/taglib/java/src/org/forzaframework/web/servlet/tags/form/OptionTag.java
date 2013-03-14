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

import org.forzaframework.web.servlet.tags.BaseBodyTag;
import org.forzaframework.web.servlet.tags.ComboboxColumnTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * User: cesarreyes
 * Date: 25-oct-2007
 * Time: 14:39:42
 * Description:
 */
public class OptionTag extends BaseBodyTag {

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int doEndTag() throws JspException {
        Tag tag = findParent(ComboboxTag.class);
        if(tag != null){
            ((ComboboxTag) tag).addOption(new Option(value, text));            
        }else{
            tag = findParent(ComboboxColumnTag.class);
            ((ComboboxColumnTag) tag).addOption(new Option(value, text));
        }

        return EVAL_PAGE;
    }

}
