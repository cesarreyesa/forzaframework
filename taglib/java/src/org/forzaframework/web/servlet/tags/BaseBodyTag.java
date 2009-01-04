package org.forzaframework.web.servlet.tags;

import javax.servlet.jsp.tagext.BodyContent;

/**
 * User: Cesar Reyes
 * Date: 21/12/2006
 * Time: 03:45:58 PM
 * Description:
 */
public class BaseBodyTag extends BaseTag {
    protected BodyContent bodyContent;

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

}
