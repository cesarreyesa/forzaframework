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

import org.forzaframework.web.servlet.tags.Item;
import org.springframework.util.Assert;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 21/12/2006
 * Time: 06:01:10 PM
 * Description:
 */
public class SeparatorTag extends BaseTag {

    private Boolean alignToRight = false;

    public Boolean getAlignToRight() {
        return alignToRight;
    }

    public void setAlignToRight(Boolean alignToRight) {
        this.alignToRight = alignToRight;
    }

    public int doEndTag() throws JspException {
        ToolbarTag parent = (ToolbarTag) findParent(ToolbarTag.class);
        if(parent != null){
            parent.addItem(new Item(this.toJSON()));
        }else{
        	MenuTag menu = (MenuTag) findParent(MenuTag.class);
        	Assert.notNull(menu, "SeparatorTag must be inside a ToolbarTag or MenuTag");
        	menu.addItem(new Item(this.toJSON()));
        }
        return EVAL_PAGE;
    }

    public Object toJSON(){
        return new JSONFunction("'-" + (alignToRight ? ">" : "") + "'");
    }
}
