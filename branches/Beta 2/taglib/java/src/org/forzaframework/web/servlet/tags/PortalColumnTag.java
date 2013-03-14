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

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.forzaframework.web.servlet.tags.PanelTag;
import org.forzaframework.web.servlet.tags.PanelItem;

public class PortalColumnTag extends PanelTag {

    public void prepareConfig(){
    	addConfigElementOpt("id", id);
    	
    	addConfigElementOpt("style", getStyle());
    	
        if(StringUtils.isNotBlank(getColumnWidth())){
        	addConfigElement("columnWidth", Double.valueOf(getColumnWidth()));
        }

        if(getItems().size() > 0){
            JSONArray jsonItems = new JSONArray();

            for(PanelItem item : getItems()){
                jsonItems.add(item.toJSON());
            }
            addConfigElement("items", jsonItems);
        }
    }

}
