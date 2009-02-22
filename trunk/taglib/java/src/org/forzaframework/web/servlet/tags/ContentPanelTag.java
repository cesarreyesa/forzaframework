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

import org.forzaframework.web.servlet.tags.PanelTag;

import javax.servlet.jsp.JspException;

/**
 * User: Cesar Reyes
 * Date: 20/06/2007
 * Time: 03:32:52 PM
 * Description:
 */
public class ContentPanelTag extends PanelTag {

    private Integer activeItem;
    private String autoLoad;

    public Integer getActiveItem() {
        return activeItem;
    }

    public void setActiveItem(Integer activeItem) {
        this.activeItem = activeItem;
    }

    public String getAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(String autoLoad) {
        this.autoLoad = autoLoad;
    }
    
    private String htmlContent;
    public void setHtmlContent(String htmlContent){
    	this.htmlContent = htmlContent;
    }
    
    public void prepareConfig(){
    	super.prepareConfig();
        if(getLayout() != null && getLayout().equals("card")){
            addConfigElement("activeItem", activeItem == null ? 0 : activeItem);
        }    	
        addConfigElementOpt("autoLoad", autoLoad);
        if(getLayout() != null && getLayout().equals("card")){
        	addConfigElement("activeItem", activeItem == null ? 0 : activeItem);
        }
        addConfigElementOpt("html", htmlContent);
    }

	@Override
	public void doInitBody() throws JspException {
		super.doInitBody();
		htmlContent = null;
	}

}
