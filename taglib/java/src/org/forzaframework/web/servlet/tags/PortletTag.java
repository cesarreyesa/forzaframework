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

import javax.servlet.jsp.JspException;

import org.forzaframework.web.servlet.tags.PanelTag;


public class PortletTag extends PanelTag {

	public PortletTag(){
		setXtype("portlet");
	}

    private String htmlContent;
    public void setHtmlContent(String htmlContent){
    	this.htmlContent = htmlContent;
    }

    public void prepareConfig(){
    	super.prepareConfig();
        addConfigElementOpt("html", htmlContent);
    }

	@Override
	public void doInitBody() throws JspException {
		super.doInitBody();
		htmlContent = null;
	}

}