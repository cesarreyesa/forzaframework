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
