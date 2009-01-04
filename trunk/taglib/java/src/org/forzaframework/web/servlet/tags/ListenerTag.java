package org.forzaframework.web.servlet.tags;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;

/**
 * @author cesarreyes
 *         Date: 23-sep-2008
 *         Time: 21:34:35
 */
public class ListenerTag extends BaseBodyTag {

    private String eventName;
    private String handler;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int doEndTag() throws JspException {
        Tag tag = findParent(Observable.class);
        if(tag != null){
            ((Observable) tag).addListener(new Listener(eventName, handler));
        }

        return EVAL_PAGE;
    }
}
