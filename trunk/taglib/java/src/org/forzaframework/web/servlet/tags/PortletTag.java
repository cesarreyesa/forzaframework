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