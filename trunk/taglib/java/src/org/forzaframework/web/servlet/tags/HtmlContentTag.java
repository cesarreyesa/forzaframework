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
