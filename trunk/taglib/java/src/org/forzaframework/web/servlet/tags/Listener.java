package org.forzaframework.web.servlet.tags;

/**
 * @author cesarreyes
 *         Date: 23-sep-2008
 *         Time: 21:53:16
 */
public class Listener {

    private String eventName;
    private String handler;

    public Listener() {
    }

    public Listener(String eventName, String handler) {
        this.eventName = eventName;
        this.handler = handler;
    }

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

}
