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

import java.io.Reader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class HtmlContentTag extends BaseBodyTag {
	
	public int doEndTag() throws JspException {
        if(this.bodyContent != null){
        	PortletTag portlet = (PortletTag) findParent(PortletTag.class);
        	if(portlet != null){
                portlet.setHtmlContent(bodyContent.getString());
        	}else{
                ContentPanelTag panel = (ContentPanelTag) findParent(ContentPanelTag.class);
                panel.setHtmlContent(bodyContent.getString());
        	}
        }
        return EVAL_PAGE;
    }    

}
