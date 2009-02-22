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

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import java.util.Locale;

/**
 * User: Cesar Reyes
 * Date: 21/12/2006
 * Time: 05:45:38 PM
 * Description:
 */
public abstract class BaseTag extends BodyTagSupport {
    protected Tag parent;
    protected RequestContext requestContext;
    protected PageContext pageContext;

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public Tag getParent() {
        return this.parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag findParent(Class target){
        return findParent(this, target);
    }

    public Tag findParent(Tag current, Class target){
        if(current.getParent() != null){
            if(current.getParent().getClass().equals(target)){
                return current.getParent();
            }else{
                Class[] interfaces = current.getParent().getClass().getInterfaces();
                if(interfaces.length > 0){
                    for(Class clazz : interfaces){
                        if(clazz.equals(target)){
                            return current.getParent();
                        }
                    }
                }
                Class superclass = current.getParent().getClass().getSuperclass();
                if(superclass != null){
                    if(superclass.equals(target)){
                        return current.getParent();
                    }
                    else{
                        Class[] superclassInterfaces = superclass.getInterfaces();
                        if(superclassInterfaces.length > 0){
                            for(Class clazz : superclassInterfaces){
                                if(clazz.equals(target)){
                                    return current.getParent();
                                }
                            }
                        }
                    }
                }
                return findParent(current.getParent(), target);
            }
        }
        else{
            return null;
        }
    }

    public int doStartTag() throws JspException {
        initRequestContext();

        return IterationTag.EVAL_BODY_AGAIN;
    }

    public void initRequestContext() {
        try {
            this.requestContext = new RequestContext((HttpServletRequest) this.pageContext.getRequest());
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            pageContext.getServletContext().log("Exception in custom tag", ex);
        }
    }

    protected final RequestContext getRequestContext() {
		return this.requestContext;
	}

    private MessageSource getMessageSource() {
        return requestContext.getWebApplicationContext();
    }

    protected String getText(String key){
        Locale locale = pageContext.getRequest().getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }
        try{
            return getMessageSource().getMessage(key, null, locale); 
        }
        catch(Exception ex){
            return "???" + key + "???";
        }
    }
}
