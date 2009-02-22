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
