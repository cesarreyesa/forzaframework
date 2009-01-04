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
